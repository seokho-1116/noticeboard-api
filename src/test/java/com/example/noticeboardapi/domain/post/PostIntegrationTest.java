package com.example.noticeboardapi.domain.post;

import com.example.noticeboardapi.web.post.PostCreateFormat;
import com.example.noticeboardapi.domain.post.entity.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void PostSaveTest() throws Exception {
        PostCreateFormat postCreateFormat = new PostCreateFormat();
        postCreateFormat.setAuthor("seokho");
        postCreateFormat.setCategory(Category.LOL);
        postCreateFormat.setTitle("hi");
        postCreateFormat.setText("hello");

        MockMultipartFile multipartFile1 = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile2 = new MockMultipartFile("file", "test2.txt", "text/plain", "test file2".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile postCreateFormatFile = new MockMultipartFile("postCreateFormat", "postCreateFormat",
                "application/json", objectMapper.writeValueAsBytes(postCreateFormat));

        mvc.perform(multipart("/posts")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(postCreateFormatFile))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
