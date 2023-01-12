package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.test.tables.Comment.COMMENT;

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
}
