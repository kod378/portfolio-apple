package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingItemMapper {

    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.price", target = "price")
    @Mapping(source = "item.stockQuantity", target = "stockQuantity")
    @Mapping(source = "shoppingItem", target = "fileName")
    @Mapping(source = "shoppingItem", target = "totalPrice")
    public ShoppingItemResponseDTO entityToResponseDto(ShoppingItem shoppingItem);

    public List<ShoppingItemResponseDTO> entityListToResponseDtoList(List<ShoppingItem> shoppingItemList);

    default String getFileName(ShoppingItem shoppingItem) {
        return shoppingItem.getItem().getItemFiles().get(0).getFileName();
    }

    default int getTotalPrice(ShoppingItem shoppingItem) {
        return shoppingItem.getItem().getPrice() * shoppingItem.getQuantity();
    }
}
