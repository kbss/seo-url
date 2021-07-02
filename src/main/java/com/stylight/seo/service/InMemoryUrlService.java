package com.stylight.seo.service;

import com.stylight.seo.domain.Node;
import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.DBStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class InMemoryUrlService implements UrlService {
    public static final String QUERY_PREFIX = "?";
    public static final String QUERY_SEPARATOR = "&";
    private final DBStub repository;
    //TODO: Externalize
    private double coverThreshold = 0.5;
    private Node root;

    public InMemoryUrlService(DBStub repository) {
        this.repository = repository;
    }

    @Override
    public Map<String, String> getPrettyUrls(List<String> urls) {
        log.info("Parametrized urls: {}", urls);
        Map<String, String> collect = urls
                .parallelStream()
                .map(url -> new AbstractMap.SimpleEntry<>(url, findByParametrizedUrl(url)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        return collect;
    }

    private String findByParametrizedUrl(String parametrizedUrl) {
        log.debug("Searching by parametrized url: {}", parametrizedUrl);
        return findBestMatchByParametrizedUrl(parametrizedUrl);
    }

    @Override
    public Map<String, String> getFullUrl(List<String> urls) {
        //TODO: return
        return null;
    }

    @PostConstruct
    private void posConstruct() {
        loadFromDatabase();
    }

    public String findBestMatchByParametrizedUrl(String parametrizedUrl) {
        List<String> urlParts = UrlUtils.splitParametrizedUrl(parametrizedUrl);
        log.info("------------------------------------");
        Node current = root;
        Node bestMatch = null;
        for (int i = 0; i < urlParts.size(); i++) {
            String part = urlParts.get(i);
            current = current.getByParametrizedUrl(part);
            if (current == null) {
                log.debug("Can't find exact url: {}", part);
                if (bestMatch == null) break;
                return buildPrettyPartialUrl(urlParts, i, bestMatch.getValue(), parametrizedUrl);
            } else {
                if (current.getValue() != null) {
                    bestMatch = current;
                }
                log.debug("Found url: {}", current.getValue());
            }
        }
        if (bestMatch == null) return parametrizedUrl;
        return bestMatch.getValue();
    }

    private String buildPrettyPartialUrl(List<String> urlParts, int lastMatchIndex, String partialUrl, String parametrizedFullUrl) {
        StringBuilder sb = new StringBuilder();
        if (!urlParts.contains(QUERY_PREFIX)) {
            sb.append(QUERY_PREFIX);
        }
        for (int i = lastMatchIndex; i < urlParts.size(); i++) {
            String s = urlParts.get(i);
            sb.append(s).append(QUERY_SEPARATOR);
        }
        //Delete last added QUERY_SEPARATOR
        sb = sb.deleteCharAt(sb.length() - 1);
        if (isBiggestPartOfUrlCovered(parametrizedFullUrl, sb.length())) {
            log.debug("Url didn't cover half of url, returning original url");
            return parametrizedFullUrl;
        }
        sb = sb.insert(0, partialUrl);
        return sb.toString();
    }

    private boolean isBiggestPartOfUrlCovered(String url, int rightPartLength) {
        return rightPartLength > url.length() * coverThreshold;
    }

    public void loadFromDatabase() {
        log.info("Loading urls from database...");
        root = new Node();
        Map<String, String> all = repository.findAll();
        all.entrySet().forEach(e -> {
            String parametrizedUrl = e.getKey();
            String prettyUrl = e.getValue();
            log.debug("{} -> {}", prettyUrl, parametrizedUrl);
            List<String> strings = UrlUtils.splitParametrizedUrl(parametrizedUrl);
            Node current = root;
            for (String part : strings) {
                current = current.addChild(part);
            }
            if (current != root) {
                current.setValue(parametrizedUrl);
            }
        });
        log.info("Loading urls from database is done!");
    }
}