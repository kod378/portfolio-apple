package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.delivery.Delivery;
import com.portfolio.apple.domain.delivery.DeliveryFee;
import com.portfolio.apple.domain.delivery.DeliveryService;
import com.portfolio.apple.domain.orderedItem.OrderedItemService;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.shoppingItem.ShoppingItemService;
import com.portfolio.apple.exception.orders.NoShoppingItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersService ordersService;
    private final ShoppingItemService shoppingItemService;
    private final OrderedItemService orderedItemService;
    private final DeliveryService deliveryService;

    @Transactional
    public Orders order(UserAccount userAccount, OrdersRequestDTO ordersRequestDTO) throws Exception{
        List<ShoppingItem> findShoppingItems = shoppingItemService.findAllByUserAccountAndIdIn(userAccount, ordersRequestDTO.getCheckShoppingItemIds());
        if (findShoppingItems.isEmpty()) {
            throw new NoShoppingItemException("장바구니에 담긴 상품이 없습니다.");
        }

        checkStock(findShoppingItems);
        final int payment = findShoppingItems.stream().mapToInt(shoppingItem -> shoppingItem.getItem().getPrice() * shoppingItem.getQuantity()).sum()
                + DeliveryFee.DELIVERY_FEE;
        Address address = getAddressFromDTO(ordersRequestDTO);
        Delivery delivery = deliveryService.savePrePareDelivery(address);
        Orders orders = ordersService.saveOrders(userAccount, delivery, payment);
        checkStock(findShoppingItems);
        orderedItemService.saveAllAndChangeStockOfItem(findShoppingItems, orders);
        shoppingItemService.deleteByUserAccountAndIdIn(userAccount, findShoppingItems);
        return orders;
    }

    private Address getAddressFromDTO(OrdersRequestDTO ordersRequestDTO) {
        return new Address(ordersRequestDTO.getAddress(), ordersRequestDTO.getDetailAddress(), ordersRequestDTO.getPostcode(), ordersRequestDTO.getPhoneNumber(), ordersRequestDTO.getAddressee());
    }

    private void checkStock(List<ShoppingItem> allByUserAccount) {
        for (ShoppingItem shoppingItem : allByUserAccount) {
            if (shoppingItem.getItem().getStockQuantity() < shoppingItem.getQuantity()) {
                throw new IllegalStateException("주문 수량이 재고 수량을 초과하였습니다.");
            }
        }
    }
}
