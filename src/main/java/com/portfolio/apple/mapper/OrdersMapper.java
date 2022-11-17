package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.orders.Orders;
import com.portfolio.apple.domain.orders.OrdersResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    @Mapping(source = "delivery.deliveryStatus", target = "deliveryStatus")
    @Mapping(source = "delivery.serialNumber", target = "deliverySerialNumber")
    @Mapping(source = "delivery.address", target = "address")
    @Mapping(source = "serialNumber", target = "orderSerialNumber")
    @Mapping(source = "createdDateTime", target = "orderDateTime")
    @Mapping(target = "orderedItemDTOList", ignore = true)
    OrdersResponseDTO entityToResponseDto(Orders orders);

    List<OrdersResponseDTO> entityToResponseDto(List<Orders> orders);
}
