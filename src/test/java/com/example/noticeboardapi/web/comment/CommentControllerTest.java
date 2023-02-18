package com.example.noticeboardapi.web.comment;

import com.example.noticeboardapi.domain.comment.service.CommentCommandService;
import com.example.noticeboardapi.domain.comment.service.CommentReadService;
import com.example.noticeboardapi.domain.comment.service.dto.CommentDto;
import com.example.noticeboardapi.web.post.PostController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentCommandService commentCommandService;

    @MockBean
    private CommentReadService commentReadService;

    @Test
    @DisplayName("댓글 쓰기 테스트")
    void requestWritingCommentTest() throws Exception {
        CommentCreateFormat commentCreateFormat = new CommentCreateFormat();
        commentCreateFormat.setAuthor("seokho7799");
        commentCreateFormat.setText("hello");

        given(commentCommandService.saveComment(anyLong(), any(CommentCreateFormat.class))).willReturn(new CommentDto());

        mvc.perform(post("/posts/1/comments")
                        .content(objectMapper.writeValueAsString(commentCreateFormat))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/posts/1/comments"));
    }
}
