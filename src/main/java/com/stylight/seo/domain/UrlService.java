package com.stylight.seo.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UrlService {

    /**
     * Receives a list of parameterized URLs and returns a map of provided parameterized URLs as
     * keys with corresponding pretty URLs as values.
     *
     * @param urls
     * @return
     */
    Map<String, String> getPrettyUrls(Collection<String> urls);

    /**
     * Receives a list of  pretty URLs, and returns a map of provided pretty URLs as keys and corresponding
     * parameterized URLs as values.
     *
     * @param urls
     * @return
     */
    Map<String, String> getFullUrl(Collection<String> urls);

}
