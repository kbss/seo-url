package com.stylight.seo.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DBStub {

    private Map<String, String> prettyUrlsMapping;
    private Map<String, String> parameterizedUrsMapping = new HashMap<>() {{
        put("/products", "/Fashion/");
        put("/products?gender=female", "/Women/");
        put("/products?tag=5678", "/Boat--Shoes/");
        put("/products?gender=female&tag=123&tag=1234", "/Women/Shoes/");
        put("/products?brand=123", "/Adidas/");
        put("/products?gender=female&tag=5678", "/Women/Shoes/?tag=5678");
    }};

    public DBStub() {
        //Invert key/value
        prettyUrlsMapping = parameterizedUrsMapping.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    public Optional<String> findExactByParametrizedUrl(String parametrizedUrl) {
        return Optional.ofNullable(parameterizedUrsMapping.get(parametrizedUrl));
    }

    public Optional<String> findExactByPrettyUrl(String prettyUrl) {
        return Optional.ofNullable(prettyUrlsMapping.get(prettyUrl));
    }

    public void getClosest(String url) {
        prettyUrlsMapping.keySet().stream().map(k -> k + " " + StringUtils.getLevenshteinDistance(k, url)).forEach(System.out::println);
    }

    public Map<String, String> findAll(){
        return parameterizedUrsMapping;
    }
}
