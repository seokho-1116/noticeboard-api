package com.example.noticeboardapi.domain.common;

import com.example.noticeboardapi.domain.common.file.FileStore;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class FileStoreTest {

    private String fileDir = System.getProperty("user.dir") + "/PostFiles/";
    private FileStore fileStore = new FileStore(fileDir);

    @Test
    @DisplayName("파일 저장을 테스트")
    void storeFilesTest() {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            multipartFiles.add(new MockMultipartFile("image" + i + ".jpg", "image" + i + ".jpg",
                    "jpg", new byte[100]));
        }
        List<PostFile> postFiles = fileStore.storeFiles(multipartFiles);

        assertThat(postFiles).extracting(PostFile::getUploadFileName).allMatch(n -> n.matches("image[0-9]+.jpg"));
        assertThat(postFiles).extracting(PostFile::getFileType).allMatch(n -> n.equals("jpg"));
    }
}