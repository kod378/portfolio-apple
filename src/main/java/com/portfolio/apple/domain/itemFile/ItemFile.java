package com.portfolio.apple.domain.itemFile;

import com.portfolio.apple.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileFullPath;

    private String fileType;

    private Long size;

    private String originalFileName;

    @Min(0)
    private Long orderNumber;  // 파일 순서, 0이면 대표이미지

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public void setItem(Item item) {
        this.item = item;
    }
}
