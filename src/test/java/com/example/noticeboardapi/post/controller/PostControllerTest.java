package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.service.PostCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostCommandService postCommandService;

    @Test
    @DisplayName("글쓰기 요청 테스트")
    void requestWritingPostTest() throws Exception {
        PostFormat postFormat = PostFormat.createPostFormat("seokho", Category.LOL, "hi", "hello",
                List.of(new MockMultipartFile("image.jpg",new byte[1000]),
                        new MockMultipartFile("p1.mp4", new byte[10000])));

        given(postCommandService.savePost(any(PostFormat.class))).willReturn(1L);
        MockHttpServletResponse response = mvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postFormat)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertEquals("/post/1", response.getHeader("Location"));
    }

}
