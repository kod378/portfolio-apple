package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.account.user.CurrentUser;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.delivery.DeliveryFee;
import com.portfolio.apple.mapper.ShoppingItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ShoppingItemUserController {

    private final ShoppingItemService shoppingItemService;
    private final ShoppingItemMapper shoppingItemMapper;

    @GetMapping("/shoppingItem")
    public String shoppingItem(Model model, @CurrentUser UserAccount userAccount) {
        final List<ShoppingItem> listByUserAccount = shoppingItemService.findListByUserAccount(userAccount);
        final List<ShoppingItemResponseDTO> shoppingItemDTOList = shoppingItemMapper.entityListToResponseDtoList(listByUserAccount);
        final Map<Boolean, List<ShoppingItemResponseDTO>> sellableMap = shoppingItemDTOList.stream().collect(Collectors.partitioningBy(dto -> dto.getStockQuantity() > 0));
        List<ShoppingItemResponseDTO> sellableShoppingItemDTOList = sellableMap.get(true);
        final int allTotalPrice = getSumTotalPrice(sellableShoppingItemDTOList);

        model.addAttribute("shoppingItemResponseDTOList", sellableShoppingItemDTOList);
        model.addAttribute("soldOutList", sellableMap.get(false));
        model.addAttribute("allTotalPrice", allTotalPrice);
        model.addAttribute("deliveryFee", DeliveryFee.DELIVERY_FEE);
        return "user/ShoppingItem/list";
    }

    @GetMapping("/shoppingItem/orderConfirm")
    public String shoppingItemBeforeOrder(@RequestParam(value = "checkShoppingItems", required = false) List<Long> checkedIdList, @CurrentUser UserAccount userAccount, Model model) {
        final List<ShoppingItem> listByUserAccountAndIdList = shoppingItemService.findAllByUserAccountAndIdIn(userAccount, checkedIdList);
        List<ShoppingItemResponseDTO> checkedShoppingItemDTOList = shoppingItemMapper.entityListToResponseDtoList(listByUserAccountAndIdList);
        final int allTotalPrice = getSumTotalPrice(checkedShoppingItemDTOList);
        model.addAttribute("shoppingItemResponseDTOList", checkedShoppingItemDTOList);
        model.addAttribute("allTotalPrice", allTotalPrice);
        model.addAttribute("deliveryFee", DeliveryFee.DELIVERY_FEE);
        return "user/ShoppingItem/order";
    }

    private static int getSumTotalPrice(List<ShoppingItemResponseDTO> shoppingItemDTOList) {
        return shoppingItemDTOList.stream().mapToInt(ShoppingItemResponseDTO::getTotalPrice).sum();
    }
}
