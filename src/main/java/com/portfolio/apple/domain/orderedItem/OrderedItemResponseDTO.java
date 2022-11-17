package com.portfolio.apple.domain.orderedItem;

import lombok.Data;

@Data
public class OrderedItemResponseDTO {

    private int quantity;
    private int price;
    private String itemName;
    private String fileName;
}
