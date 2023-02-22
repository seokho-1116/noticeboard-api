package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.TreePath;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
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

        Page<Comment> page = getCommentPage(postNo, pageable);
        Long[] orders = {6L, 378L, 280L, 396L, 943L, 676L,
                533L, 735L, 837L, 701L, 393L, 493L, 866L,
                16L, 148L, 522L, 867L, 882L, 195L, 221L};
        assertThat(page.getNumberOfElements()).isEqualTo(20);
        assertThat(page.getContent().stream().map(Comment::getId)).containsExactly(orders);
    }

    private Page<Comment> getCommentPage(Long postNo, Pageable pageable) {
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
                .leftOuterJoin(cc2).on(cc2.DESCENDANT.eq(c2.COMMENT_ID).and(cc2.DEPTH.eq(1)))
                .join(breadcrumb).on(cc1.DESCENDANT.eq(breadcrumb.DESCENDANT))
                .where(c1.COMMENT_ID.eq(ULong.valueOf(0L)))
                .groupBy(cc1.DESCENDANT)
                .orderBy(DSL.field("breadcrumbs"))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset() + 1)
                .fetchInto(Comment.class);

        int count = dslContext.fetchCount(COMMENT, COMMENT.POST_ID.eq(postNo));

        Page<Comment> page = new PageImpl<>(comments, pageable, count);
        return page;
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("특정 게시글 하위에 있는 특정 댓글에 페이지를 가져오기 테스트")
    void request20SpecificCommentUnderSpecificPostTest(Long postNo) {
        TreePath cc1 = new TreePath("cc1");
        TreePath cc2 = new TreePath("cc2");
        TreePath breadcrumb = new TreePath("breadcrumb");

        Long rowNum = dslContext
                .select(field("r.row_num"))
                .from(select(rowNumber().over(orderBy(field("breadcrumbs"))).as("row_num"),
                        cc1.DESCENDANT,
                        groupConcat(breadcrumb.ANCESTOR).orderBy(breadcrumb.DEPTH.desc()).as("breadcrumbs"))
                        .from(cc1)
                        .leftOuterJoin(cc2).on(cc2.DESCENDANT.eq(cc1.DESCENDANT).and(cc2.DEPTH.eq(1)))
                        .join(breadcrumb).on(cc1.DESCENDANT.eq(breadcrumb.DESCENDANT))
                        .where(cc1.ANCESTOR.eq(ULong.valueOf((0L))))
                        .groupBy(cc1.DESCENDANT)
                        .orderBy(field("breadcrumbs")).asTable("r"))
                .where("r.descendant = " + "800")
                .fetchOneInto(Long.class);

        int l = (int) (rowNum / 20);
        Page<Comment> commentPage = getCommentPage(postNo, PageRequest.of(l, 20));
        assertThat(commentPage.getNumberOfElements()).isEqualTo(20);
    }
}
