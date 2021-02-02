package com.leaf.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leaf.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = "chatRoom")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser extends BaseTimeEntity {

    @Id
    @Column(name="user_id")
    private String userId;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id")})
    @JsonBackReference
    private ChatRoom chatRoom;

    @Builder
    public ChatUser (String userId, ChatRoom room) {
        this.userId = userId;
        this.chatRoom = room;
    }
}
