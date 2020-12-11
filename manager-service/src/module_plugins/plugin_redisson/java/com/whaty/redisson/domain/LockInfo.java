package com.whaty.redisson.domain;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.CommonUtils;

import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 锁信息
 *
 * @author weipengsen
 */
public class LockInfo implements Serializable {

    private static final long serialVersionUID = 7850232791011739952L;

    private String key;

    private PeWebSite peWebSite;

    private String threadName;

    private String serverIp;

    private String clientIp;

    private String lockTime;

    private String requestUrl;

    private long timeout;

    private TimeUnit timeoutUnit;

    public LockInfo(String key, long timeout, TimeUnit timeoutUnit) throws SocketException, UnknownHostException {
        this.key = key;
        this.peWebSite = SiteUtil.getSite(MasterSlaveRoutingDataSource.getDbType());
        this.threadName = Thread.currentThread().getName();
        this.lockTime = CommonUtils.changeDateToString(new Date(), CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        if (CommonUtils.getRequest() != null) {
            this.serverIp = CommonUtils.getServerIp();
            this.clientIp = CommonUtils.getIpAddress();
            this.requestUrl = CommonUtils.getRequest().getRequestURI();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PeWebSite getPeWebSite() {
        return peWebSite;
    }

    public void setPeWebSite(PeWebSite peWebSite) {
        this.peWebSite = peWebSite;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LockInfo lockInfo = (LockInfo) o;
        return timeout == lockInfo.timeout &&
                Objects.equals(key, lockInfo.key) &&
                Objects.equals(peWebSite, lockInfo.peWebSite) &&
                Objects.equals(threadName, lockInfo.threadName) &&
                Objects.equals(serverIp, lockInfo.serverIp) &&
                Objects.equals(clientIp, lockInfo.clientIp) &&
                Objects.equals(lockTime, lockInfo.lockTime) &&
                Objects.equals(requestUrl, lockInfo.requestUrl) &&
                timeoutUnit == lockInfo.timeoutUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, peWebSite, threadName, serverIp, clientIp, lockTime, requestUrl, timeout, timeoutUnit);
    }

    @Override
    public String toString() {
        return "LockInfo{" +
                "key='" + key + '\'' +
                ", peWebSite=" + peWebSite +
                ", threadName='" + threadName + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", lockTime=" + lockTime +
                ", requestUrl='" + requestUrl + '\'' +
                ", timeout=" + timeout +
                ", timeoutUnit=" + timeoutUnit +
                '}';
    }
}
