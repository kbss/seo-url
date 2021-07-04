package com.stylight.seo.util;

import org.junit.jupiter.api.Assertions;

import java.util.Iterator;
import java.util.Map;

public class AssertionUtils {
    public static void assertValidSingleResult(String expectedResult, Map<String, String> urls) {
        Iterator<Map.Entry<String, String>> iterator = urls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(expectedResult, next.getValue());
    }

    public static void assertValidSingleResult(String expectedKey, String expectedValue, Map<String, String> urls) {
        Iterator<Map.Entry<String, String>> iterator = urls.entrySet().iterator();
        Assertions.assertTrue(iterator.hasNext());
        Map.Entry<String, String> next = iterator.next();
        Assertions.assertEquals(expectedKey, next.getKey());
        Assertions.assertEquals(expectedValue, next.getValue());
    }

    public static void assertEach(Map<String, String> source, Map<String, String> result) {
        result.forEach((key, value) -> {
            Assertions.assertNotNull(value);
            Assertions.assertEquals(source.get(key), value);
        });
    }
}
