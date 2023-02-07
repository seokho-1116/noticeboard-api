package com.example.noticeboardapi.domain.post.service.dto;

import com.example.noticeboardapi.domain.post.entity.PostFile;
import com.example.noticeboardapi.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostThumbnailDto {
    private Long id;
    private String author;
    private Category category;
    private String title;
    private LocalDateTime createdTime;
    private Integer recommendationCount;
    private List<PostFile> postFiles;


    public PostThumbnailDto(Record r, PostThumbnailDto postThumbnailDto) {

    }


    public static PostThumbnailDto createPostThumbnailDto(Long postId, String author, Category category,
                                                          String title, LocalDateTime createdTime,
                                                          Integer recommendationCount, List<PostFile> postFiles) {
        return new PostThumbnailDto(postId, author, category, title, createdTime, recommendationCount, postFiles);
    }
}
