package com.example.weather_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice //makes handler available to controller
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class) //tells spring which exception to handle
    @ResponseStatus(HttpStatus.BAD_REQUEST) // changes response status to 400
    public Map<String, String> handleIllegalArgument(
        IllegalArgumentException exception){
            return Map.of("error", exception.getMessage()); //return custom error message
        }

}
