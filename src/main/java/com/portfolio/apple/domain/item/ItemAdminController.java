package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/item")
public class ItemAdminController {

    private final ItemService itemService;
    private final ItemFileService itemFileService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String itemList(Model model,
                           @PageableDefault Pageable pageable) {
        Page<ItemResponseDTO> pageWithResponseDto = itemService.findPageWithResponseDto(pageable);
        int pageStart = (pageable.getPageNumber() / 10) * 10;
        int pageEnd = Math.min(pageStart + 9, pageWithResponseDto.getTotalPages());

        model.addAttribute("categoryDtoList", categoryService.findAllDto());
        model.addAttribute("itemDtoPage", pageWithResponseDto);
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);
        return "admin/item/list";
    }

    @GetMapping("/save")
    public String itemSave(Model model) {
        model.addAttribute("categoryDtoList", categoryService.findAllDto());
        model.addAttribute("itemDTO", new ItemSaveRequestDTO());
        return "admin/item/save";
    }

    //대표 이미지가 없을 경우, Controller에서 MissingServletRequestPartException 발생
    @PostMapping({"", "/"})
    public String itemSaveOrUpdate(Model model, @Valid ItemSaveRequestDTO itemRequestDTO,
                           @RequestPart("representationImage") MultipartFile representationFile, Errors errors,
                           @RequestPart(value = "image", required = false) MultipartFile[] files) throws Exception {
        List<ItemFile> itemFiles = new ArrayList<>();
        if(itemRequestDTO.getId() == null) {
            itemFiles = itemFileService.uploadAndSaveFiles(representationFile, files);
        }else {
            itemFiles = itemFileService.uploadAndUpdateFiles(representationFile, files, itemRequestDTO.getId());
        }
        itemService.saveItem(itemRequestDTO, itemFiles);
        return "redirect:/admin/item/list";
    }

    @GetMapping("/update/{id}")
    public String itemUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("categoryDtoList", categoryService.findAllDto());
        model.addAttribute("itemDTO", itemService.findItemDtoById(id));
        return "admin/item/save";
    }
}
