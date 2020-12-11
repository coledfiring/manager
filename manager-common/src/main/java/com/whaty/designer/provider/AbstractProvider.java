package com.whaty.designer.provider;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 生产者超类
 * @author weipengsen
 */
public abstract class AbstractProvider<T, V> {

    /**
     * 生产资源，如果队列已满则阻塞一段时间
     * @param args
     * @param timeout
     * @param timeUnit
     * @throws InterruptedException
     * @return
     */
    public boolean provide(V args, long timeout, TimeUnit timeUnit) throws InterruptedException {
        T resource = this.generateResource(args);
        BlockingQueue<T> queue = this.getQueueStore();
        return queue.offer(resource, timeout, timeUnit);
    }

    /**
     * 生产资源，如果队列已满则一直阻塞
     * @param args
     * @throws InterruptedException
     */
    public void provide(V args) throws InterruptedException {
        T resource = this.generateResource(args);
        BlockingQueue<T> queue = this.getQueueStore();
        queue.put(resource);
    }

    /**
     * 子类复写，获取资源队列仓库
     * @return
     */
    protected abstract BlockingQueue<T> getQueueStore();

    /**
     * 子类复写，生成资源
     * @param args
     * @return
     */
    protected abstract T generateResource(V args);

}
