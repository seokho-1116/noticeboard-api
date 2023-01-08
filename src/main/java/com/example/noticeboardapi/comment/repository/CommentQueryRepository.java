package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.generated.test.tables.Comment.COMMENT;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final DSLContext dslContext;

    public Page<Comment> find20CommentsByPaging(Long postNo, Pageable pageable) {
        List<Comment> comments = dslContext.select(COMMENT.asterisk())
                .from(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Comment.class);

        int count = dslContext.fetchCount(COMMENT, COMMENT.POST_ID.eq(postNo));

        return new PageImpl<>(comments, pageable, count);
    }

}
