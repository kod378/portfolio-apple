package com.portfolio.apple.domain.account.admin;

import com.portfolio.apple.domain.account.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccount saveAdminAccount(AdminJoinFormDTO adminJoinFormDTO) {
        AdminAccount adminAccount = AdminAccount.builder()
                .accountId(adminJoinFormDTO.getAccountId())
                .password(passwordEncoder.encode(adminJoinFormDTO.getPassword()))
                .role(Role.ADMIN)
                .build();

        return adminAccountRepository.save(adminAccount);
    }
}
