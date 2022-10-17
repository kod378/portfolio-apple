package com.portfolio.apple.domain.category;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
