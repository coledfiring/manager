package com.whaty.cache;

/**
 * 缓存键结构
 * @author hanshichao
 */
public class CacheKey {
    /**
     * key
     */
    private String key;

    /**
     * 有效时间
     */
    private Long expires;

    public CacheKey(String key) {
        this.key = key;
    }

    public CacheKey(String key, Long expires) {
        this.key = key;
        this.expires = expires;
    }

    /**
     * 带参数的缓存key
     * @param args 可变长度参数
     * @return
     */
    public String getKeyWithParams(Object... args) {
        return String.format(key, args);
    }

    public String getKey() {
        return key;
    }

    public Long getExpires() {
        return expires;
    }

}
