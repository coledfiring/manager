package com.whaty.web.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * url中通配符匹配处理类
 *
 * @author weipengsen
 */
public class UrlPatternHandler {

    private final List<Pattern> patterns;

    private final static Map<String, String> PATTERN_SIGN_MAPPER;

    private final List<String> originPatternStringList;

    static {
        PATTERN_SIGN_MAPPER = new HashMap<>(16);
        PATTERN_SIGN_MAPPER.put("*", "([a-zA-Z0-9_;=]+)");
        PATTERN_SIGN_MAPPER.put("**", "([a-zA-Z0-9_/;=]+)");
    }

    public UrlPatternHandler(List<String> urlPatternList) {
        originPatternStringList = urlPatternList;
        patterns = new ArrayList<>();
        urlPatternList.forEach(e -> {
            for (Map.Entry<String, String> mapper : PATTERN_SIGN_MAPPER.entrySet()) {
                e = e.replace(mapper.getKey(), mapper.getValue());
            }
            patterns.add(Pattern.compile("^(" + e + ")$"));
        });
    }

    /**
     * 判断url是否与模式列匹配
     * @param url
     * @return
     */
    public boolean match(String url) {
        url = url.replaceAll("/+", "/");
        if (this.originPatternStringList.contains(url)) {
            return true;
        }
        for (Pattern pattern : this.patterns) {
            if (pattern.matcher(url).find()) {
                return true;
            }
        }
        return false;
    }

}
