package com.portfolio.apple;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateEntity {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public CreateEntity(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }


    public Category saveCategory(String testCategory) {
        Category category = new Category(testCategory);
        categoryRepository.save(category);
        return category;
    }

    public Item saveItem(Category category, List<ItemFile> itemFile, String itemName) {
        Item item = Item.builder()
                .name(itemName)
                .price(1000)
                .stockQuantity(100)
                .content("testContent")
                .category(category)
                .build();
        item.applyItemFiles(itemFile);
        return itemRepository.save(item);
    }

    public List<ItemFile> createItemFileList() {
        ItemFile itemFile = ItemFile.builder()
                .fileName("test.jpg")
                .fileFullPath("E:\\Project\\apple\\src\\main\\resources\\static\\_freeApple.jpg")
                .size(1L)
                .fileType("image/jpg")
                .originalFileName("_freeApple.jpg")
                .orderNumber(0L)
                .build();
        List<ItemFile> itemFileList = new ArrayList<>();
        itemFileList.add(itemFile);
        return itemFileList;
    }

    public Item getSavedSampleItem() {
        Category category = saveCategory("testCategory");
        List<ItemFile> itemFileList = createItemFileList();
        return saveItem(category, itemFileList, "testItem");
    }
}
