package com.portfolio.apple;

import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.admin.AdminAccount;
import com.portfolio.apple.domain.account.admin.AdminAccountRepository;
import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.account.admin.AdminJoinFormDTO;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemRepository;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateEntity {


    @Autowired private final CategoryRepository categoryRepository;
    @Autowired private final ItemRepository itemRepository;
    @Autowired private final AdminAccountService adminAccountService;
    @Autowired private final AdminAccountRepository adminAccountRepository;
    @Autowired private final ShoppingItemRepository shoppingItemRepository;

    public Category saveCategory(String testCategory) {
        Category category = new Category(testCategory);
        categoryRepository.save(category);
        return category;
    }

    public Item saveItem(Category category, List<ItemFile> itemFile, String itemName) {
        Item item = Item.builder()
                .name(itemName)
                .price(1000)
                .stockQuantity(10)
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

    public Item getSavedSampleItem(String itemName) {
        Category category = saveCategory("testCategory");
        List<ItemFile> itemFileList = createItemFileList();
        return saveItem(category, itemFileList, itemName);
    }

    public void setUpAdminAccount() {
        AdminJoinFormDTO adminJoinFormDTO = new AdminJoinFormDTO("admin", "1234", "1234");
        adminAccountService.saveAdminAccount(adminJoinFormDTO);
    }

}
