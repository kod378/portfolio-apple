package com.portfolio.apple.exception;

import com.portfolio.apple.exception.category.CategoryDuplicateException;
import com.portfolio.apple.exception.category.CategoryNotFoundException;
import com.portfolio.apple.exception.item.ItemNotFoundException;
import com.portfolio.apple.exception.itemFile.UploadFileException;
import com.portfolio.apple.exception.itemFile.UploadFileNotFoundException;
import com.portfolio.apple.exception.itemFile.UploadFileNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CategoryDuplicateException.class)
    public String categoryDuplicateException(CategoryDuplicateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/admin/category/list";
    }

    @ExceptionHandler({CategoryNotFoundException.class, ItemNotFoundException.class, UploadFileNotFoundException.class})
    public ResponseEntity<String> categoryNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UploadFileNotValidException.class, UploadFileException.class})
    public String uploadFileNotValidException(UploadFileNotValidException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/admin/item/save";
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public String handleMissingServletRequestPartException(MissingServletRequestPartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "파일을 선택해주세요.");
        return "redirect:/admin/item/save";
    }
}
