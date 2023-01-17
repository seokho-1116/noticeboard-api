package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import com.example.noticeboardapi.comment.entity.TreePath;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.generated.test.tables.records.CommentRecord;
import org.jooq.impl.DSL;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@JooqTest
@ActiveProfiles("test")
public class CommentCommandRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void commentDeleteTest(Long commentNo) {
        Long postNo = 1L;
        dslContext.update(COMMENT)
                .set(COMMENT.TEXT, "삭제된 댓글입니다.")
                .where(COMMENT.POST_ID.eq(postNo).and(COMMENT.COMMENT_ID.eq(commentNo)))
                .execute();

        Comment comment = dslContext.selectFrom(COMMENT)
                .where(COMMENT.COMMENT_ID.eq(commentNo))
                .fetchOneInto(Comment.class);

        assertThat(comment.getText()).isEqualTo("삭제된 댓글입니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void replySaveTest(Long postNo) {
        long parentCommentNo = 20L;

        CommentRecord commentRecord = dslContext
                .insertInto(COMMENT, COMMENT.COMMENT_ID, COMMENT.POST_ID, COMMENT.AUTHOR, COMMENT.TEXT, COMMENT.CREATED_TIME)
                .values(1003L, 1L, "seokho", "hi", LocalDateTime.now())
                .returning(COMMENT.COMMENT_ID)
                .fetchOne();
        Long commentNo = (Long) commentRecord.getValue("comment_id");

        dslContext.insertInto(TREE_PATH)
                .select(DSL.select(DSL.val(postNo), TREE_PATH.ANCESTOR, DSL.val(commentNo), TREE_PATH.DEPTH.plus(1))
                        .from(TREE_PATH)
                        .where(TREE_PATH.DESCENDANT.eq(parentCommentNo))
                        .unionAll(DSL.select(DSL.val(postNo), DSL.val(commentNo), DSL.val(commentNo), DSL.val(0))))
                .execute();

        List<TreePath> treePaths = dslContext
                .select(TREE_PATH.asterisk())
                .from(TREE_PATH)
                .where(TREE_PATH.DESCENDANT.eq(commentNo))
                .orderBy(TREE_PATH.DEPTH.desc())
                .fetchInto(TreePath.class);

        assertThat(commentNo.intValue()).isEqualTo(1003);
        assertThat(treePaths.stream().map(TreePath::getAncestor)).containsExactly(0L, 621L, 445L, 20L, 1003L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void updateRecommendationCountTest(Long postNo) {
        Long commentNo = 1L;
        Comment before = getComment(postNo, commentNo);

        dslContext.update(COMMENT)
                .set(COMMENT.RECOMMENDATION_COUNT, COMMENT.RECOMMENDATION_COUNT.plus(1))
                .where(COMMENT.POST_ID.eq(postNo).and(COMMENT.COMMENT_ID.eq(commentNo)))
                .execute();

        Comment after = getComment(postNo, commentNo);

        assertThat(after.getRecommendationCount()).isEqualTo(before.getRecommendationCount() + 1);
    }

    private Comment getComment(Long postNo, Long commentNo) {
        return dslContext.selectFrom(COMMENT)
                .where(COMMENT.POST_ID.eq(postNo).and(COMMENT.COMMENT_ID.eq(commentNo)))
                .fetchOneInto(Comment.class);
    }
}
