package com.portfolio.apple.domain.item;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithAdminAccount;
import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.account.admin.AdminJoinFormDTO;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class ItemAdminControllerTest {

    @Autowired private AdminAccountService adminAccountService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private CreateEntity createEntity;

    @BeforeEach
    void setUp() {
        createEntity.setUpAdminAccount();
    }

    @DisplayName("아이템 리스트 화면 조회 - 정상")
    @TestWithAdminAccount
    void viewItemList() throws Exception {
        mockMvc.perform(get("/admin/item/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/item/list"))
                .andExpect(model().attributeExists("categoryDtoList"))
                .andExpect(model().attributeExists("itemDtoPage"));
    }

    @DisplayName("아이템 저장 화면 조회 - 정상")
    @TestWithAdminAccount
    void viewItemSave() throws Exception {
        mockMvc.perform(get("/admin/item/save"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/item/save"))
                .andExpect(model().attributeExists("categoryDtoList"));
    }

    @DisplayName("아이템 저장 - 정상")
    @TestWithAdminAccount
    void itemSave() throws Exception {
        //카테고리 등록
        Category category = createEntity.saveCategory("testCategory");

        Path path = Paths.get(Objects.requireNonNull(getClass().getResource("/static/images/_freeApple.jpg")).toURI());
        byte[] bytes = Files.readAllBytes(path);

        MockMultipartFile file = new MockMultipartFile("representationImage", "_freeApple.jpg", "image/jpg", bytes);
        mockMvc.perform(multipart("/admin/item")
                    .file(file)
                    .param("name", "testItem")
                    .param("price", "1000")
                    .param("stockQuantity", "100")
                    .param("content", "testContent")
                    .param("categoryName", category.getName()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/item/list"));
    }

    @DisplayName("아이템 저장 - 에러 - 비정상 파일")
    @TestWithAdminAccount
    void itemSaveWithInvalidFile() throws Exception {
        //카테고리 등록
        Category category = createEntity.saveCategory("testCategory");

        MockMultipartFile file = new MockMultipartFile("representationImage", "_freeApple.txt", "text/plain", "testByte".getBytes());
        mockMvc.perform(multipart("/admin/item")
                    .file(file)
                    .param("name", "testItem")
                    .param("price", "1000")
                    .param("stockQuantity", "100")
                    .param("content", "testContent")
                    .param("categoryName", category.getName()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/item/save"))
                    .andExpect(flash().attribute("message", "이미지 파일만 업로드 가능합니다."));
    }

    @DisplayName("아이템 저장 - 에러 - 대표이미지 누락")
    @TestWithAdminAccount
    void itemSaveWithMissingFile() throws Exception {
        //카테고리 등록
        Category category = createEntity.saveCategory("testCategory");

        mockMvc.perform(multipart("/admin/item")
                    .param("name", "testItem")
                    .param("price", "1000")
                    .param("stockQuantity", "100")
                    .param("content", "testContent")
                    .param("categoryName", category.getName()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/item/save"))
                    .andExpect(flash().attribute("message", "파일을 선택해주세요."));
    }

    @DisplayName("아이템 수정화면 - 정상")
    @TestWithAdminAccount
    void viewItemUpdate() throws Exception {
        Category category = createEntity.saveCategory("testCategory");
        List<ItemFile> itemFile = createEntity.createItemFileList();
        Item item = createEntity.saveItem(category, itemFile, "testItem");
        mockMvc.perform(get("/admin/item/update/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/item/save"))
                .andExpect(model().attributeExists("categoryDtoList"))
                .andExpect(model().attributeExists("itemDTO"));
    }

    @DisplayName("아이템 수정 - 정상")
    @TestWithAdminAccount
    void itemUpdate() throws Exception {
        Category category = createEntity.saveCategory("testCategory");
        List<ItemFile> itemFile = createEntity.createItemFileList();
        Item item = createEntity.saveItem(category, itemFile, "testItem");
        Path path = Paths.get(Objects.requireNonNull(getClass().getResource("/static/images/_freeApple.jpg")).toURI());
        byte[] bytes = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("representationImage", "_freeApple.jpg", "image/jpg", bytes);

        mockMvc.perform(multipart("/admin/item")
                    .file(file)
                    .param("id", String.valueOf(item.getId()))
                    .param("name", "updatedItem")
                    .param("price", "1000")
                    .param("stockQuantity", "100")
                    .param("content", "testContent")
                    .param("categoryName", category.getName()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/item/list"));

        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals("updatedItem", updatedItem.getName());
    }
}