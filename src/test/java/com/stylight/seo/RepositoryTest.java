package com.stylight.seo;

import com.stylight.seo.repository.InMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

public class RepositoryTest {

    private static final InMemoryRepository repository = new InMemoryRepository();

    @BeforeAll
    public static void beforeTest() {
        repository.loadFromFile();
    }

    @Test
    public void testExactMatch() {
        Map<String, String> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
    }

    @Test
    public void findByPrettyUrl() {
        Optional<String> byParametrizedUrl = repository.findByParametrizedUrl("/products?brand=377&gender=female&tag=125");
        Assertions.assertTrue(byParametrizedUrl.isPresent());
        Assertions.assertEquals("/Outlander/Hoodies/Women/", byParametrizedUrl.get());
    }

    @Test
    public void findByParametrizedUrl() {
        Optional<String> prettyUrl = repository.findByPrettyUrl("/Vests/Black/1950s/");
        Assertions.assertTrue(prettyUrl.isPresent());
        Assertions.assertEquals("/products?brand=640&color=18&tag=147", prettyUrl.get());
    }

}
