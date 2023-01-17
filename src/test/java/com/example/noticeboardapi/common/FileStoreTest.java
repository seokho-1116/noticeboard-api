package com.example.noticeboardapi.common;

import com.example.noticeboardapi.common.file.FileStore;
import com.example.noticeboardapi.post.entity.PostFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FileStoreTest {

    private FileStore fileStore = new FileStore("C:/Users/seokh/IdeaProjects/noticeboard-api/src/test/resources");

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