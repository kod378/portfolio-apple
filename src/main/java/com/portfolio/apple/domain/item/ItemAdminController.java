package com.portfolio.apple.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/item")
public class ItemAdminController {

    private final ItemService itemService;

    @GetMapping("/")
    public String itemList() {
        return "admin/item/list";
    }
}
