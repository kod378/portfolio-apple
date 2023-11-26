package com.portfolio.apple.domain.itemFile;

import com.portfolio.apple.exception.itemFile.UploadFileException;
import com.portfolio.apple.exception.itemFile.UploadFileNotFoundException;
import com.portfolio.apple.exception.itemFile.UploadFileNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemFileService {

    private final ItemFileRepository itemFileRepository;
    @Value("${file.upload-image-dir}")
    private String uploadImagePath;
    Long maxOrderNumber = 0L;

    public List<ItemFile> uploadAndSaveFiles(MultipartFile representationFile, MultipartFile[] files) throws Exception {
        List<String> uploadedFileNames = uploadFiles(representationFile, files);
        return makeItemFiles(representationFile, files, uploadedFileNames);
    }


    private List<String> uploadFiles(MultipartFile representationFile, MultipartFile[] files) {
        List<String> uploadedFileNames = new ArrayList<>();
        uploadedFileNames.add(uploadFile(representationFile));
        if(files != null) {
            for (MultipartFile file : files) {
                if(!file.isEmpty()) {
                    uploadedFileNames.add(uploadFile(file));
                }
            }
        }
        return uploadedFileNames;
    }

    private void validateFileType(MultipartFile file) {
        if(!file.getContentType().startsWith("image/")){
            throw new UploadFileNotValidException("이미지 파일만 업로드 가능합니다.");
        }
    }

    private String uploadFile(MultipartFile file) {
        validateFileType(file);
        String uuid = UUID.randomUUID().toString();
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = uuid + extension;
        String thumbnailName = "s_" + uuid + extension;
        Path filePath = Path.of(uploadImagePath, fileName);
        try {
            file.transferTo(filePath);
            Thumbnailator.createThumbnail(filePath.toFile(), Path.of(uploadImagePath, thumbnailName).toFile(), 100, 100);
        } catch (IOException e) {
            log.error("이미지 파일 업로드 에러", e);
            throw new UploadFileException(e);
        }
        return fileName;
    }

    private List<ItemFile> makeItemFiles(MultipartFile representationFile, MultipartFile[] files, List<String> uploadedFileNames) {
        List<ItemFile> itemFiles = new ArrayList<>();
        int index = 0;
        maxOrderNumber = 0L;
        itemFiles.add(makeItemFile(representationFile, uploadedFileNames.get(index++), true));
        if(files != null) {
            for (MultipartFile file : files) {
                if(!file.isEmpty()) {
                    itemFiles.add(makeItemFile(file, uploadedFileNames.get(index++), false));
                }
            }
        }
        return itemFiles;
    }

    private ItemFile makeItemFile(MultipartFile file, String uploadedFileName, boolean isRepresentation) {
        String fileType = file.getContentType();
        Long size = file.getSize();
        String originalFileName = file.getOriginalFilename();
        Long orderNum = isRepresentation ? 0L : ++maxOrderNumber;
        return ItemFile.builder()
                .fileName(uploadedFileName)
                .fileFullPath(uploadImagePath + File.separator + uploadedFileName)
                .fileType(fileType)
                .size(size)
                .originalFileName(originalFileName)
                .orderNumber(orderNum)
                .build();
    }

    public List<ItemFile> uploadAndUpdateFiles(MultipartFile representationFile, MultipartFile[] files, Long itemId) {
        List<ItemFile> itemFiles = itemFileRepository.findByItemId(itemId);
        List<ItemFile> newFiles = new ArrayList<>();
        if(representationFile.isEmpty()) {
            newFiles.add(itemFiles.get(0));
        } else {
            newFiles.add(makeItemFile(representationFile, uploadFile(representationFile), true));
            itemFileRepository.delete(itemFiles.get(0));
        }

        for (int i = 1; i < itemFiles.size(); i++) {
            newFiles.add(itemFiles.get(i));
        }
        maxOrderNumber = newFiles.get(newFiles.size() - 1).getOrderNumber();
        if(files != null) {
            for (MultipartFile file : files) {
                if(!file.isEmpty()) {
                    newFiles.add(makeItemFile(file, uploadFile(file), false));
                }
            }
        }

        return newFiles;
    }

    @Transactional
    public Long deleteItemFile(Long fileId) {
        ItemFile itemFile = itemFileRepository.findById(fileId).orElseThrow(() -> new UploadFileNotFoundException("파일이 존재하지 않습니다."));
        itemFileRepository.delete(itemFile);
        return fileId;
    }
}
