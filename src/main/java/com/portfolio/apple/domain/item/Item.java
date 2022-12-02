package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.exception.item.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemFile> itemFiles;

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

    public void applyItemFiles(List<ItemFile> itemFiles) {
        this.itemFiles = itemFiles;
        for (ItemFile itemFile : itemFiles) {
            itemFile.setItem(this);
        }
    }
}
