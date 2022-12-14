package com.portfolio.apple.domain.account.admin;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithAdminAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class AdminControllerTest {

    @Autowired
    private AdminAccountRepository adminAccountRepository;

    @Autowired
    private AdminAccountService adminAccountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired CreateEntity createEntity;

    @BeforeEach
    void setUp() {
        createEntity.setUpAdminAccount();
    }

    @DisplayName("관리자 인덱스 화면 - 미인증")
    @Test
    public void indexWithNoAuthentication() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/admin/login"));
    }

    @DisplayName("관리자 인덱스 화면 - 인증")
    @TestWithAdminAccount
    public void indexWithAuthentication() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }

    @DisplayName("관리자 로그인 페이지 조회 - 정상")
    @Test
    public void loginPage() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }


    @DisplayName("관리자 회원가입 페이지 조회 - 정상")
    @Test
    public void joinPage() throws Exception {
        mockMvc.perform(get("/admin/join"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/join"))
                .andExpect(model().attributeExists("adminJoinFormDTO"));
    }

    @DisplayName("관리자 회원가입 - 입력값 오류")
    @Test
    public void join_with_wrong_input() throws Exception {
        mockMvc.perform(post("/admin/join")
                        .param("accountId", "test")
                        .param("password", "test")
                        .param("passwordConfirm", "test1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/join"));
    }

    @DisplayName("관리자 회원가입 - 입력값 정상")
    @Test
    public void join_with_correct_input() throws Exception {
        mockMvc.perform(post("/admin/join")
                        .param("accountId", "test")
                        .param("password", "test1234")
                        .param("passwordConfirm", "test1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"))
                .andExpect(flash().attributeExists("message"));

        Optional<AdminAccount> adminAccount = adminAccountRepository.findByAccountId("test");

        assertTrue(adminAccount.isPresent());
        assertNotEquals(adminAccount.get().getPassword(), "test1234");
    }

    @DisplayName("관리자 로그인 - 로그인 실패")
    @Test
    public void loginFail() throws Exception {

        AdminJoinFormDTO adminJoinFormDTO = new AdminJoinFormDTO("test", "test1234", "test1234");
        adminAccountService.saveAdminAccount(adminJoinFormDTO);

        mockMvc.perform(post("/admin/login")
                        .param("accountId", "test")
                        .param("password", "notMatchedPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error"));
    }

    @DisplayName("관리자 로그인 - 로그인 성공")
    @Test
    public void loginSuccess() throws Exception {

        AdminJoinFormDTO adminJoinFormDTO = new AdminJoinFormDTO("test", "test1234", "test1234");
        adminAccountService.saveAdminAccount(adminJoinFormDTO);

        mockMvc.perform(post("/admin/login")
                        .param("accountId", "test")
                        .param("password", "test1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

}