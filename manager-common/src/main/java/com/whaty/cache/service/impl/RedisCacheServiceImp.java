package com.whaty.cache.service.impl;

import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.framework.cache.core.model.Cache;
import com.whaty.framework.cache.core.service.CacheService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 通用成教缓存操作接口
 * <p>
 * 所有缓存限定在cache_tycj片下（所有缓存key会自动加上cache_tycj前缀）
 * @author hanshichao
 */
@Service("redisCacheService")
public class RedisCacheServiceImp implements RedisCacheService, InitializingBean {

    /**
     * 缓存服务基础接口
     **/
    @Resource(name = "core_redisCacheService")
    private CacheService cacheService;
    /**
     * 直接操作缓存的接口
     **/
    private Cache cache = null;
    /**
     * 当前缓存类是否是第一次加载的标志位，因为缓存因为服务器重启很多失去了内存对象的对照，
     * 且没有设置超时时间(特别是putIntoMap方法)无法自动删除，对功能有影响，
     * 所以在服务器重启后第一次加载当前类的时候刷新缓存
     */
    private static boolean classFirstLoad = true;
    /**
     * map类型集合存入缓存时，在项目map中存入的值
     */
    private static final String MAP_VALUE_STR = "%s --> %s";

    /**
     * map型缓存split符号
     */
    private static final String MAP_VALUE_SPLIT = " --> ";

    /**
     * 校验map型的正则
     */
    private static final Pattern MAP_VALUE_PATTERN = Pattern.compile("^.+?-->.+?$");

    /**
     * bean属性所有属性初始化以后被调用，会在init前调用
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (null == cache) {
            cache = this.getCacheService().getCache(CacheKeys.CACHE_NAME);
        }
        //第一次加载对象刷新缓存
        if (classFirstLoad) {
            this.flushAll();
            classFirstLoad = false;
        }
    }

    /**
     * 存入缓存，永不失效
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void putToCache(String key, Object value) {
        putToCache(key, value, 0); // 0表示永久有效
    }

    /**
     * 存入缓存，指定时间后失效
     *
     * @param key          键
     * @param value        值
     * @param expireSecond 有效时间，单位秒
     */
    @Override
    public void putToCache(String key, Object value, int expireSecond) {
        cache.put(key, value, expireSecond);
        // 保存key
        cache.putIntoMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), key, value.toString());
    }

    /**
     * 存入缓存，在指定的时间点失效
     *
     * @param key   键
     * @param value 值
     * @param date  失效时间，是一个确切的时间点
     */
    @Override
    public void putToCache(String key, Object value, Date date) {
        putToCache(key, value, (int) ((date.getTime() - System.currentTimeMillis()) / 1000));
        // 保存key
        cache.putIntoMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), key, value.toString());
    }

    /**
     * 从缓存中获取单个值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public <T> T getFromCache(String key) {
        return (T) cache.get(key);
    }

    /**
     * 从缓存获取多个值
     *
     * @param keys 一组键
     * @return 一组值
     */
    @Override
    public Map<String, Object> getObjectsFromCache(String[] keys) {
        return cache.getMulti(Arrays.asList(keys));
    }

    /**
     * 获取map对象
     * @param key 键
     * @return map
     */
    @Override
    public Map<String, Object> getMap(String key) {
        return cache.getMap(key);
    }

    /**
     * 获取redis map缓存中的数据
     * @param key
     * @param field
     * @return
     */
    @Override
    public <T> T getFromMap(String key, String field) {
        return (T) this.cache.getFromMap(key, field);
    }

    /**
     * 将值存入缓存map中
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void putIntoMap(String key, String field, Object value) {
        this.cache.putIntoMap(key, field, value);
        this.cache.putIntoMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), String.format(MAP_VALUE_STR, key, field),
                value == null ? null : value.toString());
    }

    /**
     * 从缓存map中删除某个键
     * @param key
     * @param field
     */
    @Override
    public void removeFromMap(String key, String field) {
        this.cache.removeFromMap(key, field);
        this.cache.removeFromMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), String.format(MAP_VALUE_STR, key, field));
    }

    /**
     * 从缓存中删除一个值
     *
     * @param key 键
     */
    @Override
    public void remove(String key) {
        cache.remove(key);
        cache.removeFromMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), key);
    }

    @Override
    public void remove(String cacheName, String key) {
        Cache cache = this.cacheService.getCache(cacheName);
        cache.remove(key);
    }

    /**
     * 从缓存中删除多个值
     *
     * @param keys
     */
    @Override
    public void removeMulti(String[] keys) {
        cache.removeMulti(Arrays.asList(keys));
        cache.removeFromMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), keys);
    }

    /**
     * 清除全部缓存
     */
    @Override
    public void flushAll() {
        Map<String, Object> currentCached = cache.getMap(CacheKeys.CURRENT_CACHED_KEYS.getKey());
        if (MapUtils.isNotEmpty(currentCached)) {
            List<String> keyList = new ArrayList<String>(currentCached.keySet());
            cache.removeMulti(keyList);
            cache.removeFromMap(CacheKeys.CURRENT_CACHED_KEYS.getKey(), keyList.toArray(new String[keyList.size()]));
        }
    }

    /**
     * 检查是否是map型缓存
     * @param key
     * @return
     */
    @Override
    public boolean checkIsMap(String key) {
        return MAP_VALUE_PATTERN.matcher(key).find();
    }

    /**
     * 从key中分离出key和field
     * @param key
     * @return
     */
    @Override
    public String[] extractKeyAndField(String key) {
        return key.split(MAP_VALUE_SPLIT);
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

}