package com.stylight.seo.application;

import com.stylight.seo.domain.exception.UrlValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler implements ErrorController {

    public static final String SERVICE_ERROR = "Service error";

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleJsonMappingException(Exception exception) {
        log.debug(exception.getMessage(), exception);
        //Hide original error message
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorDto.builder().message(SERVICE_ERROR).build());
    }

    @ExceptionHandler(value = {UrlValidationException.class})
    public ResponseEntity handleUrlValidationException(UrlValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder().message(exception.getMessage()).build());
    }
}
