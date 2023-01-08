package com.example.noticeboardapi.comment.service.dto;

import com.example.noticeboardapi.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto fromComment(Comment comment);
}
