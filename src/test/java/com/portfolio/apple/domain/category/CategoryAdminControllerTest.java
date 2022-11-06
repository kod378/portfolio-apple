package com.portfolio.apple.domain.category;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithAdminAccount;
import com.portfolio.apple.domain.account.admin.AdminAccountRepository;
import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.account.admin.AdminJoinFormDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class CategoryAdminControllerTest {

    @Autowired
    private CreateEntity createEntity;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        createEntity.setUpAdminAccount();
    }

    @DisplayName("카테고리 조회")
    @TestWithAdminAccount
    public void list() throws Exception {
        mockMvc.perform(get("/admin/category/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/list"))
                .andExpect(model().attributeExists("categoryDtoList"));
    }

    @DisplayName("카테고리 등록 - 성공")
    @TestWithAdminAccount
    public void saveCategory() throws Exception {
        String categoryName = "test";
        mockMvc.perform(post("/admin/category")
                .param("name", categoryName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/category/list"));
    }

    @DisplayName("카테고리 등록 - 실패 - 빈 파라미터값")
    @TestWithAdminAccount
    public void saveCategoryFail() throws Exception {
        mockMvc.perform(post("/admin/category"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/list"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("categorySaveRequestDTO"));
    }

    @DisplayName("카테고리 등록 - 실패 - 중복이름")
    @TestWithAdminAccount
    public void saveCategoryFail2() throws Exception {
        String categoryName = "test";
        mockMvc.perform(post("/admin/category")
                .param("name", categoryName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/category/list"));

        mockMvc.perform(post("/admin/category")
                .param("name", categoryName))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "이미 존재하는 카테고리입니다."));
    }
}