package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.delivery.Delivery;
import com.portfolio.apple.domain.account.user.UserAccount;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String SerialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @OneToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

}
