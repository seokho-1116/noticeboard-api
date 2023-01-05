package com.example.noticeboardapi.post.service.dto;

import com.example.noticeboardapi.post.entity.Category;
import com.example.noticeboardapi.post.entity.PostFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostThumbnailDto {
    private Long id;
    private String author;
    private Category category;
    private String title;
    private LocalDateTime createdTime;
    private Integer recommendationCount;
    private List<PostFile> postFiles;

    public static PostThumbnailDto createPostThumbnailDto(Long postId, String author, Category category,
                                                          String title, LocalDateTime createdTime,
                                                          Integer recommendationCount, List<PostFile> postFiles) {
        return new PostThumbnailDto(postId, author, category, title, createdTime, recommendationCount, postFiles);
    }
}
