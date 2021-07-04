package com.stylight.seo.repository;

import com.stylight.seo.domain.UrlRepository;
import com.stylight.seo.domain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryRepository implements UrlRepository {

    public static final String DATA_FILE = "urls-mapping.list";
    private Map<String, String> parameterizedUrslMapping;
    private Map<String, String> prettyUrlsMapping;

    @PostConstruct
    public void postConstruct() {
        loadFromFile();
    }

    public void loadFromFile() {
        log.debug("Loading data from file: {}...", DATA_FILE);
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Map<String, String> collect = new BufferedReader(
                    new InputStreamReader(classLoader.getResourceAsStream(DATA_FILE), StandardCharsets.UTF_8))
                    .lines()
                    .map(l -> l.split(","))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            log.debug("Loading data completed, size: {}", collect.size());
            parameterizedUrslMapping = collect;
            prettyUrlsMapping = parameterizedUrslMapping.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        } catch (Exception e) {
            throw new ServiceException("Can't load data from file", e);
        }
    }

    @Override
    public Map<String, String> findAll() {
        return parameterizedUrslMapping;
    }

    @Override
    public Optional<String> findByParametrizedUrl(String parametrizedUrl) {
        return Optional.ofNullable(parameterizedUrslMapping.get(parametrizedUrl));
    }

    @Override
    public Optional<String> findByPrettyUrl(String pretty) {
        return Optional.ofNullable(prettyUrlsMapping.get(pretty));
    }
}
