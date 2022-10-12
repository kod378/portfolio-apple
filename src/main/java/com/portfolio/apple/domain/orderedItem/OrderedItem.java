package com.portfolio.apple.domain.orderedItem;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.domain.orders.Orders;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedItem extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int quantity;

    private int price;

}
