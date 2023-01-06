package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
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

    public Page<Post> find10PostsByPaging(Pageable pageable) {
        List<Post> posts = dslContext
                .select(POST.POST_ID, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        multisetAgg(
                                POST_FILE.FILE_ID,
                                POST_FILE.FILE_TYPE,
                                POST_FILE.STORE_FILE_NAME,
                                POST_FILE.UPLOAD_FILE_NAME
                        ).as("postFiles"))
                .from(POST)
                .join(POST_FILE)
                .on(POST.POST_ID.eq(POST_FILE.POST_ID))
                .groupBy(POST.POST_ID)
                .having(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetchInto(Post.class);

        int total = dslContext.fetchCount(POST);

        return new PageImpl<>(posts, pageable, total);
    }

    public Post findPostById(Long postNo) {
        return dslContext.select(POST.POST_ID, POST.AUTHOR, POST.TITLE,
                        POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        POST.VIEW_COUNT, POST.TEXT,
                        multisetAgg(
                                POST_FILE.FILE_ID,
                                POST_FILE.FILE_TYPE,
                                POST_FILE.STORE_FILE_NAME,
                                POST_FILE.UPLOAD_FILE_NAME
                        ).as("post_files").convertFrom(r -> r.into(PostFile.class)))
                .from(POST)
                .join(POST_FILE)
                .on(POST.POST_ID.eq(POST_FILE.POST_ID))
                .groupBy(POST.POST_ID)
                .having(POST.POST_ID.eq(postNo))
                .fetchOneInto(Post.class);
    }
}
