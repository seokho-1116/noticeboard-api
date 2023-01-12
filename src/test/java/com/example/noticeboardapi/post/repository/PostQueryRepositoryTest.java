package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;
import static org.jooq.impl.DSL.*;

@JooqTest
@ActiveProfiles("test")
public class PostQueryRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Test
    @DisplayName("메인 페이지에서 게시글 10개의 썸네일 목록을 최신순으로 보여주기 테스트")
    void request10PostsThumbnailOrderByCreatedTime() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Post> posts = dslContext
                .select(POST.POST_ID, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
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
                .having(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetchInto(Post.class);

        int total = dslContext.fetchCount(POST);

        Page<Post> result = new PageImpl<>(posts, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
        assertThat(result).allMatch(r -> r.getCreatedTime() != null);
    }

    @Test
    @DisplayName("게시글 번호로 특정 게시글 요청 테스트")
    void requestSpecificPost() {
        Long postNo = 1L;
        Post post = dslContext.select(POST.POST_ID, POST.AUTHOR, POST.TITLE,
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

        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(1L);
    }
}
