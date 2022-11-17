package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.account.user.QUserAccount;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.item.QItem;
import com.portfolio.apple.domain.itemFile.QItemFile;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShoppingItemRepositoryCustomImpl implements ShoppingItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QItem item = QItem.item;
    QItemFile itemFile = QItemFile.itemFile;
    QShoppingItem shoppingItem = QShoppingItem.shoppingItem;

    @Override
    public List<ShoppingItem> findAllByUserAccount(UserAccount userAccount) {
        List<ShoppingItem> shoppingItems = queryFactory.select(shoppingItem)
                .from(shoppingItem)
                .join(shoppingItem.item, item).fetchJoin()
                .join(item.itemFiles, itemFile).fetchJoin()
                .where(shoppingItem.userAccount.eq(userAccount)
                        , itemFile.orderNumber.eq(0L))  // 첫번째 대표이미지만 가져오기
                .fetch();

        return shoppingItems;
    }

    @Override
    public List<ShoppingItem> findAllByUserAccountAndIdIn(UserAccount userAccount, List<Long> checkedIdList) {
        List<ShoppingItem> shoppingItems = queryFactory.select(shoppingItem)
                .from(shoppingItem)
                .join(shoppingItem.item, item).fetchJoin()
                .join(item.itemFiles, itemFile).fetchJoin()
                .where(shoppingItem.userAccount.eq(userAccount)
                        , shoppingItemIdsIn(checkedIdList)
                        , itemFile.orderNumber.eq(0L))  // 첫번째 대표이미지만 가져오기
                .fetch();

        return shoppingItems;
    }

    private BooleanExpression shoppingItemIdsIn(List<Long> checkedIdList) {
        return checkedIdList != null ? shoppingItem.id.in(checkedIdList) : null;
    }
}
