package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.test.Tables.COMMENT;
import static org.jooq.generated.test.Tables.POST_FILE;
import static org.jooq.generated.test.tables.Post.POST;

@Repository
@RequiredArgsConstructor
public class PostCommandRepository {

    private final DSLContext dslContext;

    public void updateViewCount(Long postNo) {
        dslContext.update(POST)
                .set(POST.VIEW_COUNT, POST.VIEW_COUNT.plus(1))
                .where(POST.POST_ID.eq(postNo))
                .execute();
    }

    public void updateRecommendationCount(Long postNo) {
        dslContext.select(POST.asterisk())
                .from(POST)
                .where(POST.POST_ID.eq(postNo))
                .fetchOneInto(Post.class);
    }

    public void deletePost(Long postNo) {
        dslContext.delete(POST_FILE)
                .where(POST_FILE.POST_ID.eq(postNo))
                .execute();

        dslContext.delete(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo))
                .execute();

        dslContext.delete(POST)
                .where(POST.POST_ID.eq(postNo))
                .execute();
    }
}
