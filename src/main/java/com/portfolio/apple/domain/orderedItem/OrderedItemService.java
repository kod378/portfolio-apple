package com.portfolio.apple.domain.orderedItem;

import com.portfolio.apple.domain.orders.Orders;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderedItemService {

    private final OrderedItemRepository orderedItemRepository;

    @Transactional
    public List<OrderedItem> saveAllAndChangeStockOfItem(List<ShoppingItem> shoppingItemList, Orders orders) {
        List<OrderedItem> orderedItemList = new ArrayList<>();
        for (ShoppingItem shoppingItem : shoppingItemList) {
            OrderedItem orderedItem = OrderedItem.builder()
                    .orders(orders)
                    .item(shoppingItem.getItem())
                    .quantity(shoppingItem.getQuantity())
                    .price(shoppingItem.getItem().getPrice())
                    .build();
            orderedItemList.add(orderedItem);
            orderedItem.getItem().removeStock(shoppingItem.getQuantity());
        }
        return orderedItemRepository.saveAll(orderedItemList);
    }

    public List<OrderedItem> findAllByOrders(Orders orders) {
        return orderedItemRepository.findAllByOrders(orders);
    }

    public List<OrderedItem> findAllByOrdersIn(List<Orders> orders) {
        return orderedItemRepository.findAllByOrdersIn(orders);
    }
}
