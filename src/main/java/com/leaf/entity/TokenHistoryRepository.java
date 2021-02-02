package com.leaf.entity;

import com.leaf.domain.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenHistoryRepository extends JpaRepository<TokenHistory, Long> {

    List<TokenHistory> findByToken(String token);
}
