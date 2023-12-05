package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.domain.delivery.DeliveryUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrdersApiController {

    private final OrdersService ordersService;

    @PutMapping("/admin/api/orders/{ordersId}/deliverySerialNumber")
    public ResponseEntity<String> updateDeliverySerialNumber(@PathVariable Long ordersId, @RequestBody DeliveryUpdateDTO deliveryUpdateDTO) {
        if (ordersService.changeDeliverySerialNumber(ordersId, deliveryUpdateDTO.getDeliverySerialNumber())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/admin/api/orders/cancel/{ordersId}")
    public ResponseEntity<String> cancelOrdersAdmin(@PathVariable Long ordersId) {
        Orders orders = ordersService.findWithDeliveryById(ordersId);
        if (orders.getDelivery().getDeliveryStatus() != DeliveryStatus.CANCELED) {
            ordersService.cancelOrders(orders);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/api/orders/cancel/{ordersId}")
    public ResponseEntity<String> cancelOrdersUser(@PathVariable Long ordersId) {
        Orders orders = ordersService.findWithDeliveryById(ordersId);
        if (ordersService.cancelOrders(orders)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/api/orders/complete/{ordersId}")
    public ResponseEntity<String> completeOrders(@PathVariable Long ordersId) {
        Orders orders = ordersService.findWithDeliveryById(ordersId);
        if (orders.getDelivery().getDeliveryStatus() == DeliveryStatus.ARRIVE ||
                orders.getDelivery().getDeliveryStatus() == DeliveryStatus.DELIVERING) {
            ordersService.completeOrders(orders);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
