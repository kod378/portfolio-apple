package com.portfolio.apple.domain.shoppingItem;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ShoppingItemRequestDTO {

    @NotNull
    private Long itemId;
    @Min(1)
    private int quantity;

}
