package com.stylight.seo.controller;

import com.stylight.seo.domain.UrlService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PrettyUrlController {

    private final UrlService service;

    public PrettyUrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping(path = "pretty", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getPrettyUrls(@RequestBody List<String> parametrizedUrls) {
        return service.getPrettyUrls(parametrizedUrls);
    }

    @PostMapping(path = "parametrized", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getFullUrl(@RequestBody List<String> prettyUrls) {
        return service.getParametrizedUrl(prettyUrls);
    }
}