package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.Category;
import com.portfolio.apple.domain.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public Item saveItem(ItemRequestDTO itemRequestDTO) {
        Item item = itemMapper.dtoToEntity(itemRequestDTO);
        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Item item, ItemRequestDTO itemFormDTO, Category category) {
        return itemRepository.save(item);
    }

    public Item findItemByName(String itemName) {
        return itemRepository.findByName(itemName).orElseThrow((Supplier<IllegalArgumentException>) () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

    public Optional<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }
}
