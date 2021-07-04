package com.stylight.seo;

import com.stylight.seo.domain.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeTest {

    @Test
    public void addNewValueTest() {
        Node node = new Node();
        String[] nodes = {"product", "tag=1", "tag=2" };
        for (String name : nodes) {
            Node child = node.addChild(name);
            Assertions.assertNotNull(child);
            Node product = node.getByUrl(name);
            Assertions.assertNotNull(product);
        }
        Assertions.assertNull(node.getByUrl("NotExisting"));
    }

    @Test
    public void getSetValueTest() {
        Node node = new Node();
        String name = "test";
        Node child = node.addChild(name);
        Assertions.assertNotNull(child);
        String testValue = "testValue";
        String testUrl = "testUrl";
        child.setValue(testValue);
        child.setUrl(testUrl);
        Node test = node.getByUrl(name);
        Assertions.assertNotNull(test);
        Assertions.assertEquals(test.getValue(), testValue);
        Assertions.assertEquals(test.getUrl(), testUrl);
    }


}
