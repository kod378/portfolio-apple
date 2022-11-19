package com.portfolio.apple.domain.orders;

import com.portfolio.apple.mapper.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin/orders")
@RequiredArgsConstructor
public class OrdersAdminController {

    private final OrdersService ordersService;
    private final OrdersMapper ordersMapper;

    @GetMapping("/list")
    public String ordersList(Model model) {
        List<Orders> orders = ordersService.findAll();
        List<OrdersResponseDTO> ordersResponseDTOList = ordersMapper.entityToResponseDto(orders);

        model.addAttribute("ordersResponseDTOList", ordersResponseDTOList);
        return "admin/orders/list";
    }

    @GetMapping("/detail/{ordersId}")
    public String ordersDetail(@PathVariable Long ordersId, Model model) {
        Orders orders = ordersService.findWithDeliveryById(ordersId);
        OrdersResponseDTO ordersResponseDTO = ordersMapper.entityToResponseDto(orders);

        model.addAttribute("ordersResponseDTO", ordersResponseDTO);
        return "admin/orders/detail";
    }
}
