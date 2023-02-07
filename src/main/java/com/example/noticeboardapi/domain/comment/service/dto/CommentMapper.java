package com.example.noticeboardapi.domain.comment.service.dto;

import com.example.noticeboardapi.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto fromComment(Comment comment);
}
