package com.portfolio.apple.domain.account.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginService implements UserDetailsService {

    private final AdminAccountRepository adminAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        AdminAccount adminAccount = adminAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException(accountId));
        return new Admin(adminAccount);
    }
}
