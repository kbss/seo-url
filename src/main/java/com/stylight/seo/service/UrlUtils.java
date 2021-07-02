package com.stylight.seo.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//TODO: tests
public class UrlUtils {

    public static final String QUERY_SEPARATOR = "&";
    public static final String QUERY_START = "\\?";

    public static List<String> splitParametrizedUrl(String url) {
        String[] split = url.split(QUERY_START);
        if (split.length <= 1) return Collections.singletonList(url);
        List<String> set = new LinkedList<>();
        set.add(split[0]);
        String queryPart = split[1];
        if (queryPart != null && StringUtils.isNotBlank(queryPart)) {
            String[] querySplit = queryPart.split(QUERY_SEPARATOR);
            for (String query : querySplit) {
                set.add(query);
            }
        }
        return set;
    }


    public static String buildUrl(String urlPart, String queryMap) {
        if (urlPart == null) return null;
        String prefix = "?";
        if (urlPart.contains("?")) prefix = "&";
        return urlPart + prefix + queryMap;
    }

    /**
     * Returns first url part, before query separation sign &
     * Example:
     * "/products?tag=5678"                        -> "/products?tag=5678"
     * "/products?gender=female&tag=123&tag=1234"  -> "/products?gender=female"
     *
     * @param url
     * @return
     */
    public static String getFirstPart(String url) {
        String[] split = url.split(QUERY_SEPARATOR);
        if (split.length <= 1) return url;
        return split[0];

    }

    /**
     * Returns first url part, before query separation sign &
     * Example:
     * "/products?tag=5678"                        -> "/products?tag=5678"
     * "/products?gender=female&tag=123&tag=1234"  -> "/products?gender=female"
     *
     * @param url
     * @return
     */
    public static Set<String> getSecondaryQueryParams(String url) {
        String[] split = url.split(QUERY_SEPARATOR);
        if (split.length <= 1) return Collections.emptySet();
        String queryPart = split[1];
        Set<String> set = new LinkedHashSet<>();
        if (queryPart != null && StringUtils.isNotBlank(queryPart)) {
            String[] querySplit = queryPart.split(QUERY_SEPARATOR);

            for (String query : querySplit) {
                set.add(query);
            }
        }
        return set;

    }
}
