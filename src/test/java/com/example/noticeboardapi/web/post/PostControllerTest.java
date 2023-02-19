package com.example.noticeboardapi.web.post;

import com.example.noticeboardapi.domain.post.entity.Category;
import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import com.example.noticeboardapi.domain.post.service.PostCommandService;
import com.example.noticeboardapi.domain.post.service.PostReadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @MockBean
    private PostReadService postReadService;

    @Test
    @DisplayName("게시글 생성 요청 테스트")
    void requestWritingPostTest() throws Exception {
        PostCreateFormat postCreateFormat = new PostCreateFormat();
        postCreateFormat.setAuthor("seokho");
        postCreateFormat.setCategory(Category.LOL);
        postCreateFormat.setTitle("hi");
        postCreateFormat.setText("hello");

        List<MockMultipartFile> multipartFiles = List.of(new MockMultipartFile("image.jpg", new byte[1000]),
                new MockMultipartFile("p1.mp4", new byte[10000]));
        MockMultipartFile postCreateFormatFile = new MockMultipartFile("postCreateFormat", "postCreateFormat",
                "application/json", objectMapper.writeValueAsBytes(postCreateFormat));


        given(postCommandService.savePost(any(PostCreateFormat.class), any())).willReturn(1L);

        mvc.perform(multipart("/posts")
                        .file(multipartFiles.get(0))
                        .file(multipartFiles.get(1))
                        .file(postCreateFormatFile))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/1"))
                .andReturn();
    }

    @Test
    @DisplayName("게시글 요청 폼 없이 게시글 생성 요청 예외 처리 테스트")
    void requestWritingPostWithoutFormExceptionHandleTest() throws Exception {
        mvc.perform(multipart("/posts"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("검증 실패인 게시글 요청 폼으로 게시글 생성 요청 예외 처리 테스트")
    void requestWritingPostWithNotValidFormExceptionHandleTest() throws Exception {
        PostCreateFormat postCreateFormat = new PostCreateFormat();
        postCreateFormat.setAuthor("");
        postCreateFormat.setCategory(Category.LOL);
        postCreateFormat.setTitle("hi");
        postCreateFormat.setText("hello");

        MockMultipartFile postCreateFormatFile = new MockMultipartFile("postCreateFormat", "postCreateFormat",
                "application/json", objectMapper.writeValueAsBytes(postCreateFormat));


        mvc.perform(multipart("/posts")
                        .file(postCreateFormatFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 특정 게시글 요청 예외 처리 테스트")
    void requestSpecificPostExceptionHandleTest() throws Exception {
        given(postReadService.findPost(anyLong())).willThrow(NoSuchPostException.class);

        mvc.perform(get("/posts/1"))
                .andExpect(status().isNotFound());
    }

}
