package com.portfolio.apple.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryApiController {

    private final CategoryService categoryService;

    @PutMapping("/{id}")
    public Long updateCategory(@PathVariable Long id,  @RequestBody @Valid CategorySaveRequestDTO categorySaveFormDTO) throws Exception {
        return categoryService.updateCategory(id, categorySaveFormDTO);
    }

    @DeleteMapping("/{id}")
    public Long deleteCategory(@PathVariable Long id) throws Exception {
        return categoryService.deleteCategory(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
