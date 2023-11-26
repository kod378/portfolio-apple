package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.ResetTestExecutionListener;
import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.category.CategorySaveRequestDTO;
import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemSaveRequestDTO;
import com.portfolio.apple.domain.item.ItemService;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.exception.item.NotEnoughStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestExecutionListeners(value = {ResetTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
        categoryService.saveCategory(categorySaveFormDTO);
        ItemSaveRequestDTO itemFormDTO = new ItemSaveRequestDTO(10, 1000, "itemName", true, "content", categoryName);
        List<ItemFile> itemFiles = new ArrayList<>(
                List.of(ItemFile.builder()
                        .fileName("test").fileFullPath("test").fileType("test").orderNumber(1L).originalFileName("test").size(1L)
                        .build()
                        ,ItemFile.builder()
                                .fileName("test0").fileFullPath("test0").fileType("test0").orderNumber(0L).originalFileName("test").size(1L)
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

    @DisplayName("장바구니 상품 수량 변경 - 정상 입력값")
    @Test
    public void changeQuantity() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        final int quantity = 1;
        ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(user, item, quantity);

        //when
        final int changeQuantity = 5;
        shoppingItemService.changeQuantity(shoppingItem, changeQuantity);

        //then
        ShoppingItem findShoppingItem = shoppingItemRepository.findByUserAccountAndItem(user, item).get();
        assertEquals(findShoppingItem.getQuantity(), changeQuantity);
        assertEquals(user.getShoppingItems().get(0).getQuantity(), changeQuantity);
    }

    @DisplayName("장바구니 상품 수량 변경 - 예외 최소 1보다 커야함")
    @Test
    void changeQuantityException1() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        final int quantity = 5;
        ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(user, item, quantity);

        //when
        final int changeQuantity = -2;

        //then
        assertThrows(NotEnoughStockException.class, () -> shoppingItemService.changeQuantity(shoppingItem, changeQuantity),
                "ShoppingItem quantity must be more than 1");
    }

    //TODO: 장바구니 상품 수량 변경 - 예외 입력값
    @DisplayName("장바구니 상품 수량 변경 - 예외 재고 초과")
    @Test
    void changeQuantityException2() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        final int quantity = 5;
        ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(user, item, quantity);

        //when
        final int changeQuantity = 20;

        //then
        assertThrows(NotEnoughStockException.class, () -> shoppingItemService.changeQuantity(shoppingItem, changeQuantity),
                "Stock is not enough");
    }

    @DisplayName("장바구니 상품 개별 삭제 - 정상 입력값")
    @Test
    void deleteShoppingItem() throws Exception {
        //given
        UserAccount user = userAccountRepository.findByEmail("test@test.com").get();
        Item item = itemService.findItemByName("itemName");
        final int quantity = 5;
        ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(user, item, quantity);

        //when
        shoppingItemService.deleteById(shoppingItem.getId(), user);

        //then
        assertFalse(user.getShoppingItems().contains(shoppingItem));
        assertFalse(shoppingItemRepository.findById(shoppingItem.getId()).isPresent());
    }
}