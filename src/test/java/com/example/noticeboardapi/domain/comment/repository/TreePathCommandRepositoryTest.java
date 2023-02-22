package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.TreePath;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.Comment;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;
import static org.junit.jupiter.api.Assertions.*;

@JooqTest
@ActiveProfiles("test")
class TreePathCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    private final static long ROOT_COMMENT_ID = 0;

    @Test
    @DisplayName("댓글 트리 저장 테스트")
    void saveCommentTreePathTest() {
        Long postNo = 1L;
        Long commentNo = 1001L;

        dslContext.insertInto(COMMENT, COMMENT.COMMENT_ID, COMMENT.POST_ID)
                .values(ULong.valueOf(1001), 1L)
                .execute();

        dslContext.insertInto(TREE_PATH, TREE_PATH.POST_ID, TREE_PATH.ANCESTOR, TREE_PATH.DESCENDANT, TREE_PATH.DEPTH)
                .values(postNo, ULong.valueOf(commentNo), ULong.valueOf(commentNo), 0)
                .values(postNo, ULong.valueOf(ROOT_COMMENT_ID), ULong.valueOf(commentNo), 1)
                .execute();

        List<TreePath> treePaths = dslContext.selectFrom(TREE_PATH)
                .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(commentNo)))
                .fetchInto(TreePath.class);

        Assertions.assertThat(treePaths.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("대댓글 트리 저장 테스트")
    void saveReplyTreePathTest() {
        Long postNo = 1L;
        Long parentCommentNo = 1L;
        Long commentNo = 1001L;

        dslContext.insertInto(COMMENT, COMMENT.COMMENT_ID, COMMENT.POST_ID)
                .values(ULong.valueOf(1001), 1L)
                .execute();

        dslContext.insertInto(TREE_PATH)
                .select(DSL.select(DSL.val(postNo), TREE_PATH.ANCESTOR,
                                DSL.val(commentNo), TREE_PATH.DEPTH.plus(1))
                        .from(TREE_PATH)
                        .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(parentCommentNo)))
                        .unionAll(DSL.select(DSL.val(postNo), DSL.val(ULong.valueOf(commentNo)),
                                DSL.val(commentNo), DSL.val(0)))
                ).execute();

        //0 -> 894 -> 368 -> 1 -> 1001
        List<TreePath> treePaths = dslContext.selectFrom(TREE_PATH)
                .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(commentNo)))
                .orderBy(TREE_PATH.DEPTH)
                .fetchInto(TreePath.class);

        Long[] orders = {1001L, 1L, 368L, 894L, 0L};
        assertThat(treePaths.stream().map(TreePath::getAncestor)).containsExactly(orders);
    }
}