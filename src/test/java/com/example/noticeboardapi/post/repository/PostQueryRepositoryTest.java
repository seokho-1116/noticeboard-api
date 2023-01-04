package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.jooq.generated.test.tables.Post.POST;
import static org.junit.jupiter.api.Assertions.*;

@JooqTest
public class PostQueryRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Test
    @DisplayName("메인 페이지에서 게시글 10개의 썸네일 목록을 최신순으로 보여주기 테스트")
    void request10PostsThumbnailOrderByCreatedTime() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = dslContext.select(POST.POST_ID, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT)
                .from(POST)
                .where(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetchInto(Post.class);

        int total = dslContext.fetchCount(POST);

        Page<Post> result = new PageImpl<>(posts, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시글 번호로 특정 게시글 요청 테스트")
    void requestSpecificPost() {
        Long pageNo = 1L;
        Post post = dslContext.select(POST.asterisk())
                .from(POST)
                .where(POST.POST_ID.eq(pageNo))
                .fetchOneInto(Post.class);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(1L);
    }
}
