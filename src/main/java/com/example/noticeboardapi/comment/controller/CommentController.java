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
    public ResponseEntity<?> commentSave(@PathVariable Long postNo, @RequestBody CommentFormat commentFormat) {
        CommentDto commentDto = commentService.saveComment(postNo, commentFormat);
        return ResponseEntity.created(URI.create("/posts/" + postNo + "/comments"))
                .body(commentReadModel.getPageOf20CommentsContainingSpecificComment(postNo, commentDto));
    }

    @PostMapping("/posts/{postNo}/comments/{parentCommentId}/comments")
    public ResponseEntity<?> replySave(@PathVariable Long postNo, @PathVariable Long parentCommentId,
                                       @RequestBody CommentFormat commentFormat) {
        CommentDto commentDto = commentService.saveReply(postNo, parentCommentId, commentFormat);
        return ResponseEntity.created(URI.create("/posts/" + postNo + "/comments/" + parentCommentId + "/comments"))
                .body(commentReadModel.getPageOf20CommentsContainingSpecificComment(postNo, commentDto));
    }

    @GetMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> commentList(@PathVariable Long postNo, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commentReadModel.getPageOf20Comments(postNo, pageable));
    }

    @DeleteMapping("/posts/{postNo}/comments/{commentNo}")
    public ResponseEntity<?> commentDelete(@PathVariable Long postNo, @PathVariable Long commentNo) {
        commentService.deleteComment(postNo, commentNo);
        return ResponseEntity.ok().build();
    }
}
