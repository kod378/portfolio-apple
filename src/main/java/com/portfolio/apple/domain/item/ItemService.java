package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.exception.item.ItemNotFoundException;
import com.portfolio.apple.mapper.ItemMapper;
import com.portfolio.apple.domain.itemFile.ItemFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public Item saveItem(ItemSaveRequestDTO itemRequestDTO, List<ItemFile> itemFiles) {
        Item item = itemMapper.saveRequestDtoToEntity(itemRequestDTO);
        item.applyItemFiles(itemFiles);
        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Item item, ItemSaveRequestDTO itemFormDTO, Category category) {
        return itemRepository.save(item);
    }

    public Item findItemByName(String itemName) {
        return itemRepository.findByName(itemName).orElseThrow(() -> new ItemNotFoundException("상품을 찾을 수 없습니다."));
    }

    public ItemResponseDTO findItemDtoById(Long itemId) {
        return itemRepository.findItemResponseDtoById(itemId).orElseThrow(() -> new ItemNotFoundException("상품을 찾을 수 없습니다."));
    }

    public Page<ItemResponseDTO> findPageWithResponseDto(Pageable pageable) {
        return itemRepository.findPageWithResponseDto(pageable);
    }

    @Transactional
    public Long deleteItem(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("상품을 찾을 수 없습니다."));
        itemRepository.delete(findItem);
        return id;
    }
}
