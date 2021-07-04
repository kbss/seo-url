package com.stylight.seo;

import com.stylight.seo.service.TreeUrlService;
import com.stylight.seo.service.UrlValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeUrlServiceTest {

    private TreeUrlService service = new TreeUrlService(new UrlValidationService(), 0.5);

    @Test
    public void findBestMatchTest() {
        String url = "/products?brand=579&color=30&tag=119";
        String alias = "/Lilly-Pulitzer/Short-Pants/Pink/";
        service.addNewParametrizedNode(url, alias);

        String bestMatchByParametrizedUrl = service.findBestMatchByParametrizedUrl(url);
        Assertions.assertEquals(alias, bestMatchByParametrizedUrl);
    }

    @Test
    public void findLittleCoverTest() {
        String url = "/Lilly-Pulitzer/Short-Pants/Pink/";
        String alias = "/products?brand=579&color=30&tag=119";
        service.addNewPrettyNode(url, alias);
        String longUrl = url + "?tag=10000&tag=9222220&tag=456777&tag1000";
        String result = service.findBestMatchByPrettyUrl(longUrl);
        Assertions.assertEquals(longUrl, result);
    }
}
