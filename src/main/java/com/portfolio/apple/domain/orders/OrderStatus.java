package com.portfolio.apple.domain.orders;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("주문완료"), CANCELED("주문취소"), DELIVERY("배송중"), DELIVERY_COMPLETED("배송완료");

    final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
