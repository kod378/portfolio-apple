package com.portfolio.apple.domain.category;

import lombok.Getter;

@Getter
public class CategorySaveResponseDTO {
    private Long id;
    private String name;

    public CategorySaveResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
