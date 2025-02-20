package com.khokhlov.weather.error.rest;

import com.khokhlov.weather.exception.InvalidLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<AppError> handle(InvalidLoginException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public AppError handle(Exception e) {
        return new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

}