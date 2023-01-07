package com.example.noticeboardapi.comment.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COMMENT")
@Getter
public class Comment {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Column(name = "child")
    private List<Comment> child = new ArrayList<>();

    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;

    public static Comment createCommentByFormat(String author, String text, LocalDateTime createdTime) {
        return new Comment(author, text, createdTime);
    }
}
