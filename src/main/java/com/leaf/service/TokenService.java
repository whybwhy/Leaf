package com.leaf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaf.LeafFilter;
import com.leaf.domain.ChatUser;
import com.leaf.domain.Token;
import com.leaf.domain.TokenHistory;
import com.leaf.entity.TokenHistoryRepository;
import com.leaf.entity.TokenRepository;
import com.leaf.exception.GlobalException;
import com.leaf.exception.GlobalExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${service.token.retry}") private int retry;
    @Value("${service.token.expire}") private int token_expire_time;
    @Value("${service.token.read.expire}") private int token_read_time;

    private final TokenRepository tokenRepository;
    private final TokenHistoryRepository tokenHistoryService;
    private final ChatRoomService chatRoomService;
    private final TokenGenerator tokenGenerator;

    /**s
     * 동일한 금액으로 분배
     * dividend/divisor 나머지가 발생할 경우 사용자 안내 메세지
     * @param count
     * @param amount
     * @return
     */
    private Long distribute(long count, long amount) {

        long result;

        BigDecimal divisor = BigDecimal.valueOf(count);
        BigDecimal dividend = BigDecimal.valueOf(amount);

        try {
            result = dividend.divide(divisor).longValue();
        } catch (ArithmeticException e) {
            throw new GlobalException(GlobalExceptionMessage.INVALID_PARAMETER);
        }

        return result;
    }

    /**
     * 토큰 생성
     * - 동일한 금액으로 분배 하되, 나머지가 나올 경우 재시도하도록 사용자 안내 메세지 응답
     * @param userId
     * @param roomId
     * @param count
     * @param amount
     * @See TokenGenerator 토큰은 알파벳(대소문자)+숫자 랜덤으로 ${service.token.length} 사이즈로 발급되며 최대 retrySize 만큼 중복 발급 할 경우 연속될 경우 실패 메세지 발생
     * @return
     */
    public Token produce(Long roomId, String userId, int count, Long amount) {

        // 채팅방과 사용자의 정합성 검증
        chatRoomService.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(GlobalExceptionMessage.INVALID_USER_AND_ROOM::exception);

        // 토큰 발급/검증
        String tokenStream = tokenGenerator.generate(retry);

        // 토큰 저장
        Token token = Token.builder()
                .token(tokenStream)
                .amount(this.distribute(count, amount))
                .count(count)
                .roomId(roomId)
                .userId(userId)
                .totalAmount(amount)
                .expiredDate(LocalDateTime.now().plus(token_expire_time, ChronoUnit.MINUTES))
                .build();

        return tokenRepository.save(token);
    }

    @Transactional
    public TokenHistory consume(Long chatRoomId, String userId, String token) {

        // userId, roomId 정합성 체크
        chatRoomService.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(GlobalExceptionMessage.INVALID_USER_AND_ROOM::exception);

        Token tokenInfo = tokenRepository.findById(token).orElseThrow(GlobalExceptionMessage.INVALID_TOKEN::exception);

        // 토큰 생성자 캐시 받기 불가
        if (tokenInfo.getOwnerId().equals(userId))
            throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_OWNER);

        // 해당 채팅방에서 생성된 토큰 여부 유효성 체크
        if (!tokenInfo.getRoomId().equals(chatRoomId))
            throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_BY_ROOM);

        // 토큰 유효시간 체크
        if(tokenInfo.getExpiredDate().isBefore(LocalDateTime.now()))
            throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN);

        // 토큰 전체 소모 여부 체크
        if (tokenInfo.getTokenHistoryList().size() == tokenInfo.getCount())
            throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_ALL_DONE);

        // 동일한 사용자가 중복 캐시 발급 불가
        Optional<TokenHistory> tokenHistoryOptional = tokenInfo.getTokenHistoryList().stream().filter(history -> history.getUserId().equals(userId)).findFirst();
        if(tokenHistoryOptional.isPresent())
            throw new GlobalException(GlobalExceptionMessage.INVALID_DUPLICATE_TOKEN);

        // 토큰 발급 성공 내역 업데이트
        TokenHistory history = TokenHistory.builder().token(tokenInfo).amount(tokenInfo.getAmount()).userId(userId).build();
        return tokenHistoryService.save(history);
    }

    /**
     * 토큰 정보 조회
     * - 토큰 생성자만 조회 가능
     * - 유효한 토큰만 조회 가능
     * - 토큰 발급 7일 내 조회 가능
     * @param token
     * @param userId
     * @return 뿌린시각, 뿌린금액, 받기완료된금액, 받기완료된정보([받은금액, 받은사용자 아이디])
     */
    @Transactional
    public Token status(String token, String userId) {

        // 토큰 유효성 체크
        Token tokenInfo = tokenRepository.findById(token).orElseThrow(GlobalExceptionMessage.INVALID_TOKEN::exception);

        /**
         *  AS-IS
         *
        // 토큰 생성자 유효성 체크
        if(tokenInfo.getOwnerId().equals(userId))
            throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_OWNER);

        // 토큰 조회 기한 유효성 체크
        if(tokenInfo.getCreatedDate().plus(token_read_time, ChronoUnit.DAYS).isBefore(LocalDateTime.now()))
            throw new GlobalException(GlobalExceptionMessage.INVALID_EXPIRED_TOKE);*/

        // TO-BE
        LeafFilter.filter(
                  () -> LeafFilter.test(GlobalExceptionMessage.INVALID_TOKEN_OWNER::exception, tokenInfo.getOwnerId().equals(userId))
                , () -> LeafFilter.test(GlobalExceptionMessage.INVALID_EXPIRED_TOKE::exception, tokenInfo.getCreatedDate().plus(token_read_time, ChronoUnit.DAYS).isBefore(LocalDateTime.now()))
        );

        return tokenInfo;
    }
}
