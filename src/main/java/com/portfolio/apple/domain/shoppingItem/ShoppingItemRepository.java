package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.account.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long>, ShoppingItemRepositoryCustom {
    Optional<ShoppingItem> findByUserAccountAndItem(UserAccount userAccount, Item item);

}
