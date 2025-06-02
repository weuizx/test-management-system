package org.evilincorporated.pineapple.controller;

import jakarta.persistence.EntityNotFoundException;
import org.evilincorporated.pineapple.controller.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException exception) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(httpStatus).body(apiError(httpStatus, exception.getMessage()));
    }

    private ApiError apiError(HttpStatus httpStatus, String description) {
        return new ApiError(
                httpStatus.name(),
                httpStatus.getReasonPhrase(),
                description);
    }
}
