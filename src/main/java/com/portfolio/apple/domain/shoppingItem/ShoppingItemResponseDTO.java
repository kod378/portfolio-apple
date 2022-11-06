package com.portfolio.apple.domain.shoppingItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingItemResponseDTO {
    private Long id;
    private String itemName;
    private int quantity;
    private int price;
    private int totalPrice;
    private String fileName;
    private int stockQuantity;
}
