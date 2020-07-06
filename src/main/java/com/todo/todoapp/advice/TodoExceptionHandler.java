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
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class TodoExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleExceptions(Exception runtimeException, WebRequest webRequest) {
        StringBuilder stringBuilder = new StringBuilder("Error message: ");

        if (runtimeException instanceof DuplicateKeyException) {
            stringBuilder.append("A record with this key already exists!");
        } else if (runtimeException instanceof ConstraintViolationException) {
            stringBuilder.append("The given input is not valid!");
        }  else if (runtimeException instanceof AccessDeniedException) {
          stringBuilder.append("You don't have access to this operation!");
        } else {
            stringBuilder.append("An unexpected exception occurred!");
        }

        String errorMessage = stringBuilder.toString();
        LOGGER.error(errorMessage);

        return handleExceptionInternal(runtimeException, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
