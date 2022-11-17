package com.portfolio.apple.domain.delivery;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.orders.OrdersRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery savePrePareDelivery(OrdersRequestDTO ordersRequestDTO) {
        Delivery delivery = Delivery.builder()
                .address(new Address(ordersRequestDTO.getAddress(), ordersRequestDTO.getDetailAddress(), ordersRequestDTO.getPostcode(), ordersRequestDTO.getPhoneNumber(),ordersRequestDTO.getAddressee()))
                .deliveryStatus(DeliveryStatus.PREPARE)
                .build();
        return deliveryRepository.save(delivery);
    }
}
