package com.portfolio.apple.domain.shoppingItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithUserAccount;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAdapter;
import com.portfolio.apple.domain.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class ShoppingItemApiControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CreateEntity createEntity;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ShoppingItemRepository shoppingItemRepository;

    private Item sampleItem;

    @BeforeEach
    void setUp() {
        sampleItem = createEntity.getSavedSampleItem();
    }


    @DisplayName("장바구니 추가 - 정상")
    @TestWithUserAccount
    public void addShoppingItem() throws Exception {
        // given
        ShoppingItemRequestDTO shoppingItemRequestDTO = getShoppingItemRequestDTO(1);

        // when
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        // then
        mockMvc.perform(post("/api/shoppingItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shoppingItemRequestDTO)))
                .andExpect(status().isOk())
                .andExpect((param) -> {
                    String contentAsString = param.getResponse().getContentAsString();
                    Matcher matcher = pattern.matcher(contentAsString);
                    assertThat(matcher.find()).isTrue();
                });
    }

    @DisplayName("장바구니 수량 변경 - 정상")
    @TestWithUserAccount
    public void updateShoppingItemQuantity() throws Exception {
        // given
        ShoppingItemRequestDTO shoppingItemRequestDTO = getShoppingItemRequestDTO(1);
        UserAccount userAccount = getUserAccount();
        ShoppingItem shoppingItem = ShoppingItem.createShoppingItem(userAccount, sampleItem, shoppingItemRequestDTO.getQuantity());
        shoppingItemRepository.save(shoppingItem);

        // when
        int updateQuantity = 2;
        // then
        mockMvc.perform(put("/api/shoppingItem/change/" + shoppingItem.getId() + "/" + updateQuantity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(updateQuantity))
                .andExpect(jsonPath("$.shoppingItemId").value(shoppingItem.getId()))
                .andExpect(jsonPath("$.stockQuantity").value(sampleItem.getStockQuantity()));
    }

    private ShoppingItemRequestDTO getShoppingItemRequestDTO(int quantity) {
        ShoppingItemRequestDTO shoppingItemRequestDTO = new ShoppingItemRequestDTO();
        shoppingItemRequestDTO.setItemId(sampleItem.getId());
        shoppingItemRequestDTO.setQuantity(quantity);
        return shoppingItemRequestDTO;
    }

    private static UserAccount getUserAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = ((UserAdapter) principal).getUserAccount();
        return userAccount;
    }

}