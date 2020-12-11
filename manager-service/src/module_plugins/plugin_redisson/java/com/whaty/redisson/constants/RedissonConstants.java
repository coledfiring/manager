package com.whaty.redisson.constants;

/**
 * 常量池
 *
 * @author weipengsen
 */
public interface RedissonConstants {

    /**
     * 锁的key前缀
     */
    String LOCK_PREFIX = "redisson_lock_%s";

    /**
     * 锁信息在redis中的key
     */
    String LOCK_CACHE_MAP_KEY = "redisson_lock_map_key";

    /**
     * 存在redis中的等待锁的缓存key
     */
    String WAIT_LOCK_CACHE_MAP_KEY = "redisson_wait_lock_map_key";

}
