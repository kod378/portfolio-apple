package com.portfolio.apple.domain.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.apple.CreateEntity;
import com.portfolio.apple.CustomControllerTest;
import com.portfolio.apple.TestWithAdminAccount;
import com.portfolio.apple.TestWithUserAccount;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.delivery.DeliveryService;
import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.domain.delivery.DeliveryUpdateDTO;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.orderedItem.OrderedItemRepository;
import com.portfolio.apple.domain.orderedItem.OrderedItemService;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomControllerTest
class OrdersApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private ShoppingItemRepository shoppingItemRepository;
    @Autowired private OrdersRepository ordersRepository;
    @Autowired private OrderedItemRepository orderedItemRepository;
    @Autowired private CreateEntity createEntity;
    @Autowired private ObjectMapper objectMapper;



    @BeforeEach
    void setUp() {
        createEntity.setUpAdminAccount();
    }

    @DisplayName("배송번호 입력 - 정상")
    @TestWithAdminAccount
    void deliveryNumber() throws Exception {
        //given
        Orders sampleOrders = createEntity.getSavedSampleOrders();
        //when
        DeliveryUpdateDTO deliveryUpdateDTO = new DeliveryUpdateDTO();
        deliveryUpdateDTO.setDeliverySerialNumber("123456789");
        //then
        mockMvc.perform(put("/admin/api/orders/" + sampleOrders.getId() + "/deliverySerialNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deliveryUpdateDTO)))
                .andExpect(status().isOk());
    }

    @DisplayName("주문 취소[관리자] - 정상")
    @TestWithAdminAccount
    void cancelOrdersAdmin() throws Exception {
        //given
        Orders sampleOrders = createEntity.getSavedSampleOrders();
        //when
        //then
        mockMvc.perform(put("/admin/api/orders/cancel/" + sampleOrders.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("주문 취소[사용자] - 정상")
    @TestWithUserAccount
    void cancelOrdersUser() throws Exception {
        //given
        Orders sampleOrders = createEntity.getSavedSampleOrders();
        //when
        //then
        mockMvc.perform(put("/api/orders/cancel/" + sampleOrders.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("주문 취소[사용자] - 실패")
    @TestWithUserAccount
    void cancelOrdersUserFail() throws Exception {
        //given
        UserAccount userAccount = createEntity.getSavedSampleUserAccount("test@test.com");
        Orders sampleOrders = createEntity.getSavedSampleOrders();
        //when
        sampleOrders.getDelivery().updateDeliveryStatus(DeliveryStatus.DELIVERING);
        //then
        mockMvc.perform(put("/api/orders/cancel/" + sampleOrders.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}