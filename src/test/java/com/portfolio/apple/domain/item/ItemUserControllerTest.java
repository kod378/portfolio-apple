package com.portfolio.apple.domain.item;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class ItemUserControllerTest {


    @Autowired
    private CreateEntity createEntity;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("상품 리스트 페이지 조회 - 정상")
    @Test
    void itemUserList() throws Exception {
        mockMvc.perform(get("/item/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/item/list"))
                .andExpect(model().attributeExists("categoryDtoList"))
                .andExpect(model().attributeExists("itemDtoPage"))
                .andExpect(model().attributeExists("pageStart"))
                .andExpect(model().attributeExists("pageEnd"));
    }

    @DisplayName("상품 상세 페이지 조회 - 정상")
    @Test
    void itemUserDetail() throws Exception {
        Item savedSampleItem = createEntity.getSavedSampleItem();

        mockMvc.perform(get("/item/" + savedSampleItem.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/item/detail"))
                .andExpect(model().attributeExists("itemDto"));
    }
}