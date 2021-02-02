package com.leaf.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leaf.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@ToString(exclude = {"token", "tokenHistoryList", "onwerId", "roomId", "count", "expiredDate"}, callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseTimeEntity {

    @Id
    private String token;

    @Column
    private String ownerId;

    @Column
    private Long roomId;

    @Column
    private Long amount;

    @Column
    private Long totalAmount;

    @Column
    private int count;

    @Column
    private LocalDateTime expiredDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="token")
    @JsonManagedReference
    private List<TokenHistory> tokenHistoryList;

    @Builder
    public Token(String token, String userId, Long roomId, Long amount, Long totalAmount,  int count, LocalDateTime expiredDate) {
        Assert.hasText(token, "token must not be null");
        Assert.hasText(userId, "userId must not be null");
        Assert.notNull(amount, "amount must not be null");

        this.token = token;
        this.ownerId = userId;
        this.roomId = roomId;
        this.amount = amount;
        this.count = count;
        this.totalAmount = totalAmount;
        this.expiredDate = expiredDate;

    }
}
