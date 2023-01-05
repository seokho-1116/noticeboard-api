package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.entity.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(value = "test")
class PostJpaRepositoryTest {
    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    void saveTest() {
        Post savedPost = postJpaRepository.save(Post.createPostByFormat("seokho", Category.LOL, "TITLE", "TEXT"));
        assertThat(savedPost.getId()).isEqualTo(1001);
    }
}