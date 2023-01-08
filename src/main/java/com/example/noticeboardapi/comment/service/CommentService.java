package com.example.noticeboardapi.comment.service;

import com.example.noticeboardapi.comment.controller.CommentFormat;
import com.example.noticeboardapi.comment.entity.Comment;
import com.example.noticeboardapi.comment.repository.CommentJpaRepository;
import com.example.noticeboardapi.comment.service.dto.CommentDto;
import com.example.noticeboardapi.comment.service.dto.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    public CommentDto saveComment(Long postNo, CommentFormat commentFormat) {
        Comment savedComment = commentJpaRepository.save(
                Comment.createCommentByFormat(postNo, commentFormat.getAuthor(), commentFormat.getText(), LocalDateTime.now())
        );
        return commentMapper.fromComment(savedComment);
    }
}
