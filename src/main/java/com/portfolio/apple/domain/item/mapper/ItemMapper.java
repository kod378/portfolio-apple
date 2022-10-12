package com.portfolio.apple.domain.item.mapper;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRepository;
import com.portfolio.apple.domain.item.ItemRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ItemReferenceMapper.class)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryName", target = "category")
    Item dtoToEntity(ItemRequestDTO itemRequestDTO);
}
