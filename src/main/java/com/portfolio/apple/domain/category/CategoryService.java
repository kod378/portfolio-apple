package com.portfolio.apple.domain.category;

import com.portfolio.apple.exception.category.CategoryDuplicateException;
import com.portfolio.apple.exception.category.CategoryNotFoundException;
import com.portfolio.apple.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    @Transactional
    public Category saveCategory(CategorySaveRequestDTO categoryFormDTO) throws Exception{
        Optional<Category> findCategory =  categoryRepository.findByName(categoryFormDTO.getName());
        if(findCategory.isPresent()) {
            throw new CategoryDuplicateException("이미 존재하는 카테고리입니다.");
        }
        Category category = Category.createCategory(categoryFormDTO.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public Long updateCategory(Long id, CategorySaveRequestDTO categoryFormDTO) throws Exception{
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("해당 카테고리가 없습니다. id=" + id));
        category.updateCategoryName(categoryFormDTO.getName());
        return id;
    }

    public List<CategoryResponseDTO> findAllDto() {
        List<Category> all = categoryRepository.findAll();
        return categoryMapper.entityListToResponseDtoList(all);
    }

    @Transactional
    public Long deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("해당 카테고리가 없습니다. id=" + id));
        categoryRepository.delete(category);
        return id;
    }
}
