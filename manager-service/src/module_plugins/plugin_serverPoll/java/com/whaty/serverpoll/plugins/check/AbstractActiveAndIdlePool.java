package com.whaty.serverpoll.plugins.check;

import com.whaty.designer.deal.AbstractDaemonThreadDealSupport;
import com.whaty.serverpoll.server.AbstractServerPollServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 池机制提供类
 * @author weipengsen
 */
public abstract class AbstractActiveAndIdlePool extends AbstractDaemonThreadDealSupport {

    /**
     * 空闲超时时间
     */
    protected long idleTimeout;

    /**
     * 空闲队列最大数量
     */
    protected long idleMaxQueueSize;

    /**
     * 存储请求过自身的userId
     */
    protected final ConcurrentLinkedQueue<AbstractServerPollServer> idlePool = new ConcurrentLinkedQueue<>();

    /**
     * 活动的服务端对象
     */
    protected final ConcurrentHashMap<String, AbstractServerPollServer> activeQueue = new ConcurrentHashMap<>();

    /**
     * 检测并清除无用的服务端对象
     */
    protected void checkAndDestroy() {
        //循环拿出活动map中的服务端对象
        for(Map.Entry<String, AbstractServerPollServer> entry : this.activeQueue.entrySet()) {
            //从map中拿出设置为using的过程和销毁进程中的销毁过程应该是互斥的
            try {
                synchronized (entry.getKey().intern()) {
                    AbstractServerPollServer server = entry.getValue();
                    //判断服务类是否正在被使用
                    if (!server.isUsing()) {
                        //判断服务端对象是否空闲超时
                        long now = System.currentTimeMillis();
                        long lastRequestTime = server.getLastRequestTime();
                        if (now >= lastRequestTime + idleTimeout) {
                            this.dropFromPool(server);
                        }
                    }
                }
            } catch(RuntimeException e) {
                //运行时的错误如redis缓存问题不能停止此线程，否则会造成服务类过多对内存造成消耗
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除服务类
     * @param server
     */
    private void dropFromPool(AbstractServerPollServer server) {
        //移出活动集合
        this.activeQueue.remove(server.getKey());
        //如果超时将执行清除操作
        server.destroy();
        //判断队列是否已满
        if (this.idlePool.size() < idleMaxQueueSize) {
            //队列未满放入空闲队列
            this.idlePool.offer(server);
        }
    }

    @Override
    public void deal() {
       this.checkAndDestroy();
    }

}
