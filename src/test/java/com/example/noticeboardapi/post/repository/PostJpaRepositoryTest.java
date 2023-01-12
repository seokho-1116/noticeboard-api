package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(value = "test")
class PostJpaRepositoryTest {
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private PostFileJpaRepository postFileJpaRepository;

    @Test
    void saveTest() {
        Post savedPost = postJpaRepository.save(Post.createPostByFormat("seokho", Category.LOL, "TITLE", "TEXT", Collections.emptyList()));
        assertThat(savedPost.getId()).isEqualTo(1001);
    }

    @Test
    void savePostFileTest() {
        postJpaRepository.save(Post.createPostByFormat("seokho", Category.LOL, "TITLE",
                "TEXT", List.of(new PostFile("jpg", "hi", "Hello"))));
        Optional<PostFile> postFile = postFileJpaRepository.findById(501L);
        assertThat(postFile.isPresent()).isTrue();
    }
}