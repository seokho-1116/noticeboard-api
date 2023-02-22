package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
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
    private final static long ROOT_COMMENT_ID = 0;

    public Page<Comment> find20Comments(Long postNo, Pageable pageable) {
        org.jooq.generated.test.tables.Comment parentComment = new org.jooq.generated.test.tables.Comment("parentComment");
        org.jooq.generated.test.tables.Comment childComment = new org.jooq.generated.test.tables.Comment("childComment");
        TreePath parentPath = new TreePath("parentPath");
        TreePath childPath = new TreePath("childPath");
        TreePath breadcrumbPath = new TreePath("breadcrumbPath");

        List<Comment> comments = dslContext
                .select(
                        childComment.asterisk(),
                        childPath.ANCESTOR,
                        groupConcat(breadcrumbPath.ANCESTOR).orderBy(breadcrumbPath.DEPTH.desc()).as("breadcrumbs")
                )
                .from(parentComment)
                .join(parentPath).on(parentPath.ANCESTOR.eq(parentComment.COMMENT_ID).and(parentPath.POST_ID.eq(postNo)).and(parentComment.POST_ID.eq(postNo)))
                .join(childComment).on(parentPath.DESCENDANT.eq(childComment.COMMENT_ID).and(childComment.POST_ID.eq(postNo)))
                .leftOuterJoin(childPath).on(childPath.DESCENDANT.eq(childComment.COMMENT_ID).and(childPath.DEPTH.eq(1)))
                .join(breadcrumbPath).on(parentPath.DESCENDANT.eq(breadcrumbPath.DESCENDANT))
                .where(parentComment.COMMENT_ID.eq(ULong.valueOf(ROOT_COMMENT_ID)))
                .groupBy(parentPath.DESCENDANT)
                .orderBy(breadcrumbPath.ANCESTOR)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset() + 1)
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
                        .where(tp1.ANCESTOR.eq(ULong.valueOf((0L))))
                        .groupBy(tp1.DESCENDANT)
                        .orderBy(field("breadcrumbs")).asTable("r"))
                .where("r.descendant = " + commentNo.toString())
                .fetchOneInto(Long.class);

        int page = rowNum.intValue() / 20;
        return find20Comments(postNo, PageRequest.of(page, 20));
    }
}
