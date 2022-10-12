package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private int stockQuantity;

    private int price;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Lob
    private String content;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
