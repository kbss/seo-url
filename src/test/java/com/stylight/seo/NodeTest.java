package com.stylight.seo;

import com.stylight.seo.domain.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeTest {

    @Test
    public void addNewValue() {
        Node node = new Node();
        String[] nodes = {"product", "tag=1", "tag=2" };
        for (String name : nodes) {
            node.addChild(name);
            Node product = node.getByUrl(name);
            Assertions.assertNotNull(product);
        }
        Assertions.assertNull(node.getByUrl("NotExisting"));
    }
}
