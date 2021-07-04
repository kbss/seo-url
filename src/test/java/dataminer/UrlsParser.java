package dataminer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UrlsParser {

    public static final String DOMAIN = "https://www.stylight.com";
    private static final String URL = "https://www.stylight.com/sitemap.xml?mapType=search&pageNumber=";
    private static final int MAX_PAGE = 174;
    private final Pattern URL_PATTERN = Pattern.compile("<loc>(.*)<\\/loc>");

    public static void main(String[] args) {
        new UrlsParser().loadFromNet();
    }

    public void loadFromNet() {
        Set<String> allUrls = new HashSet<>();
        for (int i = 0; i < MAX_PAGE; i++) {
            allUrls.addAll(loadData(i));
        }
        String collect = allUrls.stream().collect(Collectors.joining("\n"));

        try {
            FileUtils.write(new File("urls.list"), collect, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> loadData(int pageNum) {
        System.out.println("Lading page: " + pageNum);
        String file = SimpleFileLoader.getFile(URL + pageNum);
        Matcher matcher = URL_PATTERN.matcher(file);
        if (matcher.find()) {
            return matcher.results().map(mr -> mr.group(1).replace(DOMAIN, StringUtils.EMPTY)).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
