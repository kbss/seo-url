package com.stylight.seo.domain;

import java.util.List;
import java.util.Map;

public interface ParametrizedUrlService {

    /**
     * Receives a list of parameterized URLs and returns a map of provided parameterized URLs as
     * keys with corresponding pretty URLs as values.
     *
     * @param urls
     * @return
     */
    Map<String, String> getPrettyUrls(List<String> urls);

}
