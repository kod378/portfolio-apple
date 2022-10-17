package com.portfolio.apple.domain.item;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomControllerTest
class ItemApiControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemFileRepository itemFileRepository;
    @Autowired private CreateEntity createEntity;

    @DisplayName("아이템 삭제 - 정상")
    @Test
    public void deleteItem() throws Exception {
        Category category = createEntity.saveCategory("testCategory");
        List<ItemFile> itemFile = createEntity.createItemFileList();
        Long itemFileId = itemFile.get(0).getId();
        Item item = createEntity.saveItem(category, itemFile, "testItem");
        Long itemId = item.getId();
        mockMvc.perform(delete("/api/item/" + itemId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(itemId)));

        assertThat(itemRepository.findById(itemId)).isEmpty();
        assertThat(itemFileRepository.findById(itemFileId)).isEmpty();
    }
}