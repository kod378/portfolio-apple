package com.portfolio.apple.domain.orderedItem;

import com.portfolio.apple.domain.orders.Orders;

import java.util.List;

public interface OrderedItemRepositoryCustom {
    List<OrderedItem> findAllByOrders(Orders orders);

    List<OrderedItem> findAllByOrdersIn(List<Orders> orders);
}
