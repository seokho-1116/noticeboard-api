package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    private ResponseEntity<?> savePost(@RequestBody PostFormat postFormat) {
        Long postNumber = postService.savePost(postFormat);
        return ResponseEntity.created(URI.create("/post/"+postNumber)).build();
    }

    @GetMapping("/posts")
    private ResponseEntity<?> getPosts(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(pageable));
    }
}

