package com.example.noticeboardapi.domain.comment.service;

import com.example.noticeboardapi.domain.comment.repository.TreePathCommandRepository;
import com.example.noticeboardapi.domain.comment.service.dto.CommentDto;
import com.example.noticeboardapi.web.comment.CommentCreateFormat;
import com.example.noticeboardapi.domain.comment.entity.Comment;
import com.example.noticeboardapi.domain.comment.repository.CommentCommandRepository;
import com.example.noticeboardapi.domain.comment.repository.CommentJpaRepository;
import com.example.noticeboardapi.domain.comment.service.dto.CommentMapper;
import com.example.noticeboardapi.web.comment.CommentUpdateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentCommandRepository commentCommandRepository;
    private final TreePathCommandRepository treePathCommandRepository;
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    public CommentDto saveComment(Long postNo, CommentCreateFormat commentCreateFormat) {
        Comment savedComment = getSavedComment(postNo, commentCreateFormat);

        treePathCommandRepository.saveCommentTreePath(postNo, savedComment.getId());

        return commentMapper.fromComment(savedComment);
    }

    private Comment getSavedComment(Long postNo, CommentCreateFormat commentCreateFormat) {
        return commentJpaRepository.save(
                Comment.createCommentByFormat(postNo, commentCreateFormat.getAuthor(),
                        commentCreateFormat.getText(), LocalDateTime.now())
        );
    }

    public CommentDto saveReply(Long postNo, Long parentCommentNo, CommentCreateFormat commentCreateFormat) {
        Comment savedComment = getSavedComment(postNo, commentCreateFormat);

        treePathCommandRepository.saveReplyTreePath(postNo, savedComment.getId(), parentCommentNo);

        return commentMapper.fromComment(savedComment);
    }

    public void deleteComment(Long postNo, Long commentNo) {
        commentCommandRepository.deleteComment(postNo, commentNo);
    }

    public void addRecommendationCount(Long postNo, Long commentNo) {
        commentCommandRepository.updateRecommendationCount(postNo, commentNo);
    }

    public void updateComment(Long postNo, Long commentNo, CommentUpdateFormat commentUpdateFormat) {
        commentCommandRepository.updateComment(postNo, commentNo, commentUpdateFormat.getText());
    }
}
