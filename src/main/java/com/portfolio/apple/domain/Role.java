package com.portfolio.apple.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("RULE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
