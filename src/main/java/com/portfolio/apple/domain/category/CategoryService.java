package com.portfolio.apple.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category saveCategory(CategorySaveRequestDTO categoryFormDTO) throws Exception{
        Optional<Category> findCategory =  categoryRepository.findByName(categoryFormDTO.getName());
        if(findCategory.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        Category category = Category.createCategory(categoryFormDTO.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public Long updateCategory(Long id, CategorySaveRequestDTO categoryFormDTO) throws Exception{
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + id));
        category.updateCategoryName(categoryFormDTO.getName());
        return id;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Long deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + id));
        categoryRepository.delete(category);
        return id;
    }
}
