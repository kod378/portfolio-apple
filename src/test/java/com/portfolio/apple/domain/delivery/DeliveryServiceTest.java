package com.portfolio.apple.domain.delivery;

import com.portfolio.apple.ResetTextExecutionListener;
import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.orders.OrdersRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestExecutionListeners(value = {ResetTextExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class DeliveryServiceTest {

    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @DisplayName("배송지 생성 - 정상 입력값")
    @Test
    public void createDelivery() throws Exception {
        //given
        OrdersRequestDTO ordersRequestDTO = new OrdersRequestDTO("testName", "010-0000-0000", "testAddress", "testDetailAddress", 12345, null);
        //when
        Delivery delivery = deliveryService.savePrePareDelivery(ordersRequestDTO);
        //then
        Delivery findDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow(() -> new IllegalArgumentException("배송지가 존재하지 않습니다."));
        assertEquals(findDelivery.getAddress().getAddressee(), ordersRequestDTO.getAddressee());
        assertEquals(findDelivery.getAddress().getPhoneNumber(), ordersRequestDTO.getPhoneNumber());
        assertEquals(findDelivery.getAddress().getAddress(), ordersRequestDTO.getAddress());
        assertEquals(findDelivery.getAddress().getDetailAddress(), ordersRequestDTO.getDetailAddress());
        assertEquals(findDelivery.getAddress().getPostcode(), ordersRequestDTO.getPostcode());
    }
}