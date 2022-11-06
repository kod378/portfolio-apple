package com.portfolio.apple;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Test
@WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface TestWithAdminAccount {
}
