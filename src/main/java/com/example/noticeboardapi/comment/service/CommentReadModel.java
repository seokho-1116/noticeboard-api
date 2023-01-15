package com.example.noticeboardapi.comment.service;

import com.example.noticeboardapi.comment.entity.Comment;
import com.example.noticeboardapi.comment.repository.CommentQueryRepository;
import com.example.noticeboardapi.comment.service.dto.CommentDto;
import com.example.noticeboardapi.comment.service.dto.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReadModel {

    private final CommentQueryRepository commentQueryRepository;
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    public Page<CommentDto> getPageOf20Comments(Long postNo, Pageable pageable) {
        Page<Comment> comments = commentQueryRepository.find20CommentsByPaging(postNo, pageable);
        return comments.map(commentMapper::fromComment);
    }

    public Page<CommentDto> getPageOf20CommentsContainingSpecificComment(Long postNo, CommentDto commentDto) {
        Page<Comment> comments = commentQueryRepository.findPageContainingSpecificComment(postNo, commentDto.getId());
        return comments.map(commentMapper::fromComment);
    }
}
