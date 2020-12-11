package com.whaty.serverpoll.server;

import com.whaty.notice.exception.NoticeServerPollException;

import java.util.Map;

/**
 * 服务器推接口
 * @author weipengsen
 */
public abstract class AbstractServerPollServer {

    /**
     * 当前对像是否正在使用(共享资源互斥变量)
     */
    protected volatile boolean isUsing;

    /**
     * 上次请求时间
     */
    protected long lastRequestTime;

    /**
     * 执行服务器推方法
     * @param args
     * @author weipengsen
     * @return 推送返回信息
     * @throws NoticeServerPollException
     */
    public abstract Map<String, Object> run(Map<String, Object> args) throws NoticeServerPollException;

    /**
     * 销毁方法
     * @author weipengsen
     */
    public abstract void destroy();

    /**
     * 拿到服务类的key
     * @return
     */
    public abstract String getKey();

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(long lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

}
