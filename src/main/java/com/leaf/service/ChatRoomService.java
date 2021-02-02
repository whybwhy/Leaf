package com.leaf.service;

import com.leaf.domain.ChatRoom;
import com.leaf.domain.ChatUser;
import com.leaf.entity.ChatRoomRepository;
import com.leaf.entity.ChatUserRepository;
import com.leaf.exception.GlobalException;
import com.leaf.exception.GlobalExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    /**
     * 채팅방 + 채팅 유저 신규 생성
     * @param title
     * @param ownerId
     * @return
     */
    @Transactional
    public ChatRoom create(String title, String ownerId) {

        ChatRoom chatRoom = ChatRoom.builder().chatTitle(title).ownerId(ownerId).build();
        ChatUser chatUser = ChatUser.builder().userId(ownerId).room(chatRoom).build();

        chatRoomRepository.save(chatRoom);
        chatUserRepository.save(chatUser);

        return chatRoom;
    }

    /**
     * 채팅방 + 신규 채팅 유저 입장
     * @param chatRoomId
     * @param userId
     * @return
     */
    @Transactional
    public ChatUser join(Long chatRoomId, String userId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(GlobalExceptionMessage.INVALID_ROOM::exception);

        // 중복 유저 체크
        Optional<ChatUser> chatUserOptional = chatRoom.getChatUserList().stream()
                .filter(chatUser -> chatUser.getUserId().equals(userId)).findFirst();

        if(chatUserOptional.isPresent())
            throw new GlobalException(GlobalExceptionMessage.INVALID_DUPLICATE_USER);

        ChatUser user = ChatUser.builder().userId(userId).room(chatRoom).build();

        return chatUserRepository.save(user);
    }


    public Optional<ChatUser> findByChatRoomIdAndUserId(Long chatRoomId, String userId) {
        return chatUserRepository.findByUserId(userId).stream()
                .filter(chatUser -> chatUser.getChatRoom().getChatRoomId().equals(chatRoomId)).findFirst();
    }

}
