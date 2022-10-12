package com.portfolio.apple.domain.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리 생성 - 정상 입력값")
    @Test
    public void saveCategory() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");

        //when
        categoryService.saveCategory(categorySaveFormDTO);

        //then
        Optional<Category> findByName = categoryRepository.findByName(categorySaveFormDTO.getName());
        assertTrue(findByName.isPresent());
        assertEquals(findByName.get().getName(), categorySaveFormDTO.getName());
    }

    @DisplayName("카테고리 생성 - 중복된 카테고리명")
    @Test
    public void saveCategory_DuplicateCategoryName() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");
        categoryService.saveCategory(categorySaveFormDTO);

        //when
        CategorySaveRequestDTO categorySaveFormDTO2 = new CategorySaveRequestDTO("categoryName");

        //then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> categoryService.saveCategory(categorySaveFormDTO2));
        assertEquals("이미 존재하는 카테고리입니다.", illegalArgumentException.getMessage());
    }

    @DisplayName("카테고리 수정 - 정상 입력값")
    @Test
    public void updateCategory() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");
        Category category = categoryService.saveCategory(categorySaveFormDTO);

        //when
        CategorySaveRequestDTO categorySaveFormDTO2 = new CategorySaveRequestDTO("categoryName2");
        categoryService.updateCategory(category.getId(), categorySaveFormDTO2);

        //then
        Optional<Category> findByName = categoryRepository.findByName(categorySaveFormDTO2.getName());
        assertTrue(findByName.isPresent());
        assertEquals(findByName.get().getName(), categorySaveFormDTO2.getName());
    }

    @DisplayName("카테고리 수정 - 해당 카테고리가 없습니다.")
    @Test
    public void updateCategory_NotExistCategory() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");
        Category category = categoryService.saveCategory(categorySaveFormDTO);
        Long notExistId = category.getId() + 1;

        //when
        CategorySaveRequestDTO categorySaveFormDTO2 = new CategorySaveRequestDTO("categoryName2");

        //then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(notExistId, categorySaveFormDTO2));
        assertEquals("해당 카테고리가 없습니다. id="+notExistId, illegalArgumentException.getMessage());
    }

    @DisplayName("카테고리 삭제 - 정상 입력값")
    @Test
    public void deleteCategory() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");
        Category category = categoryService.saveCategory(categorySaveFormDTO);
        Long categoryId = category.getId();

        //when
        categoryService.deleteCategory(category.getId());

        //then
        Optional<Category> findByName = categoryRepository.findByName(categorySaveFormDTO.getName());
        assertFalse(findByName.isPresent());
    }

    @DisplayName("카테고리 삭제 - 해당 카테고리가 없습니다.")
    @Test
    public void deleteCategory_NotExistCategory() throws Exception {
        //given
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO("categoryName");
        Category category = categoryService.saveCategory(categorySaveFormDTO);
        Long notExistId = category.getId() + 1L;

        //then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(notExistId));
        assertEquals("해당 카테고리가 없습니다. id="+notExistId, illegalArgumentException.getMessage());
    }
}