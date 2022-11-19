package com.portfolio.apple;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.account.Role;
import com.portfolio.apple.domain.account.admin.AdminAccount;
import com.portfolio.apple.domain.account.admin.AdminAccountRepository;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.account.user.UserAccountRepository;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.category.CategoryRepository;
import com.portfolio.apple.domain.delivery.Delivery;
import com.portfolio.apple.domain.delivery.DeliveryRepository;
import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemRepository;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.orderedItem.OrderedItem;
import com.portfolio.apple.domain.orderedItem.OrderedItemRepository;
import com.portfolio.apple.domain.orders.OrderStatus;
import com.portfolio.apple.domain.orders.Orders;
import com.portfolio.apple.domain.orders.OrdersRepository;
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
    @Autowired private final AdminAccountRepository adminAccountRepository;
    @Autowired private final UserAccountRepository userAccountRepository;
    @Autowired private final DeliveryRepository deliveryRepository;
    @Autowired private final OrdersRepository ordersRepository;
    @Autowired private final OrderedItemRepository orderedItemRepository;

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
        AdminAccount adminAccount = AdminAccount.builder()
                .accountId("admin")
                .password("1234")
                .role(Role.ADMIN)
                .build();
        adminAccountRepository.save(adminAccount);
    }

    public UserAccount getSavedSampleUserAccount(String testEmail) {
        UserAccount userAccount = UserAccount.builder()
                .email(testEmail)
                .name("test")
                .role(Role.USER)
                .build();
        return userAccountRepository.save(userAccount);
    }

    public Orders getSavedSampleOrders() {
        UserAccount userAccount = getSavedSampleUserAccount("test@test.com");
        Item sampleItem1 = getSavedSampleItem("testItem1");
        Item sampleItem2 = getSavedSampleItem("testItem2");
        Delivery delivery = getSavedSampleDelivery();
        Orders orders = getSavedSampleOrders(userAccount, delivery);

        List<Item> itemList = List.of(sampleItem1, sampleItem2);
        getSavedOrderedItemList(orders, itemList);

        return orders;
    }

    public List<OrderedItem> getSavedOrderedItemList(Orders orders, List<Item> itemList) {
        List<OrderedItem> orderedItemList = new ArrayList<>();
        for (Item item : itemList) {
            OrderedItem orderedItem = OrderedItem.builder()
                    .item(item)
                    .orders(orders)
                    .price(item.getPrice())
                    .quantity(1)
                    .build();
            orderedItemList.add(orderedItem);
            orderedItem.getItem().removeStock(orderedItem.getQuantity());
        }
        return orderedItemRepository.saveAll(orderedItemList);
    }

    public Orders getSavedSampleOrders(UserAccount userAccount, Delivery delivery) {
        Orders orders = Orders.builder()
                .serialNumber("testSerialNumber")
                .userAccount(userAccount)
                .orderStatus(OrderStatus.ORDERED)
                .delivery(delivery)
                .payment(1000)
                .build();
        ordersRepository.save(orders);
        return orders;
    }

    public Delivery getSavedSampleDelivery() {
        Delivery delivery = Delivery.builder()
                .address(new Address("testAddress", "testDetailAddress",
                        00000, "000-0000-0000", "testName"))
                .deliveryStatus(DeliveryStatus.PREPARE)
                .build();
        deliveryRepository.save(delivery);
        return delivery;
    }
}
