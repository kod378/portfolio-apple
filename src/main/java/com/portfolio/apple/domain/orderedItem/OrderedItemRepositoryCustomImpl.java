package com.portfolio.apple.domain.orderedItem;

import com.portfolio.apple.domain.item.QItem;
import com.portfolio.apple.domain.itemFile.QItemFile;
import com.portfolio.apple.domain.orders.Orders;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.portfolio.apple.domain.item.QItem.item;
import static com.portfolio.apple.domain.itemFile.QItemFile.itemFile;
import static com.portfolio.apple.domain.orderedItem.QOrderedItem.orderedItem;

@RequiredArgsConstructor
public class OrderedItemRepositoryCustomImpl implements OrderedItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderedItem> findAllByOrders(Orders orders) {
        return queryFactory
                .selectFrom(orderedItem)
                .join(orderedItem.item, item).fetchJoin()
                .join(item.itemFiles, itemFile).fetchJoin()
                .where(orderedItem.orders.eq(orders)
                    , itemFile.orderNumber.eq(0L))
                .fetch();
    }

    @Override
    public List<OrderedItem> findAllByOrdersIn(List<Orders> orders) {
        return queryFactory
                .selectFrom(orderedItem)
                .join(orderedItem.item, item).fetchJoin()
                .join(item.itemFiles, itemFile).fetchJoin()
                .where(orderedItem.orders.in(orders)
                    , itemFile.orderNumber.eq(0L))
                .fetch();
    }
}
