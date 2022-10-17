package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO entityToResponseDto(Category category);

    List<CategoryResponseDTO> entityListToResponseDtoList(List<Category> categoryList);
}
