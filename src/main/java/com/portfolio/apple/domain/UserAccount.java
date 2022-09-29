package com.portfolio.apple.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserAccount extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

//    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public UserAccount update(String name) {
        this.name = name;
        return this;
    }

    public UserAccount updateEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
