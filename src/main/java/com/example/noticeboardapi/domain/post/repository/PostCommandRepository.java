package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.records.PostRecord;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.test.Tables.*;
import static org.jooq.generated.test.tables.Post.POST;

@Repository
@RequiredArgsConstructor
public class PostCommandRepository {

    private final DSLContext dslContext;

    public void updateViewCount(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.setViewCount(postRecord.getViewCount() + 1);
        postRecord.store(POST.VIEW_COUNT);
    }

    public void updateRecommendationCount(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.setViewCount(postRecord.getRecommendationCount() + 1);
        postRecord.store(POST.RECOMMENDATION_COUNT);
    }

    private PostRecord getPostRecord(Long postNo) {
        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));

        if (postRecord == null) {
            throw new NoSuchPostException();
        }

        return postRecord;
    }

    public void deletePost(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.delete();
    }
}
