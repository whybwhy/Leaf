package com.leaf.entity;

import com.leaf.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {

    List<ChatUser> findByUserId(String userId);
}
