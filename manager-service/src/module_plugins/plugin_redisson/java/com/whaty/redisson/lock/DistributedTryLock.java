package com.whaty.redisson.lock;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 分布式try锁
 *
 * @author weipengsen
 */
public class DistributedTryLock extends AbstractDistributedLock {

    public DistributedTryLock(String key) {
        super(key);
    }

    /**
     * 尝试锁
     *
     * @param time 超时时间
     * @param unit 超时时间单位
     *
     * @throws InterruptedException
     * @throws SocketException
     * @throws UnknownHostException
     */
    public boolean tryLock(long time, TimeUnit unit)
            throws InterruptedException, SocketException, UnknownHostException {
        boolean isSuccess = this.lock.tryLock(time, unit);
        this.recordLock(time, unit);
        return isSuccess;
    }

}
