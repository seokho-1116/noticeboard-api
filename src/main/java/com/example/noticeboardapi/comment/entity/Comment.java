package com.example.noticeboardapi.comment.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COMMENT")
@Getter
public class Comment {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Column(name = "child")
    private List<Comment> child;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;
}
