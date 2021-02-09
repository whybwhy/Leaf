package com.leaf.just;

import com.leaf.LeafFilter;
import com.leaf.domain.Token;
import com.leaf.entity.TokenHistoryRepository;
import com.leaf.entity.TokenRepository;
import com.leaf.exception.GlobalExceptionMessage;
import com.leaf.service.ChatRoomService;
import com.leaf.service.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class FilterTests {

    @Autowired private TokenRepository tokenRepository;
    @Autowired private TokenHistoryRepository tokenHistoryService;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private TokenGenerator tokenGenerator;

    @Test
    public void single() {

        Long roomId = 3l;
        String userId = "whybwhy";
        String token = "Gdw";

        /*LeafFilter.filter(

                () -> LeafFilter.test("1st Exception", true)
                , () -> LeafFilter.test("2nd Exception", false)
        );*/
    }

    @Test
    public void dual() {
        /*LeafFilter.filter(
                RuntimeException.class
                , () -> LeafFilter.test("1st Exception", false)
                , () -> LeafFilter.test("2nd Exception", true)
        );*/
    }

    @Value("${service.token.read.expire}") private int token_read_time;

    @Test
    public void status() {

        String token = "2AF";
        String userId = "test1";

        Token tokenInfo = tokenRepository.findById(token).orElseThrow(GlobalExceptionMessage.INVALID_TOKEN::exception);

        LeafFilter.filter(
                  () -> LeafFilter.test(GlobalExceptionMessage.INVALID_TOKEN_OWNER::exception, tokenInfo.getOwnerId().equals(userId))
                , () -> LeafFilter.test(GlobalExceptionMessage.INVALID_EXPIRED_TOKE::exception, tokenInfo.getCreatedDate().plus(token_read_time, ChronoUnit.DAYS).isBefore(LocalDateTime.now()))
        );


        LeafFilter.filter(
                () -> LeafFilter.test(() -> new RuntimeException("INVALID_TOKEN_OWNER"), tokenInfo.getOwnerId().equals(userId))
                , () -> LeafFilter.test(() -> new RuntimeException("INVALID_EXPIRED_TOKE"), tokenInfo.getCreatedDate().plus(token_read_time, ChronoUnit.DAYS).isBefore(LocalDateTime.now()))
        );
    }
}
