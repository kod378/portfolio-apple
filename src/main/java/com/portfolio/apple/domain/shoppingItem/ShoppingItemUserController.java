package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.account.user.CurrentUser;
import com.portfolio.apple.domain.account.user.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShoppingItemUserController {

    private final ShoppingItemService shoppingItemService;

    @GetMapping("/shoppingItem")
    public String shoppingItem(Model model, @CurrentUser UserAccount userAccount) {
        List<ShoppingItemResponseDTO> shoppingItemDTOList = shoppingItemService.findShoppingItems(userAccount);
        model.addAttribute("shoppingItemResponseDTOList", shoppingItemDTOList);

        int allTotalPrice = shoppingItemDTOList.stream().mapToInt(ShoppingItemResponseDTO::getTotalPrice).sum();
        model.addAttribute("allTotalPrice", allTotalPrice);
        model.addAttribute("deliveryFee", getDeliveryFee(allTotalPrice));
        return "user/ShoppingItem/list";
    }

    private static int getDeliveryFee(int allTotalPrice) {
        //TODO: 추후 배송비 관련 로직이 추가될 수도 있음. 일단은 2500로 고정
        int deliveryFee = 0;
        if (allTotalPrice > 0) {
            deliveryFee = 2500;
        }
        return deliveryFee;
    }
}
