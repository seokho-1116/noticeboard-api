package com.example.noticeboardapi.web.common.interceptor;

import com.example.noticeboardapi.web.common.log.RequestResponseLogFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String formattedLog = RequestResponseLogFormatter.formatting(request);
        log.info("REQUEST {}[{}]", formattedLog, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String formattedLog = RequestResponseLogFormatter.formattingWithResponseBody(request, response);
        log.info("RETRIEVE {}[{}]", formattedLog, handler);
    }
}
