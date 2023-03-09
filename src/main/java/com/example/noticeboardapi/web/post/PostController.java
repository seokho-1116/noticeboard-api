package com.example.noticeboardapi.web.post;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.service.PostReadService;
import com.example.noticeboardapi.domain.post.service.PostCommandService;
import com.example.noticeboardapi.domain.post.service.dto.PostDetailDto;
import com.example.noticeboardapi.domain.post.service.dto.PostThumbnailDto;
import io.r2dbc.spi.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostCommandService postCommandService;
    private final PostReadService postReadService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> postSave(@RequestPart @Validated PostCreateFormat postCreateFormat,
                                      @RequestPart(name = "file", required = false) List<MultipartFile> multipartFiles) {
        Long postNumber = postCommandService.savePost(postCreateFormat, multipartFiles);
        return ResponseEntity.created(URI.create("/posts/"+postNumber)).build();
    }

    @PutMapping(value = "/posts/{postNo}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> postUpdate(@PathVariable Long postNo,
                                        @RequestPart @Validated PostUpdateFormat postUpdateFormat,
                                        @RequestPart(name = "file", required = false) List<MultipartFile> multipartFiles) {
        postCommandService.updatePost(postNo, postUpdateFormat, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostThumbnailDto>> postList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postReadService.find10Posts(pageable));
    }

    @GetMapping("/posts/{postNo}")
    public ResponseEntity<PostDetailDto> postDetails(@PathVariable Long postNo) {
        postCommandService.addViewCount(postNo);
        return ResponseEntity.ok(postReadService.findPost(postNo));
    }

    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<Void> postDelete(@PathVariable Long postNo) {
        postCommandService.deletePost(postNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/posts/{postNo}/recommendation")
    public ResponseEntity<Void> recommendationCountAdd(@PathVariable Long postNo) {
        postCommandService.addRecommendationCount(postNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

