package com.example.noticeboardapi.comment.repository;

import com.example.noticeboardapi.comment.entity.Comment;
import org.jooq.DSLContext;
import org.jooq.Field;
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

import static org.jooq.generated.test.tables.Comment.COMMENT;
import static org.jooq.impl.DSL.multisetAgg;

@JooqTest
@ActiveProfiles("test")
public class CommentQueryRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    @ParameterizedTest
    @ValueSource(longs = {1L})
    @DisplayName("특정 게시글 하위에 있는 댓글 20개 페이징")
    void request20CommentUnderSpecificPost(Long postNo) {
        Pageable pageable = PageRequest.of(5,20);
        org.jooq.generated.test.tables.Comment c1 = new org.jooq.generated.test.tables.Comment("c1");
        org.jooq.generated.test.tables.Comment c2 = new org.jooq.generated.test.tables.Comment("c2");


        List<Comment> comments = dslContext
                .select(
                        c1.COMMENT_ID,
                        c1.TEXT,
                        c1.POST_ID,
                        multisetAgg(
                                c2.COMMENT_ID,
                                c2.TEXT
                        ).orderBy(c2.COMMENT_ID).as("child"))
                .from(c1)
                .join(c2)
                .on(c1.COMMENT_ID.eq(c2.PARENT_ID))
                .groupBy(c1.COMMENT_ID)
                .having(c1.POST_ID.eq(postNo))
                .fetchInto(Comment.class);

        int count = dslContext.fetchCount(COMMENT, COMMENT.POST_ID.eq(postNo));

        List<Comment> temp = new ArrayList<>(20);
        int elementCount = 0;
        for (Comment comment : comments) {
            elementCount += getCurrentCommentChildCount(comment);
            if (hasEnoughSpaceInPage(pageable, temp)) {
                if (isElementCountInRange(pageable, elementCount)) {
                    addAllCommentsWhenInRange(pageable, temp, comment);
                } else if (isElementCountGreaterThanRange(pageable, elementCount)) {
                    addAllCommentsWhenGreaterThanRange(pageable, temp, elementCount, comment);
                }
            } else {
                if (hasLastSpaceInPage(pageable, temp)) {
                    temp.add(comment);
                } else {
                    break;
                }
            }
        }

        Page<Comment> page = new PageImpl<>(temp, pageable, count);
        for (Comment comment : temp) {
            System.out.println("comment = " + comment);
        }
    }

    @Test
    void name() {
        org.jooq.generated.test.tables.Comment c1 = new org.jooq.generated.test.tables.Comment("c1");
        org.jooq.generated.test.tables.Comment c2 = new org.jooq.generated.test.tables.Comment("c2");

        Field<?>[] parent = {

        }
        List<Comment> comments = dslContext
                .select(
                        c2.COMMENT_ID,
                        c2.TEXT,
                        c1.POST_ID,
                        c2.PARENT_ID
                        )
                .from(c1)
                .join(c2)
                .on(c1.COMMENT_ID.eq(c2.PARENT_ID))
                .orderBy(c2.PARENT_ID,c1.COMMENT_ID)
                .limit(20)
                .offset(20)
                .fetchInto(Comment.class);

        for (Comment comment : comments) {
            System.out.println("comment.getParent() = " + comment.getParent());
            System.out.println("comment.getId() = " + comment.getId());
            System.out.println("comment.getAuthor() = " + comment.getAuthor());
        }
    }

    private void addAllCommentsWhenGreaterThanRange(Pageable pageable, List<Comment> temp, int elementCount, Comment comment) {
        if (isPreviousElementCountLessThanOffset(pageable, elementCount, comment)) {
            addAllComments(pageable, temp, comment, elementCount);
        } else if (hasFirstCommentOver20Child(elementCount, comment, pageable)) {
            addAllFirstComments(pageable, temp, comment);
        }
        addAllCommentsWhenInRange(pageable, temp, comment);
    }

    private void addAllFirstComments(Pageable pageable, List<Comment> temp, Comment comment) {
        temp.addAll(comment.getChild().subList((int) pageable.getOffset(),
                ((pageable.getPageNumber() * pageable.getPageSize()) + pageable.getPageSize())));
    }

    private static int getCurrentCommentChildCount(Comment comment) {
        return comment.getChild().size() + 1;
    }

    private static boolean isPreviousElementCountLessThanOffset(Pageable pageable, int elementCount, Comment comment) {
        return elementCount - getCurrentCommentChildCount(comment) < pageable.getOffset();
    }

    private boolean isElementCountGreaterThanRange(Pageable pageable, int elementCount) {
        return elementCount > ((pageable.getPageNumber() * pageable.getPageSize()) + pageable.getPageSize());
    }

    private static boolean hasLastSpaceInPage(Pageable pageable, List<Comment> temp) {
        return pageable.getPageSize() - temp.size() - 1 == 1;
    }

    private static void addAllComments(Pageable pageable, List<Comment> temp, Comment comment, int elementCount) {
        int offset = (int) pageable.getOffset() - (elementCount - getCurrentCommentChildCount(comment));
        temp.addAll(comment.getChild().subList(offset,
                pageable.getPageSize() - temp.size() + offset));
    }

    private static void addAllCommentsWhenInRange(Pageable pageable, List<Comment> temp, Comment comment) {
        if (isCommentsGreaterThanPageCapacity(pageable, temp, comment)) {
            temp.addAll(comment.getChild().subList(0, pageable.getPageSize() - temp.size()));
        } else {
            temp.addAll(comment.getChild().subList(0, pageable.getPageSize() - 1));
        }
    }

    private static boolean hasFirstCommentOver20Child(int elementCount, Comment comment, Pageable pageable) {
        return elementCount == comment.getChild().size() + 1 && pageable.getOffset() < getCurrentCommentChildCount(comment);
    }

    private static boolean isCommentsGreaterThanPageCapacity(Pageable pageable, List<Comment> temp, Comment comment) {
        return comment.getChild().size() >= pageable.getPageSize() - temp.size();
    }

    private static boolean hasEnoughSpaceInPage(Pageable pageable, List<Comment> temp) {
        return temp.size() < pageable.getPageSize() - 1;
    }
    
    private static boolean isElementCountInRange(Pageable pageable, int elementCount) {
        return elementCount > pageable.getOffset() &&
                elementCount < ((pageable.getPageNumber() * pageable.getPageSize()) + pageable.getPageSize());
    }
}
