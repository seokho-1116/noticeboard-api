package com.example.noticeboardapi.web.comment.advice;

import com.example.noticeboardapi.domain.comment.exception.NoSuchCommentException;
import com.example.noticeboardapi.web.comment.CommentController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = CommentController.class)
public class CommentAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<String> noSuchCommentExceptionHandle(NoSuchCommentException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> HttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}