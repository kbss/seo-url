package com.stylight.seo;

import com.stylight.seo.repository.InMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RepositoryTest {

    private final InMemoryRepository repository = new InMemoryRepository();

    @Test
    public void testExactMatch() {
        Map<String, String> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
    }

}
