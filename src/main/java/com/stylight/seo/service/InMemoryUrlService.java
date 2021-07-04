package com.stylight.seo.service;

import com.stylight.seo.domain.UrlRepository;
import com.stylight.seo.domain.UrlService;
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
    private final UrlRepository inMemoryRepository;
    private final TreeUrlService parametrizedUrlService;

    public InMemoryUrlService(UrlRepository inMemoryRepository, TreeUrlService parametrizedUrlService) {
        this.inMemoryRepository = inMemoryRepository;
        this.parametrizedUrlService = parametrizedUrlService;
    }

    @Override
    public Map<String, String> getPrettyUrls(Collection<String> urls) {
        return findAll(urls, u -> inMemoryRepository.findByParametrizedUrl(u).orElse(parametrizedUrlService.findBestMatchByParametrizedUrl(u)));
    }

    private Map<String, String> findAll(Collection<String> urls, Function<String, String> function) {
        Map<String, String> map = urls
                .parallelStream()
                .map(url -> new AbstractMap.SimpleEntry<>(url, function.apply(url)))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue, (k1, k2) -> k1));
        return map;
    }

    @Override
    public Map<String, String> getParametrizedUrl(Collection<String> urls) {
        return findAll(urls, u -> inMemoryRepository.findByPrettyUrl(u).orElse(parametrizedUrlService.findBestMatchByPrettyUrl(u)));
    }

    @PostConstruct
    private void posConstruct() {
        log.info("Loading urls from database...");
        loadFromDatabase(inMemoryRepository.findAll());
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