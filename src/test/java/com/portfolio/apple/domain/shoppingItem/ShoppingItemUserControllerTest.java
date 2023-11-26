package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithUserAccount;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.mapper.ShoppingItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomControllerTest
class ShoppingItemUserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CreateEntity createEntity;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private ShoppingItemRepository shoppingItemRepository;
    @Autowired private ShoppingItemMapper shoppingItemMapper;
    private Item sampleItem;
    @BeforeEach
    void setUp() {
        sampleItem = createEntity.getSavedSampleItem();
    }

    @DisplayName("장바구니 목록 화면 조회 - 정상")
    @TestWithUserAccount
    @Transactional
    public void getShoppingItemList() throws Exception {
        // given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        ShoppingItem sampleShoppingItem = ShoppingItem.createShoppingItem(userAccount, sampleItem, 1);
        shoppingItemRepository.save(sampleShoppingItem);

        // when
        final List<ShoppingItemResponseDTO> shoppingItemResponseDTOList = shoppingItemMapper.entityListToResponseDtoList(shoppingItemRepository.findAllByUserAccount(userAccount));
        final int allTotalPrice = shoppingItemResponseDTOList.stream().mapToInt(ShoppingItemResponseDTO::getTotalPrice).sum();

        // then
        mockMvc.perform(get("/shoppingItem"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shoppingItemResponseDTOList"))
                .andExpect(model().attributeExists("allTotalPrice"))
                .andExpect(model().attributeExists("deliveryFee"))
                .andExpect(model().attribute("shoppingItemResponseDTOList", shoppingItemResponseDTOList))
                .andExpect(model().attribute("allTotalPrice", allTotalPrice));
    }

    @DisplayName("장바구니 목록 화면 조회 - 품절")
    @TestWithUserAccount
    @Transactional
    public void getShoppingItemList_soldOut() throws Exception {
        // given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        ShoppingItem sampleShoppingItem = ShoppingItem.createShoppingItem(userAccount, sampleItem, 1);
        shoppingItemRepository.save(sampleShoppingItem);
        // when
        sampleItem.removeStock(10);
        final List<ShoppingItemResponseDTO> shoppingItemResponseDTOList = shoppingItemMapper.entityListToResponseDtoList(shoppingItemRepository.findAllByUserAccount(userAccount));

        // then
        mockMvc.perform(get("/shoppingItem"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shoppingItemResponseDTOList"))
                .andExpect(model().attributeExists("allTotalPrice"))
                .andExpect(model().attributeExists("deliveryFee"))
                .andExpect(model().attributeExists("soldOutList"))
                .andExpect(model().attribute("soldOutList", shoppingItemResponseDTOList))
                .andExpect(model().attribute("allTotalPrice", 0));
    }

    @DisplayName("장바구니 주문 화면 조회 - 정상")
    @TestWithUserAccount
    public void getShoppingItemOrder() throws Exception {
        //given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        ShoppingItem sampleShoppingItem = ShoppingItem.createShoppingItem(userAccount, sampleItem, 1);
        shoppingItemRepository.save(sampleShoppingItem);
        //when
        final List<ShoppingItemResponseDTO> shoppingItemResponseDTOList = shoppingItemMapper.entityListToResponseDtoList(shoppingItemRepository.findAllByUserAccount(userAccount));
        final int allTotalPrice = shoppingItemResponseDTOList.stream().mapToInt(ShoppingItemResponseDTO::getTotalPrice).sum();

        //then
        mockMvc.perform(get("/shoppingItem/orderConfirm"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shoppingItemResponseDTOList"))
                .andExpect(model().attributeExists("allTotalPrice"))
                .andExpect(model().attributeExists("deliveryFee"))
                .andExpect(model().attribute("shoppingItemResponseDTOList", shoppingItemResponseDTOList))
                .andExpect(model().attribute("allTotalPrice", allTotalPrice));
    }

}