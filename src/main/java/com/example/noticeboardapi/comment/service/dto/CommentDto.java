package com.example.noticeboardapi.comment.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String author;
    private String text;
    private LocalDateTime createdTime;
}
