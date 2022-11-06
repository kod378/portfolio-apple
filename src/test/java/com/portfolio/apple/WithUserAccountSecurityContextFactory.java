package com.portfolio.apple;

import com.portfolio.apple.config.auth.OAuthAttributes;
import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.admin.AdminAccount;
import com.portfolio.apple.domain.account.admin.AdminAccountRepository;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.account.user.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class WithUserAccountSecurityContextFactory implements WithSecurityContextFactory<WithUserAccount> {

    private final UserAccountRepository userAccountRepository;

    @Override
    public SecurityContext createSecurityContext(WithUserAccount withUserAccount) {
        String username = withUserAccount.value();

        UserAccount user = UserAccount.builder()
                .name(username)
                .email("test@test.com")
                .role(Role.USER)
                .build();
        userAccountRepository.save(user);

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("id", "id");
        userAttributes.put("userAccount", user);

        OAuth2User principal = new UserAdapter(
                List.of(new SimpleGrantedAuthority(Role.USER.getKey())),
                userAttributes,
                "id");

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal, principal.getAuthorities(), "naver");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        return context;
    }
}
