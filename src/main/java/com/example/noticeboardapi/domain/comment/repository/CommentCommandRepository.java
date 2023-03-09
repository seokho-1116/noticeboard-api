package com.example.noticeboardapi.domain.comment.repository;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import com.example.noticeboardapi.domain.comment.exception.NoSuchCommentException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.records.CommentRecord;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.generated.test.tables.TreePath.TREE_PATH;

@Repository
@RequiredArgsConstructor
public class CommentCommandRepository {

    private final CommentQueryRepository commentQueryRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final DSLContext dslContext;

    public void deleteComment(Long postNo, Long commentNo) {
        CommentRecord commentRecord = getCommentRecord(postNo, commentNo);

        commentRecord.setText("삭제된 댓글 입니다.");
        commentRecord.store();
    }

    private CommentRecord getCommentRecord(Long postNo, Long commentNo) {
        CommentRecord commentRecord = dslContext.fetchOne(COMMENT, COMMENT.POST_ID.eq(postNo)
                .and(COMMENT.COMMENT_ID.eq(ULong.valueOf(commentNo))));

        if (commentRecord == null) {
            throw new NoSuchCommentException();
        }
        return commentRecord;
    }

    public void updateRecommendationCount(Long postNo, Long commentNo) {
        CommentRecord commentRecord = getCommentRecord(postNo, commentNo);

        commentRecord.setRecommendationCount(commentRecord.getRecommendationCount() + 1);
        commentRecord.store();
    }

    public void updateComment(Long postNo, Long commentNo, String text) {
        CommentRecord commentRecord = dslContext.fetchOne(COMMENT, COMMENT.POST_ID.eq(postNo)
                .and(COMMENT.COMMENT_ID.eq(ULong.valueOf(commentNo))));

        commentRecord.setText(text);
        commentRecord.store();
    }
}
