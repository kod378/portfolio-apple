package com.portfolio.apple.domain.delivery;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

}
