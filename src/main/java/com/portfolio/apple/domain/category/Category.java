package com.portfolio.apple.domain.category;

import com.portfolio.apple.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public static Category createCategory(String categoryName) {
        return new Category(categoryName);
    }

    public void updateCategoryName(String name) {
        this.name = name;
    }
}
