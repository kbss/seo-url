package com.stylight.seo.controller;

import com.stylight.seo.domain.UrlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PrettyUrlController {

    private final UrlService service;

    public PrettyUrlController(UrlService service) {
        this.service = service;
    }

    @GetMapping("pretty")
    public Map<String, String> getPrettyUrls(List<String> parametrizedUrls) {
        return service.getPrettyUrls(parametrizedUrls);
    }

    @GetMapping(path = "parametrized")
    public Map<String, String> getFullUrl(List<String> prettyUrls) {
        //TODO: return
        return service.getFullUrl(prettyUrls);
    }
}