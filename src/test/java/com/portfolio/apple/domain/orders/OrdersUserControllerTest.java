package com.portfolio.apple.domain.orders;

import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithUserAccount;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.delivery.Delivery;
import com.portfolio.apple.domain.delivery.DeliveryService;
import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.orderedItem.OrderedItem;
import com.portfolio.apple.domain.orderedItem.OrderedItemRepository;
import com.portfolio.apple.domain.orderedItem.OrderedItemService;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomControllerTest
class OrdersUserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private ShoppingItemRepository shoppingItemRepository;
    @Autowired private OrdersRepository ordersRepository;
    @Autowired private OrderedItemRepository orderedItemRepository;
    @Autowired private CreateEntity createEntity;
    @Autowired private DeliveryService deliveryService;
    @Autowired private OrdersService ordersService;
    @Autowired private OrderedItemService orderedItemService;
    private Item sampleItem;
    private ShoppingItem sampleShoppingItem;


    @DisplayName("???????????? - ??????")
    @TestWithUserAccount
    void order() throws Exception {
        //given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        saveShoppingItems(userAccount);
        final int sampleStockQuantity = sampleItem.getStockQuantity();
        //when
        //then
        mockMvc.perform(post("/orders")
                .param("addressee", "?????????")
                .param("phoneNumber", "010-1234-1234")
                .param("address", "????????? ?????????")
                .param("detailAddress", "?????????")
                .param("postcode", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/complete"));

        assertTrue(sampleStockQuantity > sampleItem.getStockQuantity());
        assertTrue(userAccount.getShoppingItems().isEmpty());
        assertTrue(shoppingItemRepository.findAllByUserAccount(userAccount).isEmpty());
        Orders orders = ordersRepository.findAll().get(0);
        assertEquals(1, ordersRepository.findAll().size());
        assertEquals(OrderStatus.ORDERED, orders.getOrderStatus());
        assertEquals("?????????", orders.getDelivery().getAddress().getAddressee());
        assertEquals("010-1234-1234", orders.getDelivery().getAddress().getPhoneNumber());
        assertEquals("????????? ?????????", orders.getDelivery().getAddress().getAddress());
        assertEquals("?????????", orders.getDelivery().getAddress().getDetailAddress());
        assertEquals(12345, orders.getDelivery().getAddress().getPostcode());
        assertEquals(DeliveryStatus.PREPARE, orders.getDelivery().getDeliveryStatus());
        List<OrderedItem> orderedItems = orderedItemRepository.findAllByOrders(orders);
        assertEquals(2, orderedItems.size());
    }

    @DisplayName("???????????? ???????????? - ??????")
    @TestWithUserAccount
    void orderCheckedItem() throws Exception {
        //given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        saveShoppingItems(userAccount);
        final int sampleStockQuantity = sampleItem.getStockQuantity();
        //when
        //then
        mockMvc.perform(post("/orders")
                        .param("addressee", "?????????")
                        .param("phoneNumber", "010-1234-1234")
                        .param("address", "????????? ?????????")
                        .param("detailAddress", "?????????")
                        .param("postcode", "12345")
                        .param("checkShoppingItemIds", sampleShoppingItem.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/complete"));

        assertTrue(sampleStockQuantity > sampleItem.getStockQuantity());
        Orders orders = ordersRepository.findAll().get(0);
        List<OrderedItem> orderedItems = orderedItemRepository.findAllByOrders(orders);
        assertEquals(1, orderedItems.size());
    }

    private void saveShoppingItems(UserAccount userAccount) {
        Item sampleItem1 = createEntity.getSavedSampleItem("testItem1");
        Item sampleItem2 = createEntity.getSavedSampleItem("testItem2");
        ShoppingItem sampleShoppingItem1 = ShoppingItem.createShoppingItem(userAccount, sampleItem1, 2);
        ShoppingItem sampleShoppingItem2 = ShoppingItem.createShoppingItem(userAccount, sampleItem2, 3);
        shoppingItemRepository.save(sampleShoppingItem1);
        shoppingItemRepository.save(sampleShoppingItem2);
        sampleItem = sampleItem1;
        sampleShoppingItem = sampleShoppingItem1;
    }

    @DisplayName("???????????? ??? ???????????? - ??????")
    @TestWithUserAccount
    void order_fail_empty_shopping_item() throws Exception {
        //given        //when        //then
        mockMvc.perform(post("/orders")
                        .param("addressee", "?????????")
                        .param("phoneNumber", "010-1234-1234")
                        .param("address", "????????? ?????????")
                        .param("detailAddress", "?????????")
                        .param("postcode", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "??????????????? ?????? ????????? ????????????."))
                .andExpect(view().name("redirect:/"));
    }

    @DisplayName("???????????? ????????? - ??????")
    @TestWithUserAccount
    void order_detail() throws Exception {
        //given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        saveShoppingItems(userAccount);
        mockMvc.perform(post("/orders")
                        .param("addressee", "?????????")
                        .param("phoneNumber", "010-1234-1234")
                        .param("address", "????????? ?????????")
                        .param("detailAddress", "?????????")
                        .param("postcode", "12345"));

        //when
        Orders orders = ordersRepository.findAll().get(0);

        //then
        mockMvc.perform(get("/orders/" + orders.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/orders/detail"))
                .andExpect(model().attributeExists("ordersResponseDTO"));

    }

    @DisplayName("???????????? - ??????")
    @TestWithUserAccount
    void order_list() throws Exception {
        //given
        UserAccount userAccount = userAccountRepository.findByEmail("test@test.com").get();
        saveShoppingItems(userAccount);
        mockMvc.perform(post("/orders")
                .param("addressee", "?????????")
                .param("phoneNumber", "010-1234-1234")
                .param("address", "????????? ?????????")
                .param("detailAddress", "?????????")
                .param("postcode", "12345"));
        //when
        //then
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/orders/list"))
                .andExpect(model().attributeExists("ordersResponseDTOList"));
    }
}