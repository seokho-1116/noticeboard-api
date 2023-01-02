package com.example.noticeboardapi.post.service;

import com.example.noticeboardapi.common.FileStore;
import com.example.noticeboardapi.post.controller.PostFormat;
import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import com.example.noticeboardapi.post.repository.PostCommandRepository;
import com.example.noticeboardapi.post.repository.PostJpaRepository;
import com.example.noticeboardapi.post.repository.PostQueryRepository;
import com.example.noticeboardapi.post.service.dto.PostThumbnailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostCommandRepository postCommandRepository;
    private final FileStore fileStore;

    public Long savePost(PostFormat postFormat) {
        List<PostFile> attachFiles = fileStore.storeFiles(postFormat.getAttachFiles());
        Post post = Post.createPostByFormat(postFormat.getAuthor(), postFormat.getCategory(),
                postFormat.getText(), postFormat.getTitle(), attachFiles);
        Post savedPost = postJpaRepository.save(post);
        return savedPost.getId();
    }

    public void addViewCount(Long postNo) {
        postCommandRepository.updateViewCount(postNo);
    }
}
