package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.Comment;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.generated.test.tables.records.PostRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@JooqTest
@ActiveProfiles("test")
class PostCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("게시물 조회수 1 추가 테스트")
    void updateViewCountTest(Long postNo) {
        Post before = getExecute(postNo);

        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));

        postRecord.setViewCount(postRecord.getViewCount() + 1);
        postRecord.store(POST.VIEW_COUNT);

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
    @DisplayName("게시물 추천 테스트")
    void updateRecommendationCountTest(Long postNo) {
        Post before = getExecute(postNo);

        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));

        postRecord.setRecommendationCount(postRecord.getRecommendationCount() + 1);
        postRecord.store(POST.RECOMMENDATION_COUNT);

        Post after = getExecute(postNo);
        assertThat(after.getRecommendationCount() - before.getRecommendationCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("게시물 삭제 테스트")
    void deletePostTest(Long postNo) {
        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));
        postRecord.delete();

        Optional<Post> post = dslContext.selectFrom(POST)
                .where(POST.POST_ID.eq(postNo))
                .fetchOptionalInto(Post.class);
        Optional<PostFile> postFile = dslContext.selectFrom(POST_FILE)
                .where(POST_FILE.POST_ID.eq(postNo))
                .fetchOptionalInto(PostFile.class);
        Optional<TreePath> treePath = dslContext.selectFrom(TREE_PATH)
                .where(TREE_PATH.POST_ID.eq(postNo))
                .fetchOptionalInto(TreePath.class);
        Optional<Comment> comment = dslContext.selectFrom(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo))
                .fetchOptionalInto(Comment.class);


        assertThat(post.isEmpty()).isTrue();
        assertThat(postFile.isEmpty()).isTrue();
        assertThat(treePath.isEmpty()).isTrue();
        assertThat(comment.isEmpty()).isTrue();
    }
}
