package com.stylight.seo.domain.exception;

public class UrlValidationException extends RuntimeException {

    public UrlValidationException() {
        super("Url can't be null");
    }
}
