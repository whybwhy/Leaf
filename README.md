# 채팅시스템 토큰 발급 / 캐쉬 받기 API

#### Spring Boot 기반 DB CRUD REST API

- Spring Boot 2.3.4
- Spring Data JPA 2.3.4 (JPA)
- Hibernate 5.4.21 (JPA)
- Lombok (Support)
- JUnit 5 (Test)
- LogBack 1.2.3 (Log)
- mysql 8.0

-------
HOST : http://localhost:8080

<pre>
<table>
<tr><td>기능</td><td>httpMethod</td><td>uri</td></tr>
<tr><td>채팅방 + 최초 사용자 생성 </td><td>GET</td><td>/create/room/{title}</td></tr>
<tr><td>채팅방 입장</td><td>GET</td><td>/join/room</td></tr>
<tr><td>토큰발급</td><td>GET</td><td>/produce/{count}/{amount}</td></tr>
<tr><td>캐쉬받기</td><td>GET</td><td>/consume/{token}</td></tr>
<tr><td>토큰 상태 조회</td><td>GET</td><td>/status/{token}</td></tr>
</table>
</pre>
 

-------
TEST

<pre>
LeafHttpClient.http

#### 파라메터나 헤더 데이터에 한글 처리는 안되어 있습니다. 알파벳이나 숫자로 테스트가 가능합니다.

### 채팅방 + 신규 유저 생성
GET http://localhost:8080/create/room/title
X-USER-ID: test
Connection: Close

### 채팅방 입장
GET http://localhost:8080/join/room
X-USER-ID: test4
X-ROOM-ID: 26
Connection: Close


### 토큰 발행
GET http://localhost:8080/produce/2/1000
X-USER-ID: test
X-ROOM-ID: 26
Connection: Close

### 토큰 소모
GET http://localhost:8080/consume/yxY
X-USER-ID: test
X-ROOM-ID: 26
Connection: Close

### 토큰 상태
GET http://localhost:8080/status/rQd
X-USER-ID: test3
X-ROOM-ID: 17
Connection: Close
###
</pre>

<pre>
ServiceTests.java

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
</pre>

-------

#### Controller 샘플
<pre>
TokenController.java

        @RequestMapping(value="/produce/{count}/{amount}", method = {RequestMethod.GET})
        public ResponseEntity<LeafResponse> produce(@RequestHeader(value="X-USER-ID") String userId
                                        , @RequestHeader(value="X-ROOM-ID") Long roomId
                                        , @PathVariable int count
                                        , @PathVariable Long amount) {
    
            Token token = tokenService.produce(roomId, userId, count, amount);
            LeafResponse<Token> result = LeafResponse.<Token>builder().result(token).build();
    
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
</pre>

-------

#### Service 샘플
<pre>

TokenService.java
    
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
    
            // 토큰 생성자 유효성 체크
            if(tokenInfo.getOwnerId().equals(userId))
                throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_OWNER2);
    
            // 토큰 조회 기한 유효성 체크
            if(tokenInfo.getCreatedDate().plus(token_read_time, ChronoUnit.DAYS).isBefore(LocalDateTime.now()))
                throw new GlobalException(GlobalExceptionMessage.INVALID_TOKEN_DATE);
    
            return tokenInfo;
        }
</pre>

-------

#### DB 연관관계
하...작성중....

#### DDL

<pre>
CREATE TABLE `token` (
  `token` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `expired_date` datetime DEFAULT NULL,
  `owner_id` varchar(255) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  `total_amount` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `token_history` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK14x0htp7bokse1sgol03qyhon` (`token`),
  CONSTRAINT `FK14x0htp7bokse1sgol03qyhon` FOREIGN KEY (`token`) REFERENCES `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `chat_room` (
  `chat_room_id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `chat_title` varchar(255) DEFAULT NULL,
  `owner_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`chat_room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `chat_user` (
  `user_id` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `chat_room_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK67fgnu7l9ghfnf79aj7p316rx` (`chat_room_id`),
  CONSTRAINT `FK67fgnu7l9ghfnf79aj7p316rx` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

</pre>

