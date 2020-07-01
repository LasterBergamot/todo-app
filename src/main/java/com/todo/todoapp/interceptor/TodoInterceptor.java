package com.todo.todoapp.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TodoInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        LOGGER.info("Status: {}, before: {} - {}", httpServletResponse.getStatus(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) {
        LOGGER.info("Status: {}, after: {} - {}", httpServletResponse.getStatus(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
    }
}
