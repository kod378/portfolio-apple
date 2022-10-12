package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.exception.NotEnoughStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;

    @Transactional
    public void addShoppingItem(UserAccount userAccount, Item item, int quantity) {
        Optional<ShoppingItem> byUserAccountAndItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item);
        if (byUserAccountAndItem.isPresent()) {
            byUserAccountAndItem.get().addQuantity(quantity, item.getStockQuantity());
        } else {
            ShoppingItem shoppingItem = ShoppingItem.createShoppingItem(userAccount, item, quantity);
            shoppingItemRepository.save(shoppingItem);
        }
    }

    @Transactional
    public void removeShoppingItem(UserAccount userAccount, Item item, int quantity) throws NotEnoughStockException {
        ShoppingItem shoppingItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item).get();
        shoppingItem.removeQuantity(quantity);
        shoppingItemRepository.save(shoppingItem);
    }

    @Transactional
    public void deleteShoppingItem(UserAccount userAccount, Item item) {
        ShoppingItem shoppingItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item).get();
        userAccount.deleteShoppingItem(shoppingItem);
        shoppingItemRepository.delete(shoppingItem);
    }
}
