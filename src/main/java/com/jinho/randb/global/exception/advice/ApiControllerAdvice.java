package com.jinho.randb.global.exception.advice;

import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.exception.ex.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> ServerError (ServerErrorException ex){
        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> AccessDenied(AccessDeniedException e){
        ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> Forbidden(ForbiddenException e){
        ErrorResponse response = new ErrorResponse<>(false, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


}
