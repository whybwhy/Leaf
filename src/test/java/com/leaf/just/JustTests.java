package com.leaf.just;

import com.leaf.domain.ChatRoom;
import com.leaf.domain.Token;
import com.leaf.entity.TokenRepository;
import com.leaf.exception.GlobalException;
import com.leaf.exception.GlobalExceptionMessage;
import com.leaf.service.ChatRoomService;
import com.leaf.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
class JustTests {

    @Test
    void type01() throws Exception {

        System.out.println(URLEncoder.encode("X-USER-ID", "utf-8"));
        System.out.println(URLEncoder.encode("X-ROOM-ID", "utf-8"));


    }

    @Test
    void type02() {

        String str = null;
        Optional.ofNullable(str).orElseThrow(GlobalExceptionMessage.INVALID_PARAMETER::exception);
    }


    @Test
    void type03() {
        /*System.out.println(Integer.compare(0, 10000));
        System.out.println(Integer.compare(10000, 0));
        System.out.println(Integer.compare(1, 1));

        Long amount = 1000l;
        int count = 3;

        Optional.ofNullable(amount)
                .filter(d -> d > 0 )
                .map(d -> distribute(count, amount))
                .orElseThrow(GlobalExceptionMessage.INVALID_PARAMETER::exception);*/


    }

    public Long type04(long count, long amount) {




        /*BigDecimal divisor = BigDecimal.valueOf(count);
        BigDecimal dividend = BigDecimal.valueOf(amount);

        try {
            dividend.divide(divisor).longValue();
        } catch (ArithmeticException e) {
            throw new GlobalException(GlobalExceptionMessage.INVALID_PARAMETER);
        }*/

        return 0l;
    }


    @Autowired
    private TokenRepository tokenRepository;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private TokenService tokenService;

    @Test
    public void type05() {
        Token token = Token.builder()
                .userId("123")
                .count(1000)
                .amount(1000l)
                .token("T0k")
                .expiredDate(LocalDateTime.now())
                .build();
        tokenRepository.save(token);

    }

    @Test
    public void type06() {
        chatRoomService.create("파랑새반", "윤볾");
    }



    @Test
    public void type07() {

        // ChatUser chatUser = chatUserService.create(2l, "나뽀");
        // log.info("ChatUser : ", chatUser);


    }

    @Test
    public void type08() throws Exception {
        ChatRoom chatRoom = chatRoomService.create("파랑새반", "윤볾");
//        ChatUser chatUser = chatRoomService.create(chatRoom.getChatRoomId(), chatRoom.getOwnerId());
        //       log.info("ChatUser : ", chatUser);


    }

    @Test
    public void type09() {
        Token tokenInfo = tokenService.status("Ezw", "윤볾");
        log.info("Token : {}", tokenInfo);
    }

    @Test
    void type10() throws Exception {

        System.out.println(URLEncoder.encode("X-USER-ID", "utf-8"));
        System.out.println(URLEncoder.encode("X-ROOM-ID", "utf-8"));


    }

    @Test
    void type11() {

        String str = null;
        Optional.ofNullable(str).orElseThrow(GlobalExceptionMessage.INVALID_PARAMETER::exception);
    }


    @Test
    void type12() {
        System.out.println(Integer.compare(0, 10000));
        System.out.println(Integer.compare(10000, 0));
        System.out.println(Integer.compare(1, 1));

        //    tokenService.creat("111", 0, 1000l);
    }

    @Test
    public void type13() {


        /*String token = tokenGenerator.generate();

        Mono<Token> postFlux = Mono.just(token)
                .flatMap(t ->  Mono.just(repository.findById(t).orElseThrow(GlobalExceptionMessage.INVALID_ROOM::exception)))
                .retry(2)
                ;*/


       /* Token token = Token.builder().expiredDate(LocalDateTime.now()).amount(10000l).count(5).token("a").userId("1234").build();
        repository.save(token);


        String result = tokenGenerator.generate(2);
        System.out.println(result);*/

    }

    public String type14(int count) {

/*
        if (count == 0)
            throw new GlobalException(GlobalExceptionMessage.INVALID_PARAMETER);

        //String token = tokenGenerator.generate();
        String token = "b";

        Optional<Token> tokenOptional = repository.findById(token);
        if (tokenOptional.isPresent()) {
            token = factorial(count - 1);
        }

        return token;
*/
return null;
    }


    @Test
    public void type15() {
        tokenService.consume(12l, "나뽀", "A6c");
    }




}
