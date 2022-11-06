package com.portfolio.apple.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @DeleteMapping("/admin/api/item/{id}")
    public Long deleteItem(@PathVariable Long id) throws Exception {
        return itemService.deleteItem(id);
    }
}
