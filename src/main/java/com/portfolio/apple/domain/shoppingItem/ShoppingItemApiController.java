package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.account.user.CurrentUser;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShoppingItemApiController {

    private final ShoppingItemService shoppingItemService;
    private final ItemService itemService;

    @PostMapping("api/shoppingItem")
    public Long createShoppingItem(@RequestBody @Valid ShoppingItemRequestDTO shoppingItemRequestDTO, @CurrentUser UserAccount userAccount) {
        if (userAccount == null) {
            return -1L;
        }
        Item item = itemService.findById(shoppingItemRequestDTO.getItemId());
        ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(userAccount, item, shoppingItemRequestDTO.getQuantity());
        return shoppingItem.getId();
    }

    @PutMapping("api/shoppingItem/change/{shoppingItemId}/{quantityToChange}")
    public Map<String, Long> changeShoppingItem(@PathVariable Long shoppingItemId, @PathVariable int quantityToChange, @CurrentUser UserAccount userAccount) {
        if (userAccount == null) return null;
        ShoppingItem shoppingItem = shoppingItemService.findById(shoppingItemId);
        shoppingItemService.changeQuantity(shoppingItem, quantityToChange);
        return getResponseMap(shoppingItem);
    }

    @DeleteMapping("api/shoppingItem/{shoppingItemId}")
    public Long deleteShoppingItem(@PathVariable Long shoppingItemId, @CurrentUser UserAccount userAccount) {
        if (userAccount == null) return null;
        return shoppingItemService.deleteById(shoppingItemId, userAccount);
    }

    private Map<String, Long> getResponseMap(ShoppingItem shoppingItem) {
        return Map.of(
                "shoppingItemId", shoppingItem.getId(),
                "quantity", (long) shoppingItem.getQuantity(),
                "stockQuantity", (long) shoppingItem.getItem().getStockQuantity()
        );
    }
}
