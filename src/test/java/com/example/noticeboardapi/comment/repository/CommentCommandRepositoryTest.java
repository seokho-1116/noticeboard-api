package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import com.example.noticeboardapi.comment.entity.TreePath;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@JooqTest
@ActiveProfiles("test")
public class CommentCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void commentDeleteTest(Long commentNo) {
        dslContext.update(COMMENT)
                .set(COMMENT.TEXT, "삭제된 댓글입니다.")
                .where(COMMENT.COMMENT_ID.eq(ULong.valueOf(commentNo)))
                .execute();

        Comment comment = dslContext.selectFrom(COMMENT)
                .where(COMMENT.COMMENT_ID.eq(ULong.valueOf(commentNo)))
                .fetchOneInto(Comment.class);

        assertThat(comment.getText()).isEqualTo("삭제된 댓글입니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void replyCommentCreateTest(Long postNo) {
        long parentCommentNo = 20L;

        Long commentNo = dslContext
                .insertInto(COMMENT, COMMENT.POST_ID, COMMENT.AUTHOR, COMMENT.TEXT, COMMENT.CREATED_TIME)
                .values(1L, "seokho", "hi", LocalDateTime.now())
                .returning(COMMENT.COMMENT_ID)
                .fetchOneInto(Long.class);

        dslContext.insertInto(TREE_PATH)
                .select(DSL.select(DSL.val(postNo), TREE_PATH.ANCESTOR, DSL.val(commentNo), TREE_PATH.DEPTH.plus(1))
                        .from(TREE_PATH)
                        .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(parentCommentNo)))
                        .unionAll(DSL.select(DSL.val(postNo), DSL.val(ULong.valueOf(commentNo)), DSL.val(commentNo), DSL.val(0L))))
                .execute();

        List<TreePath> treePaths = dslContext.selectFrom(TREE_PATH)
                .where(TREE_PATH.DESCENDANT.eq(ULong.valueOf(commentNo)))
                .fetchInto(TreePath.class);

        assertThat(commentNo).isEqualTo(1003);
        assertThat(treePaths.stream().map(TreePath::getAncestor)).containsExactly(0L, 992L, 20L, 1003L);
    }
}
