package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final DSLContext dslContext;

    public Page<Comment> find20Comments(Long postNo, Pageable pageable) {
        org.jooq.generated.test.tables.Comment c1 = new org.jooq.generated.test.tables.Comment("c1");
        org.jooq.generated.test.tables.Comment c2 = new org.jooq.generated.test.tables.Comment("c2");
        TreePath tp1 = new TreePath("tp1");
        TreePath tp2 = new TreePath("tp2");
        TreePath breadcrumb = new TreePath("breadcrumb");

        List<Comment> comments = dslContext
                .select(
                        c2.asterisk(),
                        tp2.ANCESTOR,
                        groupConcat(breadcrumb.ANCESTOR).orderBy(breadcrumb.DEPTH.desc()).as("breadcrumbs")
                )
                .from(c1)
                .join(tp1).on(tp1.ANCESTOR.eq(c1.COMMENT_ID).and(tp1.POST_ID.eq(postNo)).and(c1.POST_ID.eq(postNo)))
                .join(c2).on(tp1.DESCENDANT.eq(c2.COMMENT_ID).and(c2.POST_ID.eq(postNo)))
                .leftOuterJoin(tp2).on(tp2.DESCENDANT.eq(c2.COMMENT_ID).and(tp2.DEPTH.eq(1)))
                .join(breadcrumb).on(tp1.DESCENDANT.eq(breadcrumb.DESCENDANT))
                .where(c1.COMMENT_ID.eq(0L))
                .groupBy(tp1.DESCENDANT)
                .orderBy(DSL.field("breadcrumbs"))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Comment.class);

        int count = dslContext.fetchCount(COMMENT, COMMENT.POST_ID.eq(postNo));

        return new PageImpl<>(comments, pageable, count);
    }

    public Page<Comment> find20CommentsContainingSpecificComment(Long postNo, Long commentNo) {
        TreePath tp1 = new TreePath("tp1");
        TreePath tp2 = new TreePath("tp2");
        TreePath breadcrumb = new TreePath("breadcrumb");

        Long rowNum = dslContext
                .select(field("r.row_num"))
                .from(select(rowNumber().over(orderBy(field("breadcrumbs"))).as("row_num"),
                        tp1.DESCENDANT,
                        groupConcat(breadcrumb.ANCESTOR).orderBy(breadcrumb.DEPTH.desc()).as("breadcrumbs"))
                        .from(tp1)
                        .leftOuterJoin(tp2).on(tp2.DESCENDANT.eq(tp1.DESCENDANT).and(tp2.DEPTH.eq(1)))
                        .join(breadcrumb).on(tp1.DESCENDANT.eq(breadcrumb.DESCENDANT))
                        .where(tp1.ANCESTOR.eq(0L))
                        .groupBy(tp1.DESCENDANT)
                        .orderBy(field("breadcrumbs")).asTable("r"))
                .where("r.descendant = " + commentNo.toString())
                .fetchOneInto(Long.class);

        int page = rowNum.intValue() / 20;
        return find20Comments(postNo, PageRequest.of(page, 20));
    }
}
