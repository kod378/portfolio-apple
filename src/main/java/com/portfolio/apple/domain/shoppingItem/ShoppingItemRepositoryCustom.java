package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.account.user.UserAccount;

import java.util.List;

public interface ShoppingItemRepositoryCustom {

    List<ShoppingItem> findAllByUserAccount(UserAccount userAccount);
}
