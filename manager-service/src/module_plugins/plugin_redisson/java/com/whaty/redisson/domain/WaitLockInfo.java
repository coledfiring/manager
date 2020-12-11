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

/**
 * 等待锁信息
 *
 * @author weipengsen
 */
public class WaitLockInfo implements Serializable {

    private String waitKey;

    private PeWebSite peWebSite;

    private String threadName;

    private String serverIp;

    private String clientIp;

    private String requestUrl;

    private String requestHoldTime;

    private long requestHoldMillis;

    private long waitedTime;

    public WaitLockInfo(String waitKey) throws SocketException, UnknownHostException {
        this.waitKey = waitKey;
        this.peWebSite = SiteUtil.getSite(MasterSlaveRoutingDataSource.getDbType());
        this.threadName = Thread.currentThread().getName();
        this.requestHoldTime = CommonUtils.changeDateToString(new Date(),
                CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR);
        this.requestHoldMillis = System.currentTimeMillis();
        if (CommonUtils.getRequest() != null) {
            this.serverIp = CommonUtils.getServerIp();
            this.clientIp = CommonUtils.getIpAddress();
            this.requestUrl = CommonUtils.getRequest().getRequestURI();
        }
    }

    /**
     * 计算已等待时间
     * @return
     */
    public long countWaitedTime() {
        this.waitedTime = (System.currentTimeMillis() - this.requestHoldMillis) / 1000;
        return this.waitedTime;
    }

    public String getWaitKey() {
        return waitKey;
    }

    public void setWaitKey(String waitKey) {
        this.waitKey = waitKey;
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

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestHoldTime() {
        return requestHoldTime;
    }

    public void setRequestHoldTime(String requestHoldTime) {
        this.requestHoldTime = requestHoldTime;
    }

    public long getRequestHoldMillis() {
        return requestHoldMillis;
    }

    public void setRequestHoldMillis(long requestHoldMillis) {
        this.requestHoldMillis = requestHoldMillis;
    }

    public long getWaitedTime() {
        return waitedTime;
    }

    public void setWaitedTime(long waitedTime) {
        this.waitedTime = waitedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WaitLockInfo that = (WaitLockInfo) o;
        return requestHoldMillis == that.requestHoldMillis &&
                waitedTime == that.waitedTime &&
                Objects.equals(waitKey, that.waitKey) &&
                Objects.equals(peWebSite, that.peWebSite) &&
                Objects.equals(threadName, that.threadName) &&
                Objects.equals(serverIp, that.serverIp) &&
                Objects.equals(clientIp, that.clientIp) &&
                Objects.equals(requestUrl, that.requestUrl) &&
                Objects.equals(requestHoldTime, that.requestHoldTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(waitKey, peWebSite, threadName, serverIp, clientIp, requestUrl, requestHoldTime,
                requestHoldMillis, waitedTime);
    }

    @Override
    public String toString() {
        return "WaitLockInfo{" +
                "waitKey='" + waitKey + '\'' +
                ", peWebSite=" + peWebSite +
                ", threadName='" + threadName + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestHoldTime='" + requestHoldTime + '\'' +
                ", requestHoldMillis=" + requestHoldMillis +
                ", waitedTime=" + waitedTime +
                '}';
    }
}
