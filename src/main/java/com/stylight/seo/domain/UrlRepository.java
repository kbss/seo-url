package com.stylight.seo.domain;

import java.util.Map;
import java.util.Optional;

public interface UrlRepository {

    Map<String, String> findAll();

    Optional<String> findByParametrizedUrl(String parametrizedUrl);

    Optional<String> findByPrettyUrl(String pretty);

}
