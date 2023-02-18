package com.example.noticeboardapi.web.post.advice;

import com.example.noticeboardapi.domain.comment.exception.NoSuchCommentExcpetion;
import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class PostAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchCommentExcpetion.class)
    public ResponseEntity<String> noSuchPostExceptionHandle(NoSuchPostException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> missingServletRequestPartException(MissingServletRequestPartException e, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
