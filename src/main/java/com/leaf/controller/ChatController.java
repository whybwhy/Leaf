package com.leaf.controller;

import com.leaf.domain.ChatRoom;
import com.leaf.domain.ChatUser;
import com.leaf.service.ChatRoomService;
import com.leaf.vo.LeafResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRoomService chatRoomService;

    @RequestMapping(value="/create/room/{title}", method = {RequestMethod.GET})
    public ResponseEntity<LeafResponse> produce(@RequestHeader(value="X-USER-ID") String userId
                                    , @RequestHeader(value="X-ROOM-ID", required = false) Long roomId
                                    , @PathVariable String title) {

        ChatRoom chatRoom = chatRoomService.create(title, userId);
        LeafResponse<ChatRoom> result = LeafResponse.<ChatRoom>builder().result(chatRoom).build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/join/room", method = {RequestMethod.GET})
    public ResponseEntity<LeafResponse> consume(@RequestHeader(value="X-USER-ID") String userId
                                    , @RequestHeader(value="X-ROOM-ID") Long roomId) {

        ChatUser chatUser = chatRoomService.join(roomId, userId);
        LeafResponse<ChatUser> result = LeafResponse.<ChatUser>builder().result(chatUser).build();

        return new ResponseEntity(result, HttpStatus.OK);
    }


}
