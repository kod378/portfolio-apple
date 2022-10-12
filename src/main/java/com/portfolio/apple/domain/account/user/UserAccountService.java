package com.portfolio.apple.domain.account.user;

import com.portfolio.apple.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public void changeBasicAddress(UserAccount userAccount, Address address) {
        userAccount.changeAddress(address);
    }

}
