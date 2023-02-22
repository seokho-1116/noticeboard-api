package com.example.noticeboardapi.web.post.advice;

import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import com.example.noticeboardapi.web.common.log.RequestResponseLogFormatter;
import com.example.noticeboardapi.web.post.PostController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice(assignableTypes = PostController.class)
@Slf4j
public class PostAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<String> noSuchPostExceptionHandle(NoSuchPostException e, HttpServletRequest request) throws IOException {
        writeLog(e, request);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void writeLog(Exception e, HttpServletRequest request) {
        String formattedLog = RequestResponseLogFormatter.formattingException(request, e);
        log.error("HANDLE {}", formattedLog);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) throws IOException {
        writeLog(e, request);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> missingServletRequestPartException(MissingServletRequestPartException e, HttpServletRequest request) throws IOException {
        writeLog(e, request);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
