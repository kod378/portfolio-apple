package com.portfolio.apple.domain.item.mapper;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRequestDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-12T16:36:43+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.3.1 (Oracle Corporation)"
)
@Component
public class ItemMapperImpl implements ItemMapper {

    @Autowired
    private ItemReferenceMapper itemReferenceMapper;

    @Override
    public Item dtoToEntity(ItemRequestDTO itemRequestDTO) {
        if ( itemRequestDTO == null ) {
            return null;
        }

        Item.ItemBuilder item = Item.builder();

        try {
            item.category( itemReferenceMapper.categoryByName( itemRequestDTO.getCategoryName() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        item.stockQuantity( itemRequestDTO.getStockQuantity() );
        item.price( itemRequestDTO.getPrice() );
        item.active( itemRequestDTO.isActive() );
        item.name( itemRequestDTO.getName() );
        item.content( itemRequestDTO.getContent() );

        return item.build();
    }
}
