package com.khokhlov.weather.error;

import com.khokhlov.weather.error.rest.AppError;
import com.khokhlov.weather.exception.InvalidLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(InvalidLoginException.class)
//    public ResponseEntity<AppError> handle(InvalidLoginException e) {
//        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception e, Model model) {
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }

}
