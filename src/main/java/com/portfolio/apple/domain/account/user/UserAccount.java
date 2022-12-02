package com.portfolio.apple.domain.account.user;

import com.portfolio.apple.domain.Address;
import com.portfolio.apple.domain.BaseTimeEntity;
import com.portfolio.apple.domain.shoppingItem.ShoppingItem;
import com.portfolio.apple.domain.account.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "userAccount")
    private List<ShoppingItem> shoppingItems = new ArrayList<>();

    public UserAccount update(String name) {
        this.name = name;
        return this;
    }

    public UserAccount updateEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void changeAddress(Address address) {
        this.address = address;
    }

    public void putShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.add(shoppingItem);
    }

    public void deleteShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.remove(shoppingItem);
    }
}
