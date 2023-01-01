package com.example.noticeboardapi.post.service;

import com.example.noticeboardapi.common.FileStore;
import com.example.noticeboardapi.post.controller.PostFormat;
import com.example.noticeboardapi.post.entity.Post;
import com.example.noticeboardapi.post.entity.PostFile;
import com.example.noticeboardapi.post.repository.PostJpaRepository;
import com.example.noticeboardapi.post.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final FileStore fileStore;

    @Transactional
    public Long savePost(PostFormat postFormat) {
        List<PostFile> attachFiles = fileStore.storeFiles(postFormat.getAttachFiles());
        Post post = Post.createPostByFormat(postFormat.getAuthor(), postFormat.getCategory(),
                postFormat.getText(), postFormat.getTitle(), attachFiles);
        Post savedPost = postJpaRepository.save(post);
        return savedPost.getId();
    }
}
