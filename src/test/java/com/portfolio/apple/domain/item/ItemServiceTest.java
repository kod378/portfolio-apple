package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategorySaveRequestDTO;
import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.exception.item.ItemNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        ItemSaveRequestDTO itemFormDTO = new ItemSaveRequestDTO(10, 1000, "itemName", true, "content", categoryName);
        List<ItemFile> itemFiles = new ArrayList<>(
                List.of(ItemFile.builder()
                        .fileName("test").fileFullPath("test").fileType("test").orderNumber(1L).originalFileName("test").size(1L)
                        .build())
        );
        //when
        itemService.saveItem(itemFormDTO, itemFiles);

        //then
        Item itemByName = itemService.findItemByName(itemFormDTO.getName());
        assertEquals(itemByName.getName(), itemFormDTO.getName());
        assertEquals(itemByName.getPrice(), itemFormDTO.getPrice());
        assertEquals(itemByName.getStockQuantity(), itemFormDTO.getStockQuantity());
        assertEquals(itemByName.getContent(), itemFormDTO.getContent());
        assertEquals(itemByName.getCategory().getName(), itemFormDTO.getCategoryName());
        assertTrue(itemByName.isActive());
    }

    @DisplayName("상품 생성 - 정상 입력값 active False")
    @Test
    public void createItem2() throws Exception {
        //given
        String categoryName = "testCategory";
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO(categoryName);
        Category category2 = categoryService.saveCategory(categorySaveFormDTO);
        ItemSaveRequestDTO itemFormDTO2 = new ItemSaveRequestDTO(10, 1000, "itemName", false, "content", categoryName);
        List<ItemFile> itemFiles = new ArrayList<>(
                List.of(ItemFile.builder()
                        .fileName("test").fileFullPath("test").fileType("test").orderNumber(1L).originalFileName("test").size(1L)
                        .build())
        );

        //when
        itemService.saveItem(itemFormDTO2, itemFiles);

        //then
        Item itemByName = itemService.findItemByName(itemFormDTO2.getName());
        assertEquals(itemByName.getName(), itemFormDTO2.getName());
        assertEquals(itemByName.getPrice(), itemFormDTO2.getPrice());
        assertEquals(itemByName.getStockQuantity(), itemFormDTO2.getStockQuantity());
        assertEquals(itemByName.getContent(), itemFormDTO2.getContent());
        assertEquals(itemByName.getCategory().getName(), itemFormDTO2.getCategoryName());
        assertFalse(itemByName.isActive());
    }

    @DisplayName("상품 삭제 - 정상")
    @Test
    public void deleteItem() throws Exception {
        //given
        String categoryName = "testCategory";
        CategorySaveRequestDTO categorySaveFormDTO = new CategorySaveRequestDTO(categoryName);
        Category category = categoryService.saveCategory(categorySaveFormDTO);
        ItemSaveRequestDTO itemFormDTO = new ItemSaveRequestDTO(10, 1000, "itemName", true, "content", categoryName);
        List<ItemFile> itemFiles = new ArrayList<>(
                List.of(ItemFile.builder()
                        .fileName("test").fileFullPath("test").fileType("test").orderNumber(1L).originalFileName("test").size(1L)
                        .build())
        );
        itemService.saveItem(itemFormDTO, itemFiles);
        Item itemByName = itemService.findItemByName(itemFormDTO.getName());

        //when
        itemService.deleteItem(itemByName.getId());

        //then
        assertThrows(ItemNotFoundException.class, () -> itemService.findItemByName(itemFormDTO.getName()));
    }
}