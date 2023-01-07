package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import com.example.noticeboardapi.post.service.dto.PostThumbnailDto;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
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


    @Test
    void request10Posts() {

        Pageable pageable = PageRequest.of(0, 10);
        Field<Long> pid = POST.POST_ID.as("pid");
        Field<Long> pfid = POST_FILE.POST_ID.as("pfid");

        List<PostThumbnailDto> postThumbnailDtos = dslContext
                .select(pid, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                multiset(
                        (select(POST_FILE.FILE_ID)
                                .from(POST_FILE)
                                .join(POST)
                                .on(pfid.eq(POST.POST_ID))
                        ).where(pid.eq(pfid))
                ).as("postFiles"))
                .from(POST)
                .where(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetchInto(PostThumbnailDto.class);

        int total = dslContext.fetchCount(POST);

        Page<PostThumbnailDto> result = new PageImpl<>(postThumbnailDtos, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    void request10Posts2() {

        record PostFile(Long id, String fileType, String uploadFileName, String storeFileName, Long postId) {}
        record Post(Long id, String author, String title, LocalDateTime created_time, String category, Integer recommendationCount, List<PostFile> postFiles) {}
        Pageable pageable = PageRequest.of(0, 10);
        Field<Long> pid = POST.POST_ID.as("pid");
        Field<Long> pfid = POST_FILE.POST_ID.as("pfid");


        List<Post> postThumbnailDtos = dslContext
                .select(pid, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                multiset(
                        select(pfid, POST_FILE.FILE_TYPE, POST_FILE.UPLOAD_FILE_NAME, POST_FILE.STORE_FILE_NAME, POST_FILE.POST_ID)
                                .from(POST_FILE)
                                .join(POST)
                                .on(pfid.eq(POST.POST_ID))
                                .where()
                ).as("postFiles").convertFrom(r -> r.map(Records.mapping(PostFile::new))))
                .from(POST)
                .where(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetch(Records.mapping(Post::new));

        int total = dslContext.fetchCount(POST);

        Page<Post> result = new PageImpl<>(postThumbnailDtos, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    void request10Posts3() {

        record PostFile(Long id, String fileType, String uploadFileName, String storeFileName, Long postId) {}
        record Post(Long id, String author, String title, LocalDateTime created_time, String category, Integer recommendationCount, List<PostFile> postFiles) {}
        Pageable pageable = PageRequest.of(0, 10);
        Field<Long> pid = POST.POST_ID.as("pid");
        Field<Long> pfid = POST_FILE.POST_ID.as("pfid");

        List<Post> postThumbnailDtos = dslContext
                .select(pid, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        multiset(
                                select(pfid, POST_FILE.FILE_TYPE, POST_FILE.UPLOAD_FILE_NAME, POST_FILE.STORE_FILE_NAME, POST_FILE.POST_ID)
                                        .from(POST_FILE)
                                        .join(POST)
                                        .on(POST_FILE.POST_ID.eq(POST.POST_ID))
                        ).convertFrom(r -> r.map(Records.mapping(PostFile::new))))
                .from(POST)
                .where(pid.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetch(Records.mapping(Post::new));

        int total = dslContext.fetchCount(POST);

        Page<Post> result = new PageImpl<>(postThumbnailDtos, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    void request10Posts4() {

        record PostFile(Long id, String fileType, String uploadFileName, String storeFileName, Long postId) {}
        record Post(Long id, String author, String title, LocalDateTime created_time, String category, Integer recommendationCount, List<PostFile> postFiles) {}
        Pageable pageable = PageRequest.of(0, 10);
        Field<Long> pid = POST.POST_ID.as("pid");
        Field<Long> pfid = POST_FILE.POST_ID.as("pfid");

        List<Post> postThumbnailDtos = dslContext
                .select(POST.POST_ID, POST.AUTHOR, POST.TITLE, POST.CREATED_TIME, POST.CATEGORY, POST.RECOMMENDATION_COUNT,
                        multisetAgg(
                                POST_FILE.FILE_ID, POST_FILE.FILE_TYPE, POST_FILE.UPLOAD_FILE_NAME, POST_FILE.STORE_FILE_NAME, POST_FILE.POST_ID
                        ).as("postFiles").convertFrom(r -> r.map(Records.mapping(PostFile::new))))
                .from(POST)
                .join(POST_FILE)
                .on(POST.POST_ID.eq(POST_FILE.POST_ID))
                .groupBy(POST.POST_ID)
                .having(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetch(Records.mapping(Post::new));

        int total = dslContext.fetchCount(POST);

        Page<Post> result = new PageImpl<>(postThumbnailDtos, pageable, total);

        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }
}
