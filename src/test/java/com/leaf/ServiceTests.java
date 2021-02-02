package com.leaf;

import com.leaf.domain.ChatRoom;
import com.leaf.domain.ChatUser;
import com.leaf.domain.Token;
import com.leaf.domain.TokenHistory;
import com.leaf.service.ChatRoomService;
import com.leaf.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@SpringBootTest
public class ServiceTests {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ChatRoomService chatRoomService;

    @DisplayName("채팅방 생성")
    @Test
    public void createRoom() {

        // given
        String title = "파랑새반 부모방";
        String userId = "윤볾";

        // when
        ChatRoom chatRoom = chatRoomService.create(title, userId);

        // then
        assertAll("create"
        ,() -> assertEquals(title, chatRoom.getChatTitle())
        ,() -> assertEquals(userId, chatRoom.getOwnerId())
        ,() -> log.info("{}", chatRoom.getChatRoomId())
        );
    }

    @DisplayName("채팅방 입장")
    @Test
    public void createUser() {

        // given
        Long chatRoomId = 17l;
        String userId = "나윤";

        // when
        ChatUser chatUser = chatRoomService.join(chatRoomId, userId);

        // then
        assertAll("create"
                ,() -> assertEquals(chatRoomId, chatUser.getChatRoom().getChatRoomId())
                ,() -> assertEquals(userId, chatUser.getUserId())
                ,() -> log.info("{}", chatUser)
        );

    }

    @DisplayName("토큰 생성")
    @Test
    public void produce() {

        // given & when
        Token token = tokenService.produce(17l, "test", 2, 10000l);

        // then
        assertAll("produce"
                ,() -> assertEquals("나뽀", token.getOwnerId())
                ,() -> assertEquals(Long.valueOf(10000), token.getTotalAmount())
                ,() -> log.info("token : {}", token)
        );
    }

    @DisplayName("토큰 사용")
    @Test
    public void consumer() {

        // given
        Long chatRoomId = 17l;
        String userId = "나란";
        String token = "MOA";

        // when
        TokenHistory history = tokenService.consume(chatRoomId, userId, token);

        // then
        assertAll("consumer"
                ,() -> log.info("history : {}", history)
        );
    }

    @DisplayName("토큰 정보 & 사용 내역 조회")
    @Test
    public void read() {

        // given
        String userId = "나란";
        String token = "MOA";

        // when
        Token tokenInfo = tokenService.status(token, userId);

        // then
        assertAll("token"
                ,() -> log.info("TokenInfo : {}", tokenInfo)
        );
    }

}
