package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.account.user.CurrentUser;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.orderedItem.OrderedItem;
import com.portfolio.apple.domain.orderedItem.OrderedItemService;
import com.portfolio.apple.exception.orders.NoShoppingItemException;
import com.portfolio.apple.mapper.OrderedItemMapper;
import com.portfolio.apple.mapper.OrdersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrdersUserController {

    private final OrdersService ordersService;
    private final OrderedItemService orderedItemService;
    private final OrderService orderService;
    private final OrdersMapper ordersMapper;
    private final OrderedItemMapper orderedItemMapper;

    @GetMapping("/orders")
    public String orders(@CurrentUser UserAccount userAccount, Model model) {
        List<Orders> orders = ordersService.findOrdersByUserAccount(userAccount);
        if (orders.isEmpty()) {
            model.addAttribute("ordersResponseDTOList", Collections.emptyList());
            return "user/orders/list";
        }
        List<OrderedItem> orderedItemsByOrders = orderedItemService.findAllByOrdersIn(orders);
        Map<Orders, List<OrderedItem>> ordersListMap = orderedItemsByOrders.stream()
                .collect(groupingBy(OrderedItem::getOrders));

        List<OrdersResponseDTO> ordersResponseDTOList = new ArrayList<>();
        for (Orders order : orders) {
            OrdersResponseDTO ordersResponseDTO = ordersMapper.entityToResponseDto(order);
            ordersResponseDTO.setOrderedItemDTOList(orderedItemMapper.entityToResponseDTO(ordersListMap.get(order)));
            ordersResponseDTOList.add(ordersResponseDTO);
        }

        model.addAttribute("ordersResponseDTOList", ordersResponseDTOList);
        return "user/orders/list";
    }

    @PostMapping("/orders")
    public String orders(@CurrentUser UserAccount userAccount, OrdersRequestDTO ordersRequestDTO, RedirectAttributes redirectAttributes) {
        try {
            Orders orders = orderService.order(userAccount, ordersRequestDTO);
            redirectAttributes.addAttribute("ordersId", orders.getId());
            return "redirect:/orders/complete";
        } catch (NoShoppingItemException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            log.error("주문 오류", e);
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/orders/complete")
    public String ordersCompleted(@RequestParam("ordersId") Long ordersId, Model model) {
        model.addAttribute("ordersId", ordersId);
        return "user/orders/complete";
    }

    @GetMapping("/orders/{ordersId}")
    public String ordersDetail(@CurrentUser UserAccount userAccount, @PathVariable("ordersId") Long ordersId, Model model) {
        Orders orders = ordersService.findOrdersByIdAndAccount(ordersId, userAccount);
        List<OrderedItem> allByOrders = orderedItemService.findAllByOrders(orders);

        OrdersResponseDTO ordersResponseDTO = ordersMapper.entityToResponseDto(orders);
        ordersResponseDTO.setOrderedItemDTOList(orderedItemMapper.entityToResponseDTO(allByOrders));
        model.addAttribute("ordersResponseDTO", ordersResponseDTO);
        return "user/orders/detail";
    }
}
