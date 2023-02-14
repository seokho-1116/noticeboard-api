package com.example.noticeboardapi.web.post;

import com.example.noticeboardapi.domain.post.entity.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateFormat {
    @NotEmpty
    private String author;

    @NotNull
    private Category category;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;
}
