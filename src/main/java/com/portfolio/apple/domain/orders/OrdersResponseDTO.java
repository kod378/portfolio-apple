package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.domain.orderedItem.OrderedItemResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersResponseDTO {

    private Long id;
    private String orderSerialNumber;
    private int payment;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private String deliverySerialNumber;
    private DeliveryStatus deliveryStatus;
    private Address address;
    private List<OrderedItemResponseDTO> orderedItemDTOList;

}
