package com.khokhlov.weather.error.rest;

import com.khokhlov.weather.exception.InvalidLoginOrPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(InvalidLoginOrPasswordException.class)
    public ResponseEntity<AppError> handle(InvalidLoginOrPasswordException e) {
        log.error("ExceptionHandler caught login or password exception (REST): {}", e.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public AppError handle(Exception e) {
        log.error("ExceptionHandler caught indefinite exception (REST): {}", e.getMessage());
        return new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

}