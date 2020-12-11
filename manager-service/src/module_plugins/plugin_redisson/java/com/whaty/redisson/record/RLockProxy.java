package com.whaty.redisson.record;

import com.whaty.redisson.constants.RedissonConstants;
import com.whaty.redisson.domain.WaitLockInfo;
import com.whaty.utils.StaticBeanUtils;
import org.redisson.api.RLock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;

/**
 * 分布式锁代理
 *
 * @author weipengsen
 * @param <T>
 */
public class RLockProxy<T extends RLock> implements InvocationHandler {

    /**
     * 被代理对象
     */
    private final T target;

    private static final String[] CAN_RECORD_METHOD_NAME = {"lockInterruptibly", "tryLock", "lock"};

    static {
        Arrays.sort(CAN_RECORD_METHOD_NAME);
    }

    public RLockProxy(T target) {
        this.target = target;
    }

    /**
     * 构建代理类
     *
     * @return
     */
    public T build() {
        return (T) Proxy.newProxyInstance(this.target.getClass().getClassLoader(),
                this.target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Arrays.binarySearch(CAN_RECORD_METHOD_NAME, method.getName()) < 0) {
            return method.invoke(this.target, args);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        StaticBeanUtils.getRedisCacheService().putIntoMap(RedissonConstants.WAIT_LOCK_CACHE_MAP_KEY, token,
                new WaitLockInfo(this.target.getName()));
        try {
            return method.invoke(this.target, args);
        } finally {
            StaticBeanUtils.getRedisCacheService().removeFromMap(RedissonConstants.WAIT_LOCK_CACHE_MAP_KEY, token);
        }
    }

}
