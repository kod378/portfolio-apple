package com.portfolio.apple.config.interceptor;

import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserNameInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null) {
                Object principal = authentication.getPrincipal();
                if(principal instanceof UserAdapter) {
                    UserAccount userAccount = ((UserAdapter) principal).getUserAccount();
                    modelAndView.addObject("userName", userAccount.getName());
                }
            }
        }
    }
}
