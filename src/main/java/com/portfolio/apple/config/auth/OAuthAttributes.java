package com.portfolio.apple.config.auth;

import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.user.UserAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

//        return ofGoogle(userNameAttributeName, attributes);   // 구글 로그인 안함
        return ofNaver("id", attributes);
    }


    private static OAuthAttributes ofNaver(String nameAttributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    private static OAuthAttributes ofGoogle(String nameAttributeKey,
                                            Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
