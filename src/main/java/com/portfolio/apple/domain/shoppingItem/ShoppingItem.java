package com.portfolio.apple.domain.shoppingItem;

import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.domain.item.Item;
import com.portfolio.apple.exception.item.NotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ShoppingItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int quantity;

    public ShoppingItem(UserAccount userAccount, Item item, int quantity) {
        this.userAccount = userAccount;
        this.item = item;
        this.quantity = quantity;
    }

    public static ShoppingItem createShoppingItem(UserAccount userAccount, Item item, int quantity) {
        ShoppingItem shoppingItem = new ShoppingItem(userAccount, item, quantity);
        userAccount.putShoppingItem(shoppingItem);
        return shoppingItem;
    }

//    public void addQuantity(int quantity, int stockQuantity) {
//        if (this.quantity + quantity > stockQuantity) {
//            throw new NotEnoughStockException("Stock is not enough");
//        }
//        this.quantity += quantity;
//    }
//
//    public void removeQuantity(int quantity) {
//        int restQuantity = this.quantity - quantity;
//        if (restQuantity < 1) { // 최소 1개 이상은 있어야 한다.
//            throw new NotEnoughStockException("ShoppingItem quantity must be more than 1");
//        }
//        this.quantity = restQuantity;
//    }

    public void changeQuantity(int QuantityToChange, int stockQuantity) {
        if (QuantityToChange < 1) { // 최소 1개 이상은 있어야 한다.
            throw new NotEnoughStockException("ShoppingItem quantity must be more than 1");
        }
        if (QuantityToChange > stockQuantity) {
            throw new NotEnoughStockException("Stock is not enough");
        }
        this.quantity = QuantityToChange;
    }
}
