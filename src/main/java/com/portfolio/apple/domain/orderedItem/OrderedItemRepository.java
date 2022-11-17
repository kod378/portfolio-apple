package com.portfolio.apple.domain.orderedItem;

import com.portfolio.apple.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long>, OrderedItemRepositoryCustom {

}
