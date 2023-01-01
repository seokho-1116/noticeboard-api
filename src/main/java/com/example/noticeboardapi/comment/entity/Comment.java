package com.example.noticeboardapi.comment.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "COMMENT")
public class Comment {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String author;
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Comment> child;

    private String text;
    private LocalDateTime lastModifiedTime;
    private LocalDateTime createdTime;
    private Integer reportCount;
    private Integer recommendationCount;
}
