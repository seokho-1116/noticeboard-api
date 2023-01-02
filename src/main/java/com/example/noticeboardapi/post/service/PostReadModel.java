package com.example.noticeboardapi.post.service;

import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.repository.PostQueryRepository;
import com.example.noticeboardapi.post.service.dto.PostDetailDto;
import com.example.noticeboardapi.post.service.dto.PostMapper;
import com.example.noticeboardapi.post.service.dto.PostThumbnailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadModel {

    private final PostQueryRepository postQueryRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    public Page<PostThumbnailDto> getPosts(Pageable pageable) {
        Page<Post> posts = postQueryRepository.find10PostsByPaging(pageable);
        return posts.map(postMapper::fromPostToThumbnail);
    }

    public PostDetailDto getPost(Long postNo) {
        Post post = postQueryRepository.findPostById(postNo);
        return postMapper.fromPostToDetail(post);
    }
}
