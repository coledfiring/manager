package com.whaty.designer.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 消费者超类
 * @author weipengsen
 */
public abstract class AbstractConsumer<T> {

    /**
     * 消费资源，如果无资源则一直阻塞
     * @return
     * @throws InterruptedException
     */
    public void consume() throws InterruptedException {
        BlockingQueue<T> queue = this.getQueueStore();
        T resource = queue.take();
        this.consumeResource(resource);
    }

    /**
     * 消费资源，如果无资源则阻塞一定时间
     * @param timeout
     * @param timeUnit
     * @throws InterruptedException
     */
    public void consume(long timeout, TimeUnit timeUnit) throws InterruptedException {
        BlockingQueue<T> queue = this.getQueueStore();
        T resource = queue.poll(timeout, timeUnit);
        this.consumeResource(resource);
    }

    /**
     * 子类复写，消费资源
     * @param resource
     */
    protected abstract void consumeResource(T resource);

    /**
     * 获取资源队列仓库
     * @return
     */
    protected abstract BlockingQueue<T> getQueueStore();

}
