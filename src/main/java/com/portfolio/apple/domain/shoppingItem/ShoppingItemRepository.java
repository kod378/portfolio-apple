package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.account.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long>, ShoppingItemRepositoryCustom {
    Optional<ShoppingItem> findByUserAccountAndItem(UserAccount userAccount, Item item);

    @Query("delete from ShoppingItem s where s.userAccount.id = :#{#userAccount.id} and s in :findShoppingItems")
    @Modifying
    void deleteByUserAccountAndIdIn(@Param("userAccount") UserAccount userAccount, @Param("findShoppingItems") List<ShoppingItem> findShoppingItems);

}
