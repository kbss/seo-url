package com.stylight.seo;

import com.stylight.seo.repository.DBStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class RepositoryTest {

    private DBStub repository = new DBStub();

    @Test
    public void testExactMatch() {
        Optional<String> exactPretty = repository.findExactByParametrizedUrl("/products?brand=123");
        Assertions.assertTrue(exactPretty.isPresent());
        Assertions.assertEquals("/Adidas/", exactPretty.get());
    }

}
