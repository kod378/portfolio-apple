package com.portfolio.apple.domain.itemFile;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithAdminAccount;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomControllerTest
class ItemFileApiControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ItemFileRepository itemFileRepository;
    @Autowired private CreateEntity createEntity;

    @BeforeEach
    void setUp() {
        createEntity.setUpAdminAccount();
    }

    @DisplayName("파일 삭제 - 정상")
    @TestWithAdminAccount
    public void deleteItemFile() throws Exception {
        Category category = createEntity.saveCategory("testCategory");
        List<ItemFile> itemFile = createEntity.createItemFileList();
        Item item = createEntity.saveItem(category, itemFile, "testItem");
        Long itemFileId = itemFile.get(0).getId();

        mockMvc.perform(delete("/admin/api/itemFile/" + itemFileId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(itemFileId)));

        assertThat(itemFileRepository.findById(itemFileId)).isEmpty();
    }
}