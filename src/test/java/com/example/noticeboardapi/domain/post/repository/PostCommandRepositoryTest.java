package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.Comment;
import org.jooq.generated.test.tables.TreePath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@JooqTest
class PostCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("게시물 조회수 1 추가 테스트")
    void updateViewCountTest(Long postNo) {
        Post before = getExecute(postNo);

        dslContext.update(POST)
                .set(POST.VIEW_COUNT, POST.VIEW_COUNT.plus(1))
                .where(POST.POST_ID.eq(postNo))
                .execute();

        Post after = getExecute(postNo);
        assertThat(after.getViewCount() - before.getViewCount()).isEqualTo(1);
    }

    private Post getExecute(Long postNo) {
        return dslContext.select()
                .from(POST)
                .where(POST.POST_ID.eq(postNo))
                .fetchOneInto(Post.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("게시물 삭제 테스트")
    void deletePostTest(Long postNo) {
        dslContext.delete(POST_FILE)
                .where(POST_FILE.POST_ID.eq(postNo))
                .execute();
        dslContext.delete(TREE_PATH)
                .where(TREE_PATH.POST_ID.eq(postNo))
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
        Optional<PostFile> postFile = dslContext.selectFrom(POST_FILE)
                .where(POST_FILE.POST_ID.eq(postNo))
                .fetchOptionalInto(PostFile.class);
        Optional<Comment> comment = dslContext.selectFrom(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo))
                .fetchOptionalInto(Comment.class);


        assertThat(post.isEmpty()).isTrue();
        assertThat(postFile.isEmpty()).isTrue();
        assertThat(comment.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("게시물 추천 테스트")
    void updateRecommendationCountTest(Long postNo) {
        Post before = getExecute(postNo);

        dslContext.update(POST)
                .set(POST.RECOMMENDATION_COUNT, POST.RECOMMENDATION_COUNT.plus(1))
                .where(POST.POST_ID.eq(postNo))
                .execute();

        Post after = getExecute(postNo);
        assertThat(after.getRecommendationCount() - before.getRecommendationCount()).isEqualTo(1);
    }
}
