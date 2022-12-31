package com.example.noticeboardapi.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue(strategy = IDENTITY)
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
    @JoinColumn(name = "post_id")
    private List<PostFile> postFiles;
    private LocalDateTime lastModifiedTime;
    private LocalDateTime createdTime;
    private Integer reportCount;
    private Integer viewCount;
    private Integer recommendationCount;
}
