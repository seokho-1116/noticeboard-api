package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Category;
import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import org.jooq.generated.test.tables.records.PostFileRecord;
import org.jooq.generated.test.tables.records.PostRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Post.POST;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(value = "test")
class PostJpaRepositoryTest {
    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    void savePostTest() {
        Post savedPost = postJpaRepository.save(Post.createPostByFormat("seokho", Category.LOL, "TITLE",
                "TEXT", List.of(new PostFile("jpg", "hi", "Hello"))));
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getPostFiles().size()).isEqualTo(1);
    }

}
