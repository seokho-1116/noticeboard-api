package com.example.noticeboardapi.web.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateFormat {
    @NotEmpty
    private String title;

    @NotEmpty
    private String text;
}
