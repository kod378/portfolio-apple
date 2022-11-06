package com.portfolio.apple.domain.itemFile;

import com.portfolio.apple.exception.itemFile.UploadFileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemFileApiController {

    private final ItemFileService itemFileService;

    @DeleteMapping("/admin/api/itemFile/{id}")
    public Long deleteItemFile(@PathVariable Long id) throws Exception {

        return itemFileService.deleteItemFile(id);
    }
}
