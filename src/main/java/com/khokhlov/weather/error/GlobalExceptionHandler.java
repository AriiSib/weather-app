package com.khokhlov.weather.error;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handle(Exception e, Model model) {
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }

}
