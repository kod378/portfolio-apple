package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.item.ItemRepository;
import com.portfolio.apple.exception.item.NotEnoughStockException;
import com.portfolio.apple.exception.shoppingItem.ShoppingItemNotFoundException;
import com.portfolio.apple.mapper.ShoppingItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingItemMapper shoppingItemMapper;

    @Transactional
    public ShoppingItem addShoppingItem(UserAccount userAccount, Item item, int quantity) {
        Optional<ShoppingItem> byUserAccountAndItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item);
        if (byUserAccountAndItem.isPresent()) {
            byUserAccountAndItem.get().changeQuantity(quantity, item.getStockQuantity());
            return byUserAccountAndItem.get();
        } else {
            ShoppingItem shoppingItem = ShoppingItem.createShoppingItem(userAccount, item, quantity);
            return shoppingItemRepository.save(shoppingItem);
        }
    }

//    @Transactional
//    public void subtractShoppingItem(UserAccount userAccount, Item item, int quantity) throws NotEnoughStockException {
//        ShoppingItem shoppingItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item).get();
//        shoppingItem.removeQuantity(quantity);
//        shoppingItemRepository.save(shoppingItem);
//    }

    @Transactional
    public void deleteShoppingItem(UserAccount userAccount, Item item) {
        ShoppingItem shoppingItem = shoppingItemRepository.findByUserAccountAndItem(userAccount, item).get();
        userAccount.deleteShoppingItem(shoppingItem);
        shoppingItemRepository.delete(shoppingItem);
    }

    public List<ShoppingItemResponseDTO> findShoppingItems(UserAccount userAccount) {
        List<ShoppingItem> allByUserAccount = shoppingItemRepository.findAllByUserAccount(userAccount);
        return shoppingItemMapper.entityListToResponseDtoList(allByUserAccount);
    }

    public ShoppingItem findById(Long shoppingItemId) {
        return shoppingItemRepository.findById(shoppingItemId).orElseThrow(() -> new ShoppingItemNotFoundException("해당 장바구니가 없습니다. id=" + shoppingItemId));
    }

    @Transactional
    public void changeQuantity(ShoppingItem shoppingItem, int quantityToChange) {
        shoppingItem.changeQuantity(quantityToChange, shoppingItem.getItem().getStockQuantity());
    }
}
