package com.example.noticeboardapi.web.common.log;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RequestResponseLogFormatter {

    public static String formatting(HttpServletRequest request) {
        String uuid = getLogId(request);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            request.setAttribute("logId", uuid);
        }

        return String.format("[%s] %s [%s]", uuid, request.getMethod(), request.getRequestURI());
    }

    private static String getLogId(HttpServletRequest request) {
        return (String) request.getAttribute("logId");
    }

    public static String formattingException(HttpServletRequest request, Exception e) {
        String uuid = getLogId(request);
        return String.format("[%s][%s][%s] %s [%s][request body: %s]", uuid, e.getClass().getSimpleName(), e.getMessage(),
                request.getMethod(), request.getRequestURI(), getRequestBody(request));
    }

    public static String formattingWithResponseBody(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return String.format("%s[response body: %s]", formatting(request),getResponseBody(response));
    }

    private static String getResponseBody(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = getResponseWrapper(response);
        String result = new String(responseWrapper.getContentAsByteArray());
        responseWrapper.copyBodyToResponse();
        return result;
    }

    private static ContentCachingResponseWrapper getResponseWrapper(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        }
        return new ContentCachingResponseWrapper(response);
    }

    public static String formattingWithRequestBody(HttpServletRequest request) throws IOException {
        return String.format("%s[request body: %s]", formatting(request), getRequestBody(request));
    }


    private static String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper requestWrapper = getRequestWrapper(request);
        return new String(requestWrapper.getContentAsByteArray());
    }

    private static ContentCachingRequestWrapper getRequestWrapper(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return  (ContentCachingRequestWrapper) request;
        }
        return new ContentCachingRequestWrapper(request);
    }
}
