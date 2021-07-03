package com.stylight.seo.repository;

import com.stylight.seo.domain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryRepository {

    public static final String DATA_FILE = "urls-mapping.list";
    private Map<String, String> parameterizedUrsMapping = new HashMap<>() {{
        put("/products", "/Fashion/");
        put("/products?gender=female", "/Women/");
        put("/products?tag=5678", "/Boat--Shoes/");
        put("/products?gender=female&tag=123&tag=1234", "/Women/Shoes/");
        put("/products?brand=123", "/Adidas/");
    }};


    @PostConstruct
    public void postConstruct() {
        parameterizedUrsMapping = loadFromFile();
    }

    public Map<String, String> loadFromFile() {
        log.debug("Loading data from file: {}...", DATA_FILE);
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Map<String, String> collect = new BufferedReader(
                    new InputStreamReader( classLoader.getResourceAsStream(DATA_FILE), StandardCharsets.UTF_8))
                    .lines()
                    .map(l -> l.split(","))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));

            log.debug("Loading data completed, size: {}", collect.size());
            return collect;
        } catch (Exception e) {
            throw new ServiceException("Can't load data from file", e);
        }
    }


    public Map<String, String> findAll() {
        return parameterizedUrsMapping;
    }
}
