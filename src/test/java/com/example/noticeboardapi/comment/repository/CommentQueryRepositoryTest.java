package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import com.sun.source.tree.Tree;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;
import static org.jooq.impl.DSL.*;

@JooqTest
@ActiveProfiles("test")
public class CommentQueryRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("특정 게시글 하위에 있는 댓글 20개 페이징")
    void request20CommentUnderSpecificPostTest(Long postNo) {
        Pageable pageable = PageRequest.of(0, 20);

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

        Page<Comment> page = new PageImpl<>(comments, pageable, count);
        Long[] orders = {1L, 20L, 1001L, 1002L, 32L, 2L, 16L, 24L};
        assertThat(page.getNumberOfElements()).isEqualTo(8);
        assertThat(page.getContent().stream().map(Comment::getId)).containsExactly(orders);
    }
}
