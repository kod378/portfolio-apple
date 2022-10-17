package com.portfolio.apple.domain.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.domain.account.admin.AdminAccountRepository;
import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.account.admin.AdminJoinFormDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class CategoryApiControllerTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AdminAccountService adminAccountService;
    @Autowired
    private AdminAccountRepository adminAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        CategorySaveRequestDTO categorySaveRequestDTO = new CategorySaveRequestDTO("test");
        categoryService.saveCategory(categorySaveRequestDTO);

        AdminJoinFormDTO adminJoinFormDTO = new AdminJoinFormDTO("admin", "1234", "1234");
        adminAccountService.saveAdminAccount(adminJoinFormDTO);

    }

    @DisplayName("카테고리 수정 - 성공")
    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void updateCategory() throws Exception {
        String categoryName = "test2";
        CategorySaveRequestDTO categorySaveRequestDTO = new CategorySaveRequestDTO(categoryName);
        mockMvc.perform(put("/api/category/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @DisplayName("카테고리 수정 - 실패")
    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void updateCategoryFail() throws Exception {
        String categoryName = "test2";
        CategorySaveRequestDTO categorySaveRequestDTO = new CategorySaveRequestDTO(categoryName);

        mockMvc.perform(put("/api/category/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("해당 카테고리가 없습니다. id=2"));
    }

    @DisplayName("카테고리 삭제 - 성공")
    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void deleteCategory() throws Exception {
        mockMvc.perform(delete("/api/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        Assertions.assertThat(categoryRepository.findById(1L).isEmpty()).isTrue();
    }

    @DisplayName("카테고리 삭제 - 실패")
    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void deleteCategoryFail() throws Exception {
        mockMvc.perform(delete("/api/category/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("해당 카테고리가 없습니다. id=2"));
    }
}