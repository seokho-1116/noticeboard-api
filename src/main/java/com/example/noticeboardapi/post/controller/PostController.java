package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.service.PostReadModel;
import com.example.noticeboardapi.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostReadModel postReadModel;

    @PostMapping("/post")
    public ResponseEntity<?> savePost(@RequestBody PostFormat postFormat) {
        Long postNumber = postService.savePost(postFormat);
        return ResponseEntity.created(URI.create("/post/"+postNumber)).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(postReadModel.getPosts(pageable));
    }

    @GetMapping("/posts/{postNo}")
    public ResponseEntity<?> getPost(@PathVariable Long postNo) {
        postService.addViewCount(postNo);
        return ResponseEntity.ok(postReadModel.getPost(postNo));
    }

    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<?> deletePost(@PathVariable Long postNo) {
        postService.deletePost(postNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

