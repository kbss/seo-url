package com.stylight.seo.service;

import com.stylight.seo.application.CacheConfiguration;
import com.stylight.seo.domain.Node;
import com.stylight.seo.domain.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TreeUrlService {

    public static final String QUERY_PREFIX = "?";
    public static final String QUERY_SEPARATOR = "&";
    private final ValidationService validationService;
    //TODO: Externalize
    private double coverThreshold = 0.5;
    private Node parametrizedUrlsRoot;
    private Node prettyUrlsRoot;

    public TreeUrlService(ValidationService validationService) {
        parametrizedUrlsRoot = new Node();
        prettyUrlsRoot = new Node();
        this.validationService = validationService;
    }

    public void addNewParametrizedNode(String url, String alias) {
        addNewNode(parametrizedUrlsRoot, url, alias);
    }

    public void addNewPrettyNode(String url, String alias) {
        addNewNode(prettyUrlsRoot, url, alias);
    }

    private void addNewNode(Node root, String url, String alias) {
        List<String> strings = splitUrl(url);
        Node current = root;
        for (String part : strings) {
            current = current.addChild(part);
        }
        if (current != root) {
            current.setUrl(alias);
        }
    }

    private boolean isBiggestPartOfUrlCovered(String url, int rightPartLength) {
        return rightPartLength > url.length() * coverThreshold;
    }

    private String buildPartialUrl(List<String> urlParts, int lastMatchIndex, String partialUrl, String parametrizedFullUrl) {
        StringBuilder sb = new StringBuilder();
        if (!partialUrl.contains(QUERY_PREFIX)) {
            sb.append(QUERY_PREFIX);
        } else {
            sb.append(QUERY_SEPARATOR);
        }
        for (int i = lastMatchIndex; i < urlParts.size(); i++) {
            String s = urlParts.get(i);
            sb.append(s).append(QUERY_SEPARATOR);
        }
        //Delete last added QUERY_SEPARATOR
        sb = sb.deleteCharAt(sb.length() - 1);
        if (isBiggestPartOfUrlCovered(parametrizedFullUrl, sb.length())) {
            log.debug("Url didn't cover half of url, returning original url");
            return parametrizedFullUrl;
        }
        sb = sb.insert(0, partialUrl);
        return sb.toString();
    }

    @Cacheable(cacheNames = CacheConfiguration.PARAMETRIZED_URL, condition = "#url != null", key = "#url")
    public String findBestMatchByParametrizedUrl(String url) {
        return findBestMatch(url, parametrizedUrlsRoot);
    }

    @Cacheable(cacheNames = CacheConfiguration.PRETTY_URL, condition = "#url != null", key = "#url")
    public String findBestMatchByPrettyUrl(String url) {
        return findBestMatch(url, prettyUrlsRoot);
    }

    private String findBestMatch(String url, Node current) {
        validationService.validateUrl(url);
        List<String> urlParts = splitUrl(url);
        Node bestMatch = null;
        for (int i = 0; i < urlParts.size(); i++) {
            String part = urlParts.get(i);
            current = current.getByParametrizedUrl(part);
            if (current == null) {
                log.debug("Can't find exact url: {}", part);
                if (bestMatch == null) break;
                return buildPartialUrl(urlParts, i, bestMatch.getUrl(), url);
            } else {
                if (current.getUrl() != null) {
                    bestMatch = current;
                }
            }
        }
        if (bestMatch == null) return url;
        return bestMatch.getUrl();
    }

    /**
     * Splits given url by query params
     *
     * @param url
     * @return
     */
    private List<String> splitUrl(String url) {
        return UrlUtils.splitParametrizedUrl(url);
    }

}