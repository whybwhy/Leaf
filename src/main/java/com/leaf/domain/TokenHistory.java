package com.leaf.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leaf.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@ToString(exclude = {"token", "id"})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String userId;

    @Column
    private Long amount;

    @ManyToOne
    @JoinColumn(name="token")
    @JsonBackReference
    private Token token;

    @Builder
    public TokenHistory(String userId, Token token, Long amount) {
        this.userId = userId;
        this.token = token;
        this.amount = amount;
    }
}
