package com.stylight.seo.domain;

import com.stylight.seo.domain.exception.UrlValidationException;

public interface ValidationService {
    /**
     * Validation given url
     *
     * @param url URL path
     */
    void validateUrl(String url) throws UrlValidationException;
}
