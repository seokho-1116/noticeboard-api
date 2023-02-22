package com.example.noticeboardapi.domain.comment.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@Repository
@RequiredArgsConstructor
public class TreePathCommandRepository {
    private final DSLContext dslContext;
    private final static long ROOT_COMMENT_ID = 0;

    public void saveCommentTreePath(Long postNo, Long commentNo) {
        dslContext.insertInto(TREE_PATH, TREE_PATH.POST_ID, TREE_PATH.ANCESTOR, TREE_PATH.DESCENDANT, TREE_PATH.DEPTH)
                .values(postNo, ULong.valueOf(commentNo), ULong.valueOf(commentNo), 0)
                .values(postNo, ULong.valueOf(ROOT_COMMENT_ID), ULong.valueOf(commentNo), 1)
                .execute();
    }

    public void saveReplyTreePath(Long postNo, Long commentNo, Long parentCommentNo) {
        dslContext.insertInto(TREE_PATH)
                .select(DSL.select(DSL.val(postNo), TREE_PATH.ANCESTOR,
                                DSL.val(commentNo), TREE_PATH.DEPTH.plus(1))
                        .from(TREE_PATH)
                        .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(parentCommentNo)))
                        .unionAll(DSL.select(DSL.val(postNo), DSL.val(ULong.valueOf(commentNo)),
                                DSL.val(commentNo), DSL.val(0)))
                ).execute();
    }
}
