package com.whaty.redisson;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.redisson.config.RedissonConfiguration;
import com.whaty.redisson.constants.RedissonConstants;
import org.apache.commons.collections.MapUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redisson对象管理类
 *
 * @author weipengsen
 */
@Configuration
@DependsOn("redissonConfiguration")
public class RedissonManager implements InitializingBean {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private static Redisson REDISSON;

    public static Redisson getRedisson() {
        return REDISSON;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        REDISSON = (Redisson) Redisson.create(RedissonConfiguration.REDISSON_CONFIG);
        // 启动时清除残留的锁
        Map<String, Object> lockInfo = this.redisCacheService.getMap(RedissonConstants.LOCK_CACHE_MAP_KEY);
        if (MapUtils.isNotEmpty(lockInfo)) {
            lockInfo.keySet().stream()
                    .peek(e -> redisCacheService.removeFromMap(RedissonConstants.LOCK_CACHE_MAP_KEY, e))
                    .map(REDISSON::getLock)
                    .forEach(RLock::forceUnlock);
        }
        // 启动时清除锁等待信息
        this.redisCacheService.remove(RedissonConstants.WAIT_LOCK_CACHE_MAP_KEY);
        testLock();
    }

    /**
     * 测试锁
     */
    private void testLock() {
        String randomKey = UUID.randomUUID().toString();
        RLock lock = REDISSON.getLock(randomKey);
        try {
            lock.lockInterruptibly(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
