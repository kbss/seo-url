package com.stylight.seo.domain;

import java.util.HashMap;
import java.util.Map;


public class Node {

    private String value;
    private String url;

    private Map<String, Node> partsMap = new HashMap<>();

    public Node addChild(String urlPart) {
        Node childNode = new Node();
        return partsMap.computeIfAbsent(urlPart, n -> childNode);
    }

    public Node getByUrl(String urlPart) {
        return partsMap.get(urlPart);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
