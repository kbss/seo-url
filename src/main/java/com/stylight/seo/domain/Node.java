package com.stylight.seo.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class Node {

    private String value;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, Node> partsMap = new HashMap<>();

    public Node addChild(String urlPart) {
        Node childNode = new Node();
        return partsMap.computeIfAbsent(urlPart, n -> childNode);
    }

    public Node getByParametrizedUrl(String parametrizedUrlPart) {
        return partsMap.get(parametrizedUrlPart);
    }
}
