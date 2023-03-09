package com.example.noticeboardapi.web.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateFormat {
    @NotEmpty
    private String text;
}
