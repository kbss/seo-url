package com.stylight.seo.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//TODO: test
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
}
