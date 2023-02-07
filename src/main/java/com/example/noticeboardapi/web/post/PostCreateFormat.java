package com.example.noticeboardapi.web.post;

import com.example.noticeboardapi.domain.post.entity.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateFormat {
    @NotBlank
    private String author;

    @NotNull
    private Category category;

    @NotBlank
    private String title;

    @NotNull
    private String text;
}
