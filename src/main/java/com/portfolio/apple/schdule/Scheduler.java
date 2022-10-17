package com.portfolio.apple.schdule;

import com.portfolio.apple.domain.itemFile.ItemFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@EnableScheduling
public class Scheduler {

    private final ItemFileRepository itemFileRepository;

    @Value("${file.upload-image-dir}")
    String uploadPath;

    public Scheduler(ItemFileRepository itemFileRepository) {
        this.itemFileRepository = itemFileRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotMatchFiles() {
        System.out.println("deleteNotMatchFiles start");
        List<Path> paths = getPaths();
        try {
            Thread.sleep(10000);
            searchDbAndDeleteRemainedFiles(paths);
        }
        catch (InterruptedException e) {
            log.error("Thread.sleep error", e);
            throw new RuntimeException(e);
        }
        System.out.println("deleteNotMatchFiles end");
    }

    private List<Path> getPaths() {
        List<Path> paths = Collections.emptyList();
        try(Stream<Path> walk = Files.walk(Paths.get(uploadPath), 1)) {
            paths = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Files.walk error", e);
            e.printStackTrace();
        }
        return paths;
    }

    private void searchDbAndDeleteRemainedFiles(List<Path> paths) {
        Map<String, List<Path>> collectMap = paths.stream().collect(Collectors.groupingBy(path -> path.getFileName().toString()));
        List<String> FileNamesInDB = itemFileRepository.findAllFileNames();
        for (String fileName : FileNamesInDB) {
            collectMap.remove(fileName);
            collectMap.remove("s_" + fileName);
        }
        collectMap.values().forEach(paths1 -> paths1.forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error("Files.delete error", e);
                throw new RuntimeException(e);
            }
        }));
    }
}
