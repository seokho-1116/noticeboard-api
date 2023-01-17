package com.example.noticeboardapi.comment.controller;

import com.example.noticeboardapi.comment.service.CommentReadService;
import com.example.noticeboardapi.comment.service.CommentCommandService;
import com.example.noticeboardapi.comment.service.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentReadService commentReadService;

    @PostMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> commentSave(@PathVariable Long postNo, @RequestBody CommentFormat commentFormat) {
        CommentDto commentDto = commentCommandService.saveComment(postNo, commentFormat);
        return ResponseEntity.created(URI.create("/posts/" + postNo + "/comments"))
                .body(commentReadService.find20CommentsContainingSpecificComment(postNo, commentDto));
    }

    @PostMapping("/posts/{postNo}/comments/{parentCommentId}/comments")
    public ResponseEntity<?> replySave(@PathVariable Long postNo, @PathVariable Long parentCommentId,
                                       @RequestBody CommentFormat commentFormat) {
        CommentDto commentDto = commentCommandService.saveReply(postNo, parentCommentId, commentFormat);
        return ResponseEntity.created(URI.create("/posts/" + postNo + "/comments/" + parentCommentId + "/comments"))
                .body(commentReadService.find20CommentsContainingSpecificComment(postNo, commentDto));
    }

    @GetMapping("/posts/{postNo}/comments")
    public ResponseEntity<?> commentList(@PathVariable Long postNo, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commentReadService.find20Comments(postNo, pageable));
    }

    @DeleteMapping("/posts/{postNo}/comments/{commentNo}")
    public ResponseEntity<?> commentDelete(@PathVariable Long postNo, @PathVariable Long commentNo) {
        commentCommandService.deleteComment(postNo, commentNo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/{postNo}/comments/{commentNo}/recommendation")
    public ResponseEntity<?> recommendationCountAdd(@PathVariable Long postNo, @PathVariable Long commentNo) {
        commentCommandService.addRecommendationCount(postNo, commentNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
