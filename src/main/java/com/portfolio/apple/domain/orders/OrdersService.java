package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.delivery.Delivery;
import com.portfolio.apple.domain.delivery.DeliveryStatus;
import com.portfolio.apple.exception.EntityByIdNotFoundException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Transactional
    public Orders saveOrders(UserAccount userAccount, Delivery delivery, int payment) {
        Orders orders = Orders.builder()
                .serialNumber(getRandomOrderNumber())
                .userAccount(userAccount)
                .orderStatus(OrderStatus.ORDERED)
                .delivery(delivery)
                .payment(payment)
                .build();
        return ordersRepository.save(orders);
    }

    private String getRandomOrderNumber() {
        String date = LocalDate.now().toString().replaceAll("-", "");
        String generatedString = new RandomString(4).nextString();
        return date + generatedString;
    }

    public Orders findOrdersByIdAndAccount(Long ordersId, UserAccount userAccount) {
        return ordersRepository.findOrdersWithDeliveryByIdAndAccount(ordersId, userAccount).orElseThrow(() -> new EntityByIdNotFoundException("주문을 찾을 수 없습니다."));
    }

    public List<Orders> findOrdersWithDeliveryByUserAccount(UserAccount userAccount) {
        return ordersRepository.findOrdersWithDeliveryByUserAccount(userAccount);
    }

    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    public Orders findWithDeliveryById(Long ordersId) {
        return ordersRepository.findWithDeliveryById(ordersId).orElseThrow(() -> new EntityByIdNotFoundException("주문을 찾을 수 없습니다."));
    }

    @Transactional
    public void updateOrdersDeliverySerialNumberAndStatus(Orders orders, String deliverySerialNumber) {
        Delivery delivery = orders.getDelivery();
        delivery.updateDeliverySerialNumber(deliverySerialNumber);
        delivery.updateDeliveryStatus(DeliveryStatus.DELIVERING);
        orders.updateOrderStatus(OrderStatus.DELIVERY);
    }

    @Transactional
    public boolean cancelOrders(Orders orders) {
        if (orders.getDelivery().getDeliveryStatus() == DeliveryStatus.PREPARE) {
            Delivery delivery = orders.getDelivery();
            orders.updateOrderStatus(OrderStatus.CANCELED);
            delivery.updateDeliveryStatus(DeliveryStatus.CANCELED);
            return true;
        }
        return false;
    }

    @Transactional
    public void completeOrders(Orders orders) {
        Delivery delivery = orders.getDelivery();
    }

    public boolean changeDeliverySerialNumber(Long ordersId, String deliverySerialNumber) {
        boolean result = false;
        try {
            Orders orders = this.findWithDeliveryById(ordersId);
            if (orders.getOrderStatus() != OrderStatus.CANCELED) {
                this.updateOrdersDeliverySerialNumberAndStatus(orders, deliverySerialNumber);
                result = true;
            }
        } catch (EntityByIdNotFoundException e) {
//            예외 처리
        }
        return result;
    }
}
