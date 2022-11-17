package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.delivery.Delivery;
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

    public List<Orders> findOrdersByUserAccount(UserAccount userAccount) {
        return ordersRepository.findOrdersWithDeliveryByUserAccount(userAccount);
    }
}
