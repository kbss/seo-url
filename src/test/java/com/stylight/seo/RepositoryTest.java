package com.stylight.seo;

import com.stylight.seo.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

public class RepositoryTest {

    private InMemoryRepository repository = new InMemoryRepository();

    @Test
    public void testExactMatch() {
//        Optional<String> exactPretty = repository.findExactByParametrizedUrl("/products?brand=123");
//        Assertions.assertTrue(exactPretty.isPresent());
//        Assertions.assertEquals("/Adidas/", exactPretty.get());
    }

}
