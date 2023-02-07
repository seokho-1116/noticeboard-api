package com.example.noticeboardapi.web.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateFormat {
    @NotEmpty
    private String author;

    @NotEmpty
    private String text;

    public CommentCreateFormat(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
