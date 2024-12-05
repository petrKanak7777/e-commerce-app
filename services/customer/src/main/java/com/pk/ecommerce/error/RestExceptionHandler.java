package com.pk.ecommerce.error;

import com.pk.ecommerce.error.exception.InvalidArgumentException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleError404(NoHandlerFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,
                "Requested resource does not exist", HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIO(IOException e) {
        logger.error("Exception Caused By: ", e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred in IO streams.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException e) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED,
                "Access denied.", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidArgumentException(InvalidArgumentException e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
                e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
                e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,
                e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException e) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleError(Exception e) {
        logger.error("Exception Caused By: ", e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                e.getClass().getName() + " " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}