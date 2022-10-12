package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategorySaveRequestDTO;
import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.category.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @DisplayName("상품 생성 - 정상 입력값 active True")
    @Test
    public void createItem() throws Exception {
        //given
        String categoryName = "testCategory";
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO(categoryName);
        Category category = categoryService.saveCategory(categorySaveFormDTO);
        ItemRequestDTO itemFormDTO = new ItemRequestDTO(10, 1000, "itemName", true, "content", categoryName);

        //when
        itemService.saveItem(itemFormDTO);

        //then
        Optional<Item> findItem = itemService.findByName(itemFormDTO.getName());
        assertTrue(findItem.isPresent());
        assertEquals(findItem.get().getName(), itemFormDTO.getName());
        assertEquals(findItem.get().getPrice(), itemFormDTO.getPrice());
        assertEquals(findItem.get().getStockQuantity(), itemFormDTO.getStockQuantity());
        assertEquals(findItem.get().getCategory().getName(), category.getName());
        assertEquals(findItem.get().isActive(), true);
    }

    @DisplayName("상품 생성 - 정상 입력값 active False")
    @Test
    public void createItem2() throws Exception {
        //given
        String categoryName = "testCategory";
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO(categoryName);
        Category category2 = categoryService.saveCategory(categorySaveFormDTO);
        ItemRequestDTO itemFormDTO2 = new ItemRequestDTO(10, 1000, "itemName", false, "content", categoryName);

        //when
        itemService.saveItem(itemFormDTO2);

        //then
        Optional<Item> findItem2 = itemService.findByName(itemFormDTO2.getName());
        assertTrue(findItem2.isPresent());
        assertEquals(findItem2.get().getName(), itemFormDTO2.getName());
        assertEquals(findItem2.get().getPrice(), itemFormDTO2.getPrice());
        assertEquals(findItem2.get().getStockQuantity(), itemFormDTO2.getStockQuantity());
        assertEquals(findItem2.get().getCategory().getName(), category2.getName());
        assertEquals(findItem2.get().isActive(), false);
    }
}