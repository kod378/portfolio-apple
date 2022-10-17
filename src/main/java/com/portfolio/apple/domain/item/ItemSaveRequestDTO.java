package com.portfolio.apple.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ItemSaveRequestDTO {

    private Long id;

    @Min(0)
    private int stockQuantity;

    @Min(1)
    private int price;

    @NotEmpty
    private String name;

    private boolean active;

    private String content;

    private String categoryName;

    public ItemSaveRequestDTO(int stockQuantity, int price, String name, boolean active, String content, String categoryName) {
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.name = name;
        this.active = active;
        this.content = content;
        this.categoryName = categoryName;
    }
}
