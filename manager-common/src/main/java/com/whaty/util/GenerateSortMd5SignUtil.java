package com.whaty.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 生成ASCII码排序的md5加密签名
 *
 * @author weipengsen
 */
public class GenerateSortMd5SignUtil {

    /**
     * 非法字符
     */
    private final static Map<String, String> INVALID_STRING_MAP;

    static {
        INVALID_STRING_MAP = new HashMap<>();
        INVALID_STRING_MAP.put("null", "null");
    }

    private final static Logger logger = LoggerFactory.getLogger(GenerateSortMd5SignUtil.class);

    /**
     * 生成
     * @param paramsMap
     * @return
     */
    public static String generate(Map<String, String> paramsMap) {
        paramsMap = new TreeMap<>(paramsMap);
        StringBuilder signStrTemp = new StringBuilder();
        paramsMap.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v) && !INVALID_STRING_MAP.containsKey(v)) {
                signStrTemp.append(k).append("=").append(v).append("&");
            }
            if (INVALID_STRING_MAP.containsKey(v) && logger.isWarnEnabled()) {
                logger.warn("生成签名的参数中存在非法字符:" + v);
            }
        });
        if (signStrTemp.indexOf("&") != -1) {
            signStrTemp.delete(signStrTemp.lastIndexOf("&"), signStrTemp.length());
        }
        return CommonUtils.md5(signStrTemp.toString());
    }

}
