package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.entity.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFormat {
    private String author;
    private Category category;
    private String title;
    private String text;
    private String imageLink;
    private String videoLink;

    public static PostFormat createPostFormat(String author, Category category, String title,
                                              String text, String imageLink, String videoLink) {
        return new PostFormat(author, category, title, text, imageLink, videoLink);
    }
}
