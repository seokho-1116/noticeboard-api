package com.example.noticeboardapi.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    private String author;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @Enumerated(value = EnumType.STRING)
    private Category category;
    private String text;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private List<UploadFile> uploadFiles;
    private LocalDateTime lastModifiedTime;
    private LocalDateTime createdTime;
    private Integer reportCount;
    private Integer viewCount;
    private Integer recommendationCount;
}
