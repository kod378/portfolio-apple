package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemSaveRequestDTO;
import com.portfolio.apple.domain.item.ItemResponseDTO;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CategoryReferenceMapper.class)
public interface ItemMapper {

    @Mapping(source = "categoryName", target = "category")
    @Mapping(target = "itemFiles", ignore = true)
    Item saveRequestDtoToEntity(ItemSaveRequestDTO itemRequestDTO);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "itemFiles", target = "itemFileDtoList")
    ItemResponseDTO entityToResponseDto(Item item);

    List<ItemResponseDTO> entityListToResponseDtoList(List<Item> itemList);

    ItemFileResponseDTO entityToResponseDto(ItemFile itemFile);

}
