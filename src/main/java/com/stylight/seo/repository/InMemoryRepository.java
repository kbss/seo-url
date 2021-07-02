package com.stylight.seo.repository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryRepository {

    private Map<String, String> parameterizedUrsMapping = new HashMap<>() {{
        put("/products", "/Fashion/");
        put("/products?gender=female", "/Women/");
        put("/products?tag=5678", "/Boat--Shoes/");
        put("/products?gender=female&tag=123&tag=1234", "/Women/Shoes/");
        put("/products?brand=123", "/Adidas/");
        put("/products?gender=female&tag=5678", "/Women/Shoes/?tag=5678");
        put("/products?gender=men&tag=100", "/Snoods/Men/");
    }};

    public Map<String, String> findAll() {
        return parameterizedUrsMapping;
    }
}
