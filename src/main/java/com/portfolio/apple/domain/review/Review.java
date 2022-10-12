package com.portfolio.apple.domain.review;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.item.Item;

import javax.persistence.*;

@Entity
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Lob
    private String comment;

    @Column(nullable = false)
    private int grade;
}
