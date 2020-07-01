package com.todo.todoapp.config;

import com.todo.todoapp.interceptor.TodoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class TodoInterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    TodoInterceptor todoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(todoInterceptor);
    }
}
