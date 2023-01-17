package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.service.PostReadService;
import com.example.noticeboardapi.post.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostCommandService postCommandService;
    private final PostReadService postReadService;

    @PostMapping("/post")
    public ResponseEntity<?> postSave(@RequestBody PostFormat postFormat) {
        Long postNumber = postCommandService.savePost(postFormat);
        return ResponseEntity.created(URI.create("/post/"+postNumber)).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<?> postList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postReadService.find10Posts(pageable));
    }

    @GetMapping("/posts/{postNo}")
    public ResponseEntity<?> postDetails(@PathVariable Long postNo) {
        postCommandService.addViewCount(postNo);
        return ResponseEntity.ok(postReadService.findPost(postNo));
    }

    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<?> postDelete(@PathVariable Long postNo) {
        postCommandService.deletePost(postNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/posts/{postNo}/recommendation")
    public ResponseEntity<?> recommendationCountAdd(@PathVariable Long postNo) {
        postCommandService.addRecommendationCount(postNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

