package com.whaty.redisson.lock;

import com.whaty.redisson.RedissonManager;
import com.whaty.redisson.constants.RedissonConstants;
import com.whaty.redisson.domain.LockInfo;
import com.whaty.redisson.record.RLockProxy;
import com.whaty.utils.StaticBeanUtils;
import org.redisson.api.RLock;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁抽象父类
 *
 * @author weipengsen
 */
public abstract class AbstractDistributedLock {

    /**
     * 主题key
     */
    protected final String key;
    /**
     * 锁对象
     */
    protected final RLock lock;

    public AbstractDistributedLock(String key) {
        this.key = this.convertKey(key);
        RLock lock = RedissonManager.getRedisson().getLock(this.key);
        this.lock = new RLockProxy<>(lock).build();
    }

    /**
     * 源key转换为锁使用的key
     * @param origin
     * @return
     */
    protected String convertKey(String origin) {
        return String.format(RedissonConstants.LOCK_PREFIX, origin);
    }

    /**
     * 释放锁
     * 检测是否非线程获取到锁
     */
    public void unlock() {
        if (!this.isHeldByCurrentThread()) {
            throw new RuntimeException(String.format("the lock [%s] is not held by current thread", this.key));
        }
        // 先释放锁，在移除记录，放置先移除记录后宕机导致无法在重启时删除
        this.lock.unlock();
        this.removeLockRecord();
    }

    /**
     * 是否被当前线程加锁
     * @return
     */
    public boolean isHeldByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    /**
     * 锁是否被获取
     * @return
     */
    public boolean isLocked() {
        return this.lock.isLocked();
    }

    /**
     * 强制释放锁
     * @return
     */
    public boolean forceUnLock() {
        boolean isSuccess = this.lock.forceUnlock();
        this.removeLockRecord();
        return isSuccess;
    }

    /**
     * 记录锁信息
     * @param time
     * @param unit
     * @throws SocketException
     * @throws UnknownHostException
     */
    protected void recordLock(long time, TimeUnit unit) throws SocketException, UnknownHostException {
        StaticBeanUtils.getRedisCacheService()
                .putIntoMap(RedissonConstants.LOCK_CACHE_MAP_KEY, this.key, new LockInfo(this.key, time, unit));
    }

    /**
     * 删除锁记录
     */
    protected void removeLockRecord() {
        StaticBeanUtils.getRedisCacheService().removeFromMap(RedissonConstants.LOCK_CACHE_MAP_KEY, this.key);
    }

}
