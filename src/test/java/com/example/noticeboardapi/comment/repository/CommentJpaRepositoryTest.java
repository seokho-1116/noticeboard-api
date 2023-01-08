package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles(value = "test")
public class CommentJpaRepositoryTest {

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Test
    void saveCommentTest() {
        Comment comment = Comment.createCommentByFormat(1L, "seokho", "text", LocalDateTime.now());
        Comment savedComment = commentJpaRepository.save(comment);
        assertThat(savedComment.getId()).isEqualTo(1001L);
        assertThat(savedComment.getPostId()).isEqualTo(1L);
    }
}
