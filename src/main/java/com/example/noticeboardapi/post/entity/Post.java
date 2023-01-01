package com.example.noticeboardapi.post.entity;

import com.example.noticeboardapi.comment.entity.Comment;
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
@Getter
@NoArgsConstructor
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
    private String title;
    private String text;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<PostFile> postFiles = new ArrayList<>();
    private LocalDateTime lastModifiedTime;
    private LocalDateTime createdTime;
    private Integer reportCount;
    private Integer viewCount;
    private Integer recommendationCount;

    public Post(String author, Category category, String title, String text, List<PostFile> postFiles) {
        this.author = author;
        this.category = category;
        this.text = text;
        this.postFiles.addAll(postFiles);
    }

    public static Post createPostByFormat(String author, Category category, String title,
                                          String text, List<PostFile> postFiles) {
        return new Post(author, category, title, text, postFiles);
    }
}
