package com.example.noticeboardapi.post.controller;

import com.example.noticeboardapi.post.entity.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostFormat {
    private String author;
    private Category category;
    private String title;
    private String text;
    private List<MultipartFile> attachFiles;

    public PostFormat(String author, Category category, String title, String text, List<MultipartFile> attachFiles) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.text = text;
        this.attachFiles = attachFiles;
    }

    public static PostFormat createPostFormat(String author, Category category,
                                              String title, String text, List<MultipartFile> attachFiles) {
        return new PostFormat(author, category, title, text, attachFiles);
    }
}
