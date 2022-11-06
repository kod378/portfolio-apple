package com.portfolio.apple;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserAccountSecurityContextFactory.class)
public @interface WithUserAccount {
    String value();
}
