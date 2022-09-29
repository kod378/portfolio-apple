package com.portfolio.apple.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    @Embedded
    private Address address;

}
