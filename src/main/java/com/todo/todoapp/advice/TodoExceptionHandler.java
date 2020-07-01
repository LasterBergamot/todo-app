package com.todo.todoapp.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
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

    private static Logger LOGGER = LoggerFactory.getLogger(TodoExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleExceptions(RuntimeException runtimeException, WebRequest webRequest) {
        StringBuilder stringBuilder = new StringBuilder("Message: An unexpected exception occurred!");

        if (runtimeException instanceof DuplicateKeyException) {
            stringBuilder = new StringBuilder("Message: A record with this key already exists!");
        } else if (runtimeException instanceof ConstraintViolationException) {
            stringBuilder = new StringBuilder("Message: The given input is not valid!");
        }

        stringBuilder.append("\n").append("Original message: ").append(runtimeException.getMessage());

        LOGGER.error(stringBuilder.toString());

        return handleExceptionInternal(runtimeException, stringBuilder.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
