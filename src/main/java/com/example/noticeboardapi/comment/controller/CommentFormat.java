package com.example.noticeboardapi.comment.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentFormat {
    private String author;
    private String text;

    public CommentFormat(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
