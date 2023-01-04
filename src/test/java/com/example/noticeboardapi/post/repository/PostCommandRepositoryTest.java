package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.Comment;
import org.jooq.generated.test.tables.PostFile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;
import static org.junit.jupiter.api.Assertions.*;

@JooqTest
class PostCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void updateViewCount(Long postNo) {
        int beforeViewCount = getExecute(postNo);

        dslContext.update(POST)
                .set(POST.VIEW_COUNT, POST.VIEW_COUNT.plus(1))
                .where(POST.POST_ID.eq(postNo))
                .execute();

        int afterViewCount = getExecute(postNo);
        assertThat(afterViewCount - beforeViewCount).isEqualTo(1);
    }

    private Integer getExecute(Long postNo) {
        return dslContext.select(POST.VIEW_COUNT)
                .from(POST)
                .where(POST.POST_ID.eq(postNo))
                .fetchOne().into(Integer.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void deletePost(Long postNo) {
        dslContext.delete(POST_FILE)
                .where(POST_FILE.POST_ID.eq(postNo))
                .execute();
        dslContext.delete(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo))
                .execute();
        dslContext.delete(POST)
                .where(POST.POST_ID.eq(postNo))
                .execute();

        Optional<Post> post = dslContext.selectFrom(POST)
                .where(POST.POST_ID.eq(postNo))
                .fetchOptionalInto(Post.class);

        assertThat(post.isEmpty()).isTrue();
    }
}