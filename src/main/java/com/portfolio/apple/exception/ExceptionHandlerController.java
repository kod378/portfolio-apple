package com.portfolio.apple.exception;

import com.portfolio.apple.domain.account.user.CurrentUser;
import com.portfolio.apple.domain.account.user.UserAccount;
import com.portfolio.apple.exception.category.CategoryDuplicateException;
import com.portfolio.apple.exception.itemFile.UploadFileException;
import com.portfolio.apple.exception.itemFile.UploadFileNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CategoryDuplicateException.class)
    public String categoryDuplicateException(CategoryDuplicateException e, RedirectAttributes redirectAttributes,
                                             @CurrentUser UserAccount userAccount, HttpServletRequest request) {
        logError(userAccount, request, e);
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/admin/category/list";
    }

    @ExceptionHandler(ApiEntityNotFoundException.class)
    public ResponseEntity<String> categoryNotFoundException(RuntimeException e , @CurrentUser UserAccount userAccount, HttpServletRequest request) {
        logError(userAccount, request, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UploadFileNotValidException.class, UploadFileException.class})
    public String uploadFileNotValidException(UploadFileNotValidException e, RedirectAttributes redirectAttributes,
                                              @CurrentUser UserAccount userAccount, HttpServletRequest request) {
        logError(userAccount, request, e);
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/admin/item/save";
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public String handleMissingServletRequestPartException(MissingServletRequestPartException e, RedirectAttributes redirectAttributes,
                                                           @CurrentUser UserAccount userAccount, HttpServletRequest request) {
        logError(userAccount, request, e);
        redirectAttributes.addFlashAttribute("message", "파일을 선택해주세요.");
        return "redirect:/admin/item/save";
    }

    @ExceptionHandler
    public String handleException(Exception e, @CurrentUser UserAccount userAccount, HttpServletRequest request) {
        logError(userAccount, request, e);
        return "error";
    }

    private static void logError(UserAccount userAccount, HttpServletRequest request, Exception e) {
        if (userAccount != null) {
            log.error("[BAD REQUEST] user : {}, url : {}, message : {}", userAccount.getEmail(), request.getRequestURL(), e.getMessage());
        } else {
            log.error("[BAD REQUEST] url : {}, message : {}", request.getRequestURL(), e.getMessage());
        }
        log.error("Exception", e);
    }
}
