package com.stylight.seo.service;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.InMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//TODO: caching
@Slf4j
@Service
public class InMemoryUrlService implements UrlService {
    private final InMemoryRepository repository;
    private final TreeUrlService parametrizedUrlService;

    public InMemoryUrlService(InMemoryRepository repository, TreeUrlService parametrizedUrlService) {
        this.repository = repository;
        this.parametrizedUrlService = parametrizedUrlService;
    }

    @Override
    public Map<String, String> getPrettyUrls(Collection<String> urls) {
        return findAll(urls, (u) -> parametrizedUrlService.findBestMatchByParametrizedUrl(u));
    }

    private Map<String, String> findAll(Collection<String> urls, Function<String, String> function) {
        Map<String, String> collect = urls
                .parallelStream()
                .map(url -> new AbstractMap.SimpleEntry<>(url, function.apply(url)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (k1, k2) -> k1));
        return collect;
    }

    @Override
    public Map<String, String> getParametrizedUrl(Collection<String> urls) {
        return findAll(urls, (u) -> parametrizedUrlService.findBestMatchByPrettyUrl(u));
    }

    @PostConstruct
    private void posConstruct() {
        log.info("Loading urls from database...");
        loadFromDatabase(repository.findAll());
        log.info("Loading urls from database is done!");
    }

    public void loadFromDatabase(Map<String, String> urlsMap) {
        urlsMap.entrySet().forEach(e -> {
            String parametrizedUrl = e.getKey();
            String prettyUrl = e.getValue();
            parametrizedUrlService.addNewParametrizedNode(parametrizedUrl, prettyUrl);
            parametrizedUrlService.addNewPrettyNode(prettyUrl, parametrizedUrl);
        });
    }
}