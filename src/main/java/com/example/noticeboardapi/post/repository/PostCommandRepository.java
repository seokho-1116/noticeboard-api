package com.example.noticeboardapi.post.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

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
}
