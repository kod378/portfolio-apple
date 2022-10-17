package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.itemFile.ItemFileResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemResponseDTO {

        private Long id;
        private String name;
        private int price;
        private int stockQuantity;
        private String categoryName;
        private String content;
        private boolean active;

        private List<ItemFileResponseDTO> itemFileDtoList;


        public ItemResponseDTO(Long id, String name, int price, int stockQuantity,
                               String categoryName, String content, boolean active, List<ItemFileResponseDTO> itemFileDtoList) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.stockQuantity = stockQuantity;
                this.categoryName = categoryName;
                this.content = content;
                this.active = active;
                this.itemFileDtoList = itemFileDtoList;
        }
}
