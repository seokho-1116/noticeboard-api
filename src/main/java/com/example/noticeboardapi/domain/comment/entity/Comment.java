package com.example.noticeboardapi.domain.comment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COMMENT")
@Getter
@NoArgsConstructor
public class Comment implements Serializable {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;

    public Comment(Long postId, String author, String text, LocalDateTime createdTime) {
        this.postId = postId;
        this.author = author;
        this.text = text;
        this.createdTime = createdTime;
    }

    public static Comment createCommentByFormat(Long postId, String author, String text, LocalDateTime createdTime) {
        return new Comment(postId, author, text, createdTime);
    }
}
