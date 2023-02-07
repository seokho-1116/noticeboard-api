package com.example.noticeboardapi.web.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        String method = request.getMethod();
        String userAddr = request.getRemoteAddr();

        request.setAttribute(LOG_ID, uuid);

        log.info("{} [{}][{}][{}][{}]", method, userAddr, uuid, requestURI, handler);
        return true;
    }
}
