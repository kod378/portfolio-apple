package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.orderedItem.OrderedItem;
import com.portfolio.apple.domain.orderedItem.OrderedItemResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderedItemMapper {

    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.itemFiles", target = "fileName")
    OrderedItemResponseDTO entityToResponseDTO(OrderedItem orderedItem);

    List<OrderedItemResponseDTO> entityToResponseDTO(List<OrderedItem> orderedItemList);

    default String getFileName(List<ItemFile> itemFiles) {
        return itemFiles.get(0).getFileName();
    }
}
