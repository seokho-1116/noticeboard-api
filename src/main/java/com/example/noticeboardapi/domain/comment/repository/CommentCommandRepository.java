package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import com.example.noticeboardapi.domain.comment.exception.NoSuchCommentExcpetion;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.UpdateSetMoreStep;
import org.jooq.generated.test.tables.records.CommentRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@Repository
@RequiredArgsConstructor
public class CommentCommandRepository {

    private CommentQueryRepository commentQueryRepository;
    private CommentJpaRepository commentJpaRepository;
    private DSLContext dslContext;

    public Comment saveReply(Comment comment, Long parentCommentNo) {
            Comment saveComment = commentJpaRepository.save(comment);

            dslContext.insertInto(TREE_PATH)
                    .select(DSL.select(DSL.val(comment.getPostId()), TREE_PATH.ANCESTOR,
                                    DSL.val(saveComment.getId()), TREE_PATH.DEPTH.plus(1))
                            .from(TREE_PATH)
                            .where(TREE_PATH.DESCENDANT.eq(parentCommentNo))
                        .unionAll(DSL.select(DSL.val(comment.getPostId()), DSL.val(saveComment.getId()),
                                DSL.val(saveComment.getId()), DSL.val(0)))
                ).execute();

        return saveComment;
    }

    public void deleteComment(Long postNo, Long commentNo) {
        CommentRecord commentRecord = dslContext.fetchOne(COMMENT, COMMENT.POST_ID.eq(postNo).and(COMMENT.COMMENT_ID.eq(commentNo)));

        if (commentRecord == null) {
            throw new NoSuchCommentExcpetion();
        }

        commentRecord.setText("삭제된 댓글 입니다.");
        commentRecord.store();
    }

    public void updateRecommendationCount(Long postNo, Long commentNo) {
        CommentRecord commentRecord = dslContext.fetchOne(COMMENT, COMMENT.POST_ID.eq(postNo).and(COMMENT.COMMENT_ID.eq(commentNo)));

        if (commentRecord == null) {
            throw new NoSuchCommentExcpetion();
        }

        commentRecord.setRecommendationCount(commentRecord.getRecommendationCount() + 1);
        commentRecord.store();
    }
}
