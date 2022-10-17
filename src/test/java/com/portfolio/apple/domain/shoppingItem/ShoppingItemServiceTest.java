package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.ResetTextExecutionListener;
import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategorySaveRequestDTO;
import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemSaveRequestDTO;
import com.portfolio.apple.domain.item.ItemService;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestExecutionListeners(value = {ResetTextExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class ShoppingItemServiceTest {

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShoppingItemService shoppingItemService;
    @Autowired
    private ShoppingItemRepository shoppingItemRepository;
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() throws Exception {
        UserAccount user = UserAccount.builder()
                .email("test@test.com")
                .name("test")
                .role(Role.USER)
                .build();
        userAccountRepository.save(user);
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
    }


    @DisplayName("장바구니 상품 담기 - 정상 입력값")
    @Test
    public void addShoppingItem() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        int quantity = 1;

        //when
        shoppingItemService.addShoppingItem(user, item, quantity);

        //then
        ShoppingItem findShoppingItem = shoppingItemRepository.findByUserAccountAndItem(user, item).get();
        assertTrue(user.getShoppingItems().contains(findShoppingItem));
        assertEquals(findShoppingItem.getQuantity(), quantity);
        assertEquals(findShoppingItem.getItem(), item);
        assertEquals(findShoppingItem.getUserAccount(), user);
    }

    @DisplayName("장바구니 상품 수량 더하기 - 정상 입력값")
    @Test
    public void addQuantity() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        int quantity = 1;
        shoppingItemService.addShoppingItem(user, item, quantity);

        //when

    }

    @DisplayName("장바구니 상품 수량 빼기 - 정상 입력값")
    @Test
    void removeQuantity() throws Exception {
        //given


        //when

        //then
    }

    @DisplayName("장바구니 상품 삭제 - 정상 입력값")
    @Test
    void deleteShoppingItem() throws Exception {
        //given

        //when

        //then
    }
}