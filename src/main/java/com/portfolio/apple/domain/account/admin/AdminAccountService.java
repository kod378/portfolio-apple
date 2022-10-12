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
    private final AuthenticationManager authenticationManager;

    public AdminAccount saveAdminAccount(AdminJoinFormDTO adminJoinFormDTO) {
        AdminAccount adminAccount = AdminAccount.builder()
                .accountId(adminJoinFormDTO.getAccountId())
                .password(passwordEncoder.encode(adminJoinFormDTO.getPassword()))
                .role(Role.ADMIN)
                .build();

        return adminAccountRepository.save(adminAccount);
    }

//    public void loginProcess(AdminLoginFormDTO adminLoginFormDTO) {
//        AdminAccount adminAccount = adminAccountRepository.findByAccountId(adminLoginFormDTO.getAccountId())
//                .orElseThrow(() -> new UsernameNotFoundException(adminLoginFormDTO.getAccountId()));
//
//        if(!passwordEncoder.matches(adminLoginFormDTO.getPassword(), adminAccount.getPassword())) {
//            throw new UsernameNotFoundException(adminLoginFormDTO.getAccountId());
//        }
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                adminLoginFormDTO.getAccountId(), adminLoginFormDTO.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
//        Authentication authenticate = authenticationManager.authenticate(token);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//    }
}
