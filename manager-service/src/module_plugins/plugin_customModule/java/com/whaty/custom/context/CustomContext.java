package com.whaty.custom.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 定制配置存储的线程上下文
 *
 * @author weipengsen
 */
public class CustomContext {

    /**
     * 线程上下文
     */
    private final static ThreadLocal<Map<String, String>> CUSTOM_CONFIG_CONTEXT = new ThreadLocal<>();

    /**
     * 向上下文存储值
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        if (CUSTOM_CONFIG_CONTEXT.get() == null) {
            CUSTOM_CONFIG_CONTEXT.set(new HashMap<>(16));
        }
        CUSTOM_CONFIG_CONTEXT.get().put(key, value);
    }

    /**
     * 将map中所有的键值对放入上下文中
     * @param collection
     */
    public static void putAll(Map<String, String> collection) {
        if (CUSTOM_CONFIG_CONTEXT.get() == null) {
            CUSTOM_CONFIG_CONTEXT.set(new HashMap<>(16));
        }
        CUSTOM_CONFIG_CONTEXT.get().putAll(collection);
    }

    /**
     * 从上下文中拿值
     * @param key
     * @return
     */
    public static String get(String key) {
        if (CUSTOM_CONFIG_CONTEXT.get() == null) {
            return null;
        }
        return CUSTOM_CONFIG_CONTEXT.get().get(key);
    }

    /**
     * 获取上下文中所有的值
     * @return
     */
    public static Map<String, String> getMap() {
        return CUSTOM_CONFIG_CONTEXT.get();
    }

    /**
     * 移除上下文中的值
     * @param key
     */
    public static void remove(String key) {
        CUSTOM_CONFIG_CONTEXT.get().remove(key);
    }

    /**
     * 移除上下文中的线程绑定对象
     */
    public static void removeMap() {
        CUSTOM_CONFIG_CONTEXT.remove();
    }

}
