package com.example.noticeboardapi.domain.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "author")
    private String author;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<PostFile> postFiles = new ArrayList<>();

    public Post(String author, Category category, String title, String text, List<PostFile> postFiles) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.text = text;
        this.postFiles = postFiles;
    }

    public static Post createPostByFormat(String author, Category category, String title,
                                          String text, List<PostFile> postFiles) {
        return new Post(author, category, title, text, postFiles);
    }
}
