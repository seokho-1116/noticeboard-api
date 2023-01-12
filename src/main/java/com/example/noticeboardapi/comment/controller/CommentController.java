package com.example.noticeboardapi.comment.controller;

import com.example.noticeboardapi.comment.service.CommentReadModel;
import com.example.noticeboardapi.comment.service.CommentService;
import com.example.noticeboardapi.comment.service.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentReadModel commentReadModel;

    @PostMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> saveComment(@PathVariable Long postNo, @RequestBody CommentFormat commentFormat) {
        CommentDto commentDto = commentService.saveComment(postNo, commentFormat);
        return ResponseEntity.created(URI.create("/posts/"+postNo+"/comments")).body(commentDto);
    }

    @GetMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long postNo, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commentReadModel.get20Comments(postNo, pageable));
    }
}
