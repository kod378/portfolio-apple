package com.portfolio.apple.domain.itemFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ItemFileResponseDTO {
    private Long id;
    private String fileName;
    private String originalFileName;
}
