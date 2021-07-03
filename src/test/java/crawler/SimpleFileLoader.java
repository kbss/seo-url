package crawler;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SimpleFileLoader {

    public static String getFile(String urlStr) {
        try {
            return IOUtils.toString(new URL(urlStr).openStream(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
