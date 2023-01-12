package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.impl.DSL.groupConcat;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final DSLContext dslContext;

    public Page<Comment> find20CommentsByPaging(Long postNo, Pageable pageable) {
        org.jooq.generated.test.tables.Comment c1 = new org.jooq.generated.test.tables.Comment("c1");
        org.jooq.generated.test.tables.Comment c2 = new org.jooq.generated.test.tables.Comment("c2");
        TreePath cc1 = new TreePath("cc1");
        TreePath cc2 = new TreePath("cc2");
        TreePath breadcrumb = new TreePath("breadcrumb");

        List<Comment> comments = dslContext
                .select(
                        c2.asterisk(),
                        cc2.ANCESTOR,
                        groupConcat(breadcrumb.ANCESTOR).orderBy(breadcrumb.DEPTH.desc()).as("breadcrumbs")
                )
                .from(c1)
                .join(cc1).on(cc1.ANCESTOR.eq(c1.COMMENT_ID).and(cc1.POST_ID.eq(postNo)).and(c1.POST_ID.eq(postNo)))
                .join(c2).on(cc1.DESCENDANT.eq(c2.COMMENT_ID).and(c2.POST_ID.eq(postNo)))
                .leftOuterJoin(cc2).on(cc2.DESCENDANT.eq(c2.COMMENT_ID).and(cc2.DEPTH.eq(1L)))
                .join(breadcrumb).on(cc1.DESCENDANT.eq(breadcrumb.DESCENDANT))
                .where(c1.PARENT_ID.isNull())
                .groupBy(cc1.DESCENDANT)
                .orderBy(DSL.field("breadcrumbs"))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Comment.class);

        int count = dslContext.fetchCount(COMMENT, COMMENT.POST_ID.eq(postNo));

        return new PageImpl<>(comments, pageable, count);
    }

}
