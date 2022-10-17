package com.portfolio.apple;

import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.account.admin.AdminJoinFormDTO;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitListener implements ApplicationListener<ApplicationStartedEvent> {

    private final AdminAccountService adminAccountService;

    public InitListener(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
//        adminAccountService.saveAdminAccount(new AdminJoinFormDTO("test", "test", "test"));
//        System.out.println("관리자 계정 초기 생성됨");
    }
}
