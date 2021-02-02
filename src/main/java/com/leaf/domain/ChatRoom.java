package com.leaf.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leaf.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "chatUserList")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseTimeEntity implements Persistable<Long> {

    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column
    private String ownerId;

    @Column
    private String chatTitle;

    @OneToMany(mappedBy = "chatRoom")
    @JsonManagedReference
    private List<ChatUser> chatUserList = new ArrayList<>();

    @Builder
    public ChatRoom(String ownerId, String chatTitle) {
        this.ownerId = ownerId;
        this.chatTitle = chatTitle;
    }

    @Override
    public Long getId() {
        return this.chatRoomId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
