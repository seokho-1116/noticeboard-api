package com.example.noticeboardapi.domain.post.service.dto;

import com.example.noticeboardapi.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostThumbnailDto fromPostToThumbnail(Post post);
    PostDetailDto fromPostToDetail(Post post);
}
