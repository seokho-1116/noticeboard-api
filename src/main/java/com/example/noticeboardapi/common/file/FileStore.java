package com.example.noticeboardapi.common.file;

import com.example.noticeboardapi.post.entity.PostFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    private final String fileDir;

    public FileStore(@Value("${file.dir}") String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<PostFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<PostFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public PostFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return PostFile.createPostFile(extractExt(originalFilename), originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}