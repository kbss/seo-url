package com.stylight.seo.domain.exception;

public class NullUrlException extends RuntimeException {

    public NullUrlException() {
        super("Url can't be null");
    }
}
