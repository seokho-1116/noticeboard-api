package com.example.noticeboardapi.web.post.advice;

import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import com.example.noticeboardapi.web.common.ErrorResponse;
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

@RestControllerAdvice(assignableTypes = PostController.class)
@Slf4j
public class PostAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ErrorResponse> noSuchPostExceptionHandle(NoSuchPostException e,
                                                                   HttpServletRequest request) {
        writeLog(e, request);
        return getResponseEntity(e, HttpStatus.NOT_FOUND, "please enter a different post number");
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
        return getResponseEntity(e, HttpStatus.BAD_REQUEST,
                "Check the validation conditions for the entered values"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestPartException(MissingServletRequestPartException e,
                                                                            HttpServletRequest request) {
        writeLog(e, request);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST, "Required request part is incorrect or empty.");
    }
}
