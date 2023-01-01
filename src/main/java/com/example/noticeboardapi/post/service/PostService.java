package com.example.noticeboardapi.post.service;

import com.example.noticeboardapi.post.controller.PostFormat;
import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.repository.PostJpaRepository;
import com.example.noticeboardapi.post.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postJpaRepository;

    @Transactional
    public Long savePost(PostFormat postFormat) {
        Post post = Post.createPostByFormat(postFormat.getAuthor(), postFormat.getCategory(), postFormat.getText(),
                postFormat.getImageLink(), postFormat.getVideoLink());
        Post savedPost = postJpaRepository.save(post);
        return savedPost.getId();
    }
}
