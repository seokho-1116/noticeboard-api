package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final DSLContext dslContext;

    public Page<Post> find10PostsByPaging(Pageable pageable) {
        List<Post> posts = dslContext.select(POST.POST_ID, POST.AUTHOR, POST.CATEGORY, POST.CREATED_TIME, POST.RECOMMENDATION_COUNT)
                .from(POST)
                .where(POST.POST_ID.gt(pageable.getOffset()))
                .limit(pageable.getPageSize())
                .fetchInto(Post.class);

        int total = dslContext.fetchCount(POST);

        return new PageImpl<>(posts, pageable, total);
    }
}
