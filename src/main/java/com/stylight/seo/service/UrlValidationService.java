package com.stylight.seo.service;

import com.stylight.seo.domain.ValidationService;
import com.stylight.seo.domain.exception.UrlValidationException;
import org.springframework.stereotype.Service;

@Service
public class UrlValidationService implements ValidationService {
    @Override
    public void validateUrl(String url) {
        if (url == null) {
            throw new UrlValidationException();
        }
    }
}
