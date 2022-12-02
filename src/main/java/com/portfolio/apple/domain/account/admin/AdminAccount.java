package com.portfolio.apple.domain.account.admin;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.account.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class AdminAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
