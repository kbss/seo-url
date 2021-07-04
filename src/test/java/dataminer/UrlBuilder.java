package dataminer;

import org.apache.commons.io.FileUtils;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UrlBuilder {

    public static final String[] COLORS = {"red", "green", "blue", "magenta", "cyan", "yellow", "black", "white", "gray", "orange", "pink" };
    public static final String TAG = "tag=";
    private final Map<String, String> colorMap = new HashMap<>();
    private final Map<String, String> brandMap = new HashMap<>();
    private final Map<String, String> tagMap = new HashMap<>();
    private final Map<String, String> genderMap = new HashMap<>() {{
        put("men", "male");
        put("women", "female");
    }};
    private final Set<String> excludeSet = new HashSet<>() {{
        add("/Fashion/");
    }};
    int maxLength = 0;
    private int brandCount = 1;
    private int tagCount = 1;

    public UrlBuilder() {
        buildColorMap();
    }

    public static void main(String[] args) {
        new UrlBuilder().parseData();
    }

    private void buildColorMap() {
        int i = 0;
        for (String color : COLORS) {
            colorMap.put(color, i++ + "");
            colorMap.put("light-" + color, i++ + "");
            colorMap.put("dark-" + color, i++ + "");
        }
    }

    public Map<String, String> parseData() {
        Map<String, String> urlsMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        List<String> strings = loadUrlsFromFile();
        for (int i = 0; i < strings.size(); i++) {
            String url = strings.get(i);
            if (excludeSet.contains(url)) continue;
            if (url.startsWith("/")) {
                String parametrizedUrls = parseUrl(url);
                if (StringUtils.isNotBlank(parametrizedUrls)) {
                    urlsMap.put(parametrizedUrls, url);
                    maxLength = Math.max(parametrizedUrls.length(), url.length());
                    sb.append(parametrizedUrls).append(",").append(url).append("\n");
                }
            }
        }
        System.out.println("Max length: " + maxLength);
        try {
            FileUtils.write(new File("urls-mapping.list"), sb.toString(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlsMap;
    }

    private String getColor(String str) {
        return colorMap.get(str.toLowerCase());
    }

    private String getGender(String str) {
        return genderMap.get(str.toLowerCase());
    }

    private String parseUrl(String url) {
        String[] split = url.split("/");
        if (split.length < 1) return null;
        String genderCode = null;
        String colorCode = null;
        String brandCode = null;
        String tag = "";
        Set<String> tags = new HashSet<>();
        for (int i = split.length - 1; i > 0; i--) {
            String part = split[i];
            if (i == 1) {
                String gender = getGender(part);
                if (gender != null) {
                    genderCode = gender;
                    continue;
                }
                brandCode = brandMap.computeIfAbsent(part, b -> brandCount++ + "");
                break;
            }
            String gender = getGender(part);
            String color = getColor(part);
            if (genderCode == null && gender != null) {
                genderCode = gender;
                continue;
            } else if (colorCode == null && color != null) {
                colorCode = color;
                continue;
            }
            if (StringUtils.isNotBlank(part)) {
                String id = tagMap.get(part);
                if (id == null) {
                    id = Integer.toString(tagCount++);
                    tagMap.put(part, id);
                }
                tags.add(TAG + id);
            }
        }
        tag = tags.stream().collect(Collectors.joining("&"));
        StringBuilder sb = new StringBuilder("/products?");
        appendUrl(brandCode, sb, "brand=");
        appendUrl(genderCode, sb, "gender=");
        appendUrl(colorCode, sb, "color=");
        sb.append(tag);

        String string = sb.toString();
        string = removeLastQuerySeparator(string);
        return string;
    }

    private String removeLastQuerySeparator(String string) {
        if (string.endsWith("&")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    private StringBuilder appendUrl(String brandCode, StringBuilder sb, String s) {
        if (StringUtils.isBlank(brandCode)) return sb;
        return sb.append(s).append(brandCode).append("&");
    }

    private List<String> loadUrlsFromFile() {
        try {
            return FileUtils.readLines(new File("urls.list"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
