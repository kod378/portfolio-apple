package com.portfolio.apple.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/category")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("categorySaveRequestDTO", new CategorySaveRequestDTO());
        model.addAttribute("categoryDtoList", categoryService.findAllDto());

        return "admin/category/list";
    }

    @PostMapping("")
    public String register(@Valid CategorySaveRequestDTO categorySaveFormDTO, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            return "admin/category/list";
        }

        categoryService.saveCategory(categorySaveFormDTO);
        return "redirect:/admin/category/list";
    }
}
