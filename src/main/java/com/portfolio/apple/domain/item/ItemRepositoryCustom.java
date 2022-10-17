package com.portfolio.apple.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepositoryCustom {
    Page<ItemResponseDTO> findPageWithResponseDto(Pageable pageable);

    Optional<ItemResponseDTO> findItemResponseDtoById(Long itemId);
}
