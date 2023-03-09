package com.example.noticeboardapi.web.comment.advice;

import com.example.noticeboardapi.domain.comment.exception.NoSuchCommentException;
import com.example.noticeboardapi.web.comment.CommentController;
import com.example.noticeboardapi.web.common.log.RequestResponseLogFormatter;
import com.example.noticeboardapi.web.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = CommentController.class)
@Slf4j
public class CommentAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<ErrorResponse> noSuchCommentExceptionHandle(NoSuchCommentException e,
                                                                      HttpServletRequest request) {
        writeLog(e, request);
        return getResponseEntity(e, HttpStatus.NOT_FOUND, "Please enter a different comment number");
    }

    private void writeLog(Exception e, HttpServletRequest request) {
        String formattedLog = RequestResponseLogFormatter.formattingException(request, e);
        log.error("HANDLE {}", formattedLog);
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(Exception e, HttpStatus status, String description) {
        return new ResponseEntity<>(ErrorResponse.createErrorResponse(status.toString(), e.getMessage(), description),
                status
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                         HttpServletRequest request) {
        writeLog(e, request);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST, "Check the validation conditions for the entered values");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> HttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                         HttpServletRequest request) {
        writeLog(e, request);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST, "Request body is incorrect or empty.");
    }
}