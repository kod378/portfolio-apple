package com.portfolio.apple.domain.account.admin;

import com.portfolio.apple.domain.account.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class Admin extends User {

    private final AdminAccount adminAccount;

    public Admin(AdminAccount adminAccount) {
        super(adminAccount.getAccountId(), adminAccount.getPassword(), List.of(new SimpleGrantedAuthority(Role.ADMIN.getKey())));
        this.adminAccount = adminAccount;
    }
}
