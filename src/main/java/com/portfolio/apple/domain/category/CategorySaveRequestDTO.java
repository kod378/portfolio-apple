package com.portfolio.apple.domain.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CategorySaveRequestDTO {

    @NotEmpty
    private String name;

    public CategorySaveRequestDTO(String name) {
        this.name = name;
    }
}
