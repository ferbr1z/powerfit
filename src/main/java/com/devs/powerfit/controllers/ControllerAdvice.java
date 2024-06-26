package com.devs.powerfit.controllers;

import com.devs.powerfit.dtos.errors.ErrorDto;
import com.devs.powerfit.exceptions.BaseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(HttpMessageNotReadableException e) {
        String message = e.getMessage();

        ErrorDto errorDto = ErrorDto.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message(message)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorDto errorDto = ErrorDto.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message(message)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExeptionHandler(RuntimeException e) {
        ErrorDto errorDto = ErrorDto.builder()
                .code("500")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> notFoundExceptionHandler(BaseException e) {
        ErrorDto errorDto = ErrorDto.builder()
                .code(e.getStatus().toString())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorDto, e.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();
                    String errorMessage = violation.getMessage();
                    return  field + ": " + errorMessage;
                })
                .collect(Collectors.joining("; "));

        ErrorDto errorDto = ErrorDto.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message(message)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

}

