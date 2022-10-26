package com.portfolio.apple.config.auth;

import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.account.user.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserAccountRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        UserAccount user = saveOrUpdate(attributes);
        Map<String, Object> userAttributesMap = getUserAttributesMap(attributes.getAttributes(), user);

//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
        return new UserAdapter(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                userAttributesMap,
                attributes.getNameAttributeKey());
    }

    private Map<String, Object> getUserAttributesMap(Map<String, Object> attributes, UserAccount user) {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("id", attributes.get("id"));
        userAttributes.put("userAccount", user);
        return userAttributes;
    }

    private UserAccount saveOrUpdate(OAuthAttributes attributes) {
        UserAccount user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateEmail(attributes.getEmail()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
