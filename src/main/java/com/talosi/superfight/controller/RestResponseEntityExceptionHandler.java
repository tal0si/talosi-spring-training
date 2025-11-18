package com.talosi.superfight.controller;

import com.talosi.superfight.exception.HttpRequestException;
import com.talosi.superfight.exception.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                ErrorDto.builder()
                        .errorMessage("An error occurred")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .build(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { HttpRequestException.class })
    protected ResponseEntity<Object> handleConflict(HttpRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                ErrorDto.builder()
                        .errorMessage(ex.getMessage())
                        .status(ex.getStatus().toString())
                        .build()
                ,  new HttpHeaders(), ex.getStatus(), request);
    }

}
