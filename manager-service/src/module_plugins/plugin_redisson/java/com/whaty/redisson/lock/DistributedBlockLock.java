package com.whaty.redisson.lock;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 分布式阻塞锁
 *
 * @author weipengsen
 */
public class DistributedBlockLock extends AbstractDistributedLock {

    public DistributedBlockLock(String key) {
        super(key);
    }

    /**
     * 阻塞锁
     * 可被线程中断
     *
     * @param time 阻塞超时时间
     * @param unit 单位
     * @throws InterruptedException
     * @throws SocketException
     * @throws UnknownHostException
     */
    public void lock(long time, TimeUnit unit) throws InterruptedException, SocketException, UnknownHostException {
        this.recordLock(time, unit);
        this.lock.lockInterruptibly(time, unit);
    }

}
