package com.portfolio.apple.config.auth;

import com.portfolio.apple.domain.UserAccount;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    public SessionUser(UserAccount user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
