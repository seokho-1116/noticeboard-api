package com.example.noticeboardapi.web.common;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private String description;

    private ErrorResponse(String errorCode, String errorMessage, String description) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.description = description;
    }

    public static ErrorResponse createErrorResponse(String errorCode, String errorMessage,
                                                    String description) {
        return new ErrorResponse(errorCode, errorMessage, description);
    }
}
