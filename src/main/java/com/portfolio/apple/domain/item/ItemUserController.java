package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ItemUserController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping("/item/list")
    public String itemUserList(Model model, @PageableDefault(size = 9) Pageable pageable) {
        Page<ItemResponseDTO> pageWithResponseDto = itemService.findPageWithResponseDto(pageable);
        int pageStart = (pageable.getPageNumber() / 10) * 10;
        int pageEnd = Math.min(pageStart + 9, pageWithResponseDto.getTotalPages() - 1);

        model.addAttribute("categoryDtoList", categoryService.findAllDto());
        model.addAttribute("itemDtoPage", pageWithResponseDto);
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);
        return "user/item/list";
    }

    @GetMapping("/item/{id}")
    public String itemUserDetail(Model model, @PathVariable Long id) {
        ItemResponseDTO itemResponseDto = itemService.findItemDtoById(id);
//        model.addAttribute("categoryDtoList", categoryService.findAllDto());
        model.addAttribute("itemDto", itemResponseDto);
        return "user/item/detail";
    }
}
