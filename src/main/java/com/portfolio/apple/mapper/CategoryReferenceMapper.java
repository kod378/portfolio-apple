package com.portfolio.apple.mapper;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryReferenceMapper {

    private final CategoryRepository categoryRepository;

    public Category categoryByName(String categoryName) throws Exception{
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new Exception("해당 카테고리가 존재하지 않습니다."));
    }
}
