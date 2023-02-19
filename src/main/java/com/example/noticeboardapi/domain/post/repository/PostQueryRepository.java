package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectLimitPercentStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;
import static org.jooq.impl.DSL.multisetAgg;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final DSLContext dslContext;

    public Page<Post> find10Posts(Pageable pageable) {
        int total = dslContext.fetchCount(POST);

        List<Post> posts;
        if (pageable.getSort().isSorted()) {
            posts = getPopularPosts(pageable);
        } else {
            posts = getLatestPost(pageable, total);
        }

        return new PageImpl<>(posts, pageable, total);
    }

    private List<Post> getPopularPosts(Pageable pageable) {
        return getSelectQueryOnLatestPosts(pageable, isPopularPost())
                .offset(pageable.getOffset())
                .fetchInto(Post.class);
    }

    private SelectLimitPercentStep<?> getSelectQueryOnLatestPosts(Pageable pageable, Condition condition) {
        return dslContext
                .select(POST.POST_ID, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        multisetAgg(
                                POST_FILE.FILE_ID,
                                POST_FILE.FILE_TYPE,
                                POST_FILE.STORE_FILE_NAME,
                                POST_FILE.UPLOAD_FILE_NAME).as("post_files"))
                .from(POST)
                .leftOuterJoin(POST_FILE)
                .on(POST.POST_ID.eq(POST_FILE.POST_ID))
                .where(condition)
                .groupBy(POST.POST_ID)
                .orderBy(POST.POST_ID.desc())
                .limit(pageable.getPageSize());
    }

    private Condition isPopularPost() {
        return POST.RECOMMENDATION_COUNT.ge(60);
    }

    private List<Post> getLatestPost(Pageable pageable, int total) {
        Long offset = getOffset(pageable, total);
        return getSelectQueryOnLatestPosts(pageable, isPostIdLeThanCursor(offset))
                .fetchInto(Post.class);
    }

    private long getOffset(Pageable pageable, int total) {
        return total - pageable.getOffset();
    }

    private Condition isPostIdLeThanCursor(Long offset) {
        return POST.POST_ID.le(offset);
    }

    public Post findPostById(Long postNo) {
        return dslContext
                .select(POST.POST_ID, POST.AUTHOR, POST.TITLE,
                        POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        POST.VIEW_COUNT, POST.TEXT,
                        multisetAgg(
                                POST_FILE.FILE_ID,
                                POST_FILE.FILE_TYPE,
                                POST_FILE.STORE_FILE_NAME,
                                POST_FILE.UPLOAD_FILE_NAME).as("post_files"))
                .from(POST)
                .leftOuterJoin(POST_FILE)
                .on(POST.POST_ID.eq(POST_FILE.POST_ID))
                .where(POST.POST_ID.eq(postNo))
                .groupBy(POST.POST_ID)
                .fetchOptionalInto(Post.class)
                .orElseThrow(NoSuchPostException::new);
    }
}
