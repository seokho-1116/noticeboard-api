package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.service.PostService;
import com.example.noticeboardapi.post.service.dto.PostDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}

