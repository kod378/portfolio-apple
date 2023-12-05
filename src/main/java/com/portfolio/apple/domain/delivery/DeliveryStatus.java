package com.portfolio.apple.domain.delivery;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    PREPARE, DELIVERING, ARRIVE, CANCELED, COMPLETED
}
