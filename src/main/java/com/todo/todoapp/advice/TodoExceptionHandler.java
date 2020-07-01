package com.todo.todoapp.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class TodoExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleUnexpectedExceptions(RuntimeException runtimeException, WebRequest webRequest) {
        return handleExceptionInternal(runtimeException, "An unexpected exception occurred!", new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationExceptions(RuntimeException runtimeException, WebRequest webRequest) {
        return handleExceptionInternal(runtimeException, "The given input is not valid!", new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
