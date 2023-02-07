package com.example.noticeboardapi.domain.comment.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TREE_PATH")
@Getter
public class TreePath {
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "ancestor")
    private Long ancestor;

    @Column(name = "descendant")
    private Long descendant;

    @Column(name = "depth")
    private Long depth;
}
