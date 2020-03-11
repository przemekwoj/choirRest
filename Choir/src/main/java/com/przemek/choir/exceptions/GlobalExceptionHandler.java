package com.przemek.choir.exceptions;

import com.przemek.choir.exceptions.particularExceptions.ChoristerNotFound;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errorMessage.append(fieldError.getDefaultMessage() + ", "));
        apiError.setMessage(errorMessage.toString());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Bad Http method, you should use " + ex.getSupportedHttpMethods().toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ChoristerNotFound.class)
    protected ResponseEntity<Object> handleChoristerNotFound(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"chorister not found");
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }
}
