package com.example.noticeboardapi.comment.service;

import com.example.noticeboardapi.comment.controller.CommentFormat;
import com.example.noticeboardapi.comment.entity.Comment;
import com.example.noticeboardapi.comment.repository.CommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;

    public Long saveComment(CommentFormat commentFormat) {
        Comment savedComment = commentJpaRepository.save(
                Comment.createCommentByFormat(commentFormat.getAuthor(), commentFormat.getText(), LocalDateTime.now())
        );
        return savedComment.getId();
    }
}
