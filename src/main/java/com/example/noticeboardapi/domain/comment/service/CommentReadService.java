package com.example.noticeboardapi.domain.comment.service;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import com.example.noticeboardapi.domain.comment.repository.CommentQueryRepository;
import com.example.noticeboardapi.domain.comment.service.dto.CommentDto;
import com.example.noticeboardapi.domain.comment.service.dto.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReadService {

    private final CommentQueryRepository commentQueryRepository;
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    public Page<CommentDto> find20Comments(Long postNo, Pageable pageable) {
        Page<Comment> comments = commentQueryRepository.find20Comments(postNo, pageable);
        return comments.map(commentMapper::fromComment);
    }

    public Page<CommentDto> find20CommentsContainingSpecificComment(Long postNo, CommentDto commentDto) {
        Page<Comment> comments = commentQueryRepository.find20CommentsContainingSpecificComment(postNo, commentDto.getId());
        return comments.map(commentMapper::fromComment);
    }
}
