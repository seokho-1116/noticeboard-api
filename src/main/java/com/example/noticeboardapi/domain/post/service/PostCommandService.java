package com.example.noticeboardapi.domain.post.service;

import com.example.noticeboardapi.domain.common.file.FileStore;
import com.example.noticeboardapi.web.post.PostCreateFormat;
import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import com.example.noticeboardapi.domain.post.repository.PostCommandRepository;
import com.example.noticeboardapi.domain.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {

    private final PostJpaRepository postJpaRepository;
    private final PostCommandRepository postCommandRepository;
    private final FileStore fileStore;

    public Long savePost(PostCreateFormat postCreateFormat, List<MultipartFile> multipartFiles) {
        List<PostFile> postFiles = new ArrayList<>();
        if (!multipartFiles.isEmpty()) {
            postFiles.addAll(fileStore.storeFiles(multipartFiles));
        }
        Post post = Post.createPostByFormat(postCreateFormat.getAuthor(), postCreateFormat.getCategory(),
                postCreateFormat.getText(), postCreateFormat.getTitle(), postFiles);
        Post savedPost = postJpaRepository.save(post);
        return savedPost.getId();
    }

    public void addViewCount(Long postNo) {
        postCommandRepository.updateViewCount(postNo);
    }

    public void addRecommendationCount(Long postNo) {
        postCommandRepository.updateRecommendationCount(postNo);
    }

    public void deletePost(Long postNo) {
        postCommandRepository.deletePost(postNo);
    }
}
