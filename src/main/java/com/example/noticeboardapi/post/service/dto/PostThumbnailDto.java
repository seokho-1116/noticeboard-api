package com.example.noticeboardapi.post.service.dto;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.entity.PostFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostThumbnailDto {
    private Long id;
    private String author;
    private Category category;
    private String title;
    private String text;
    private List<PostFile> postFiles;

    public static PostThumbnailDto createPostThumbnailDto(Long postId, String author, Category category,
                                                          String title, String text, List<PostFile> postFiles) {
        return new PostThumbnailDto(postId, author, category, title, text, postFiles);
    }
}
