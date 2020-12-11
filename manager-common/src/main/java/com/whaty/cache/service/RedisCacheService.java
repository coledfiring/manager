package com.whaty.cache.service;

import java.util.Date;
import java.util.Map;

/**
 * 缓存服务接口
 *
 * @author yanjunfeng
 * @update hanshichao
 * @copy from CacheService
 */
public interface RedisCacheService {

    /**
     * 存入缓存，永不失效
     *
     * @param key   键
     * @param value 值
     */
    void putToCache(String key, Object value);

    /**
     * 存入缓存，指定时间后失效
     *
     * @param key          键
     * @param value        值
     * @param expireSecond 有效时间，单位秒
     */
    void putToCache(String key, Object value, int expireSecond);

    /**
     * 存入缓存，在指定的时间点失效
     *
     * @param key   键
     * @param value 值
     * @param date  失效时间，是一个确切的时间点
     */
    void putToCache(String key, Object value, Date date);

    /**
     * 从缓存中获取单个值
     *
     * @param key 键
     * @return 值
     */
    <T> T getFromCache(String key);

    /**
     * 从缓存获取多个值
     *
     * @param keys 一组键
     * @return 一组值
     */
    Map<String, Object> getObjectsFromCache(String[] keys);

    /**
     * 获取map对象
     * @param key 键
     * @return map
     */
    Map<String, Object> getMap(String key);

    /**
     * 从缓存map中拿出数据
     * @param key 缓存的key值
     * @param field 缓存中的map中需要拿出的值对应的key值
     * @return
     */
    <T> T getFromMap(String key, String field);

    /**
     * 向缓存map插入数据
     * @param key 缓存的key值
     * @param field 缓存中的map中value对应的key值
     * @param value
     */
    void putIntoMap(String key, String field, Object value);

    /**
     * 删除缓存map中的数据
     * @param key 缓存的key值
     * @param field 缓存中的map中需要删除的值对应的key值
     */
    void removeFromMap(String key, String field);

    /**
     * 从缓存中删除一个值
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 从缓存中删除一个值
     *
     * @param cacheName 缓存块名
     * @param key 键
     */
    void remove(String cacheName, String key);

    /**
     * 从缓存中删除多个值
     *
     * @param keys
     */
    void removeMulti(String[] keys);

    /**
     * 清除全部缓存
     */
    void flushAll();

    /**
     * 检查是否是map类型
     * @param key
     * @return
     */
    boolean checkIsMap(String key);

    /**
     * 提取出key和field
     * @param key
     * @return
     */
    String[] extractKeyAndField(String key);
}
