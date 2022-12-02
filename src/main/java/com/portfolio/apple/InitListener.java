package com.portfolio.apple;

import com.portfolio.apple.domain.account.admin.AdminAccountService;
import com.portfolio.apple.domain.category.CategorySaveRequestDTO;
import com.portfolio.apple.domain.category.CategoryService;
import com.portfolio.apple.domain.item.ItemSaveRequestDTO;
import com.portfolio.apple.domain.item.ItemService;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.ItemFileService;
import lombok.RequiredArgsConstructor;

import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class InitListener implements ApplicationListener<ApplicationStartedEvent> {

    private final AdminAccountService adminAccountService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ItemFileService itemFileService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
//        adminAccountService.saveAdminAccount(new AdminJoinFormDTO("test", "test", "test"));
//        System.out.println("관리자 계정 초기 생성됨");
        //아이템 및 파일이미지 미리 생성
//        try {
//            initSampleItems();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    private void initSampleItems() throws Exception {
        categoryService.saveCategory(new CategorySaveRequestDTO("testCategory1"));
        List<ItemFile> itemFileList = Collections.emptyList();
        Path path1 = Paths.get(Objects.requireNonNull(getClass().getResource("/static/images/사과1.jpg")).toURI());
        Path path2 = Paths.get(Objects.requireNonNull(getClass().getResource("/static/images/사과2.jpg")).toURI());
        Path path3 = Paths.get(Objects.requireNonNull(getClass().getResource("/static/images/사과3.jpg")).toURI());
        FileItem fileItem1 = new DiskFileItem("representationImage", "image/jpeg", false, path1.getFileName().toString(), (int) path1.toFile().length(), path1.getParent().toFile());
        FileItem fileItem2 = new DiskFileItem("image", "image/jpeg", false, path2.getFileName().toString(), (int) path2.toFile().length(), path2.getParent().toFile());
        FileItem fileItem3 = new DiskFileItem("image", "image/jpeg", false, path3.getFileName().toString(), (int) path3.toFile().length(), path3.getParent().toFile());
        try {
            try (InputStream input = Files.newInputStream(path1);
                 OutputStream os = fileItem1.getOutputStream()) {
                IOUtils.copy(input, os);
            }
            try (InputStream input = Files.newInputStream(path2);
                 OutputStream os = fileItem2.getOutputStream()) {
                IOUtils.copy(input, os);
            }
            try (InputStream input = Files.newInputStream(path3);
                 OutputStream os = fileItem3.getOutputStream()) {
                IOUtils.copy(input, os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Part part1 = new ApplicationPart(fileItem1, path1.toFile());
        Part part2 = new ApplicationPart(fileItem2, path2.toFile());
        Part part3 = new ApplicationPart(fileItem3, path3.toFile());
        MultipartFile representationFile = new CustomMultipartFile(part1, path1.getFileName().toString());
        MultipartFile[] multipartFiles = new MultipartFile[2];
        multipartFiles[0] = new CustomMultipartFile(part2, path2.getFileName().toString());
        multipartFiles[1] = new CustomMultipartFile(part3, path3.getFileName().toString());

        for(int i=0; i<20; i++){
            ItemSaveRequestDTO itemSaveRequestDTO = new ItemSaveRequestDTO(10, 10000, "item"+i, true, "content"+i, "testCategory1");
            itemFileList = itemFileService.uploadAndSaveFiles(representationFile, multipartFiles);
            itemService.saveItem(itemSaveRequestDTO, itemFileList);
        }
    }
}
