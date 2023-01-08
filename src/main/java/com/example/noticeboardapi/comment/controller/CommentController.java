package com.example.noticeboardapi.comment.controller;

import com.example.noticeboardapi.comment.repository.CommentJpaRepository;
import com.example.noticeboardapi.comment.service.CommentService;
import com.example.noticeboardapi.comment.service.dto.CommentDto;
import com.example.noticeboardapi.post.repository.PostJpaRepository;
import com.example.noticeboardapi.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentJpaRepository commentJpaRepository;

    @PostMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> savePost(Long postNo, CommentFormat commentFormat) {
        CommentDto commentDto = commentService.saveComment(postNo, commentFormat);
        return ResponseEntity.ok(commentDto);
    }
}
