package com.whaty.designer.listener;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.framework.asserts.TycjAssert;

import java.util.Optional;

/**
 * 版本更新器
 *
 * @author weipengsen
 */
public abstract class AbstractVersionUpdateListenerWithRedis {

    private volatile Long version;

    private final static int RETRY_NUM = 5;

    /**
     * 检查是否已更新
     * @return
     */
    public boolean checkUpdated() {
        if (this.getVersionFromRemote() == null) {
            this.updateRemoteVersion(System.currentTimeMillis());
            return false;
        }
        if (this.getVersionFromLocal() == null) {
            this.updateLocalVersion(this.getVersionFromRemote());
        }
        return Optional.ofNullable(this.getVersionFromRemote())
                .map(e -> !e.equals(this.getVersionFromLocal())).orElse(false);
    }

    /**
     * 更新配置
     */
    public void update() {
        this.updateLocalVersion(this.getVersionFromRemote());
        this.updateNow();
    }

    /**
     * 获取远端版本
     * @return
     */
    private Long getVersionFromRemote() {
        Long target = this.getRedisCacheService().getFromCache(this.getCacheKey());
        int retryNum = 1;
        while (target == null && retryNum <= RETRY_NUM) {
            target = this.getRedisCacheService().getFromCache(this.getCacheKey());
            retryNum ++;
        }
        return target;
    }

    /**
     * 更新远端版本
     * @param version
     */
    public void updateRemoteVersion(Long version) {
        TycjAssert.isAllNotNull(version, "version must is not null");
        this.getRedisCacheService().putToCache(this.getCacheKey(), version, 24 * 60 * 60);
    }

    /**
     * 获取本地版本
     * @return
     */
    private Long getVersionFromLocal() {
        return this.version;
    }

    /**
     * 更新本地版本
     * @param version
     */
    private void updateLocalVersion(Long version) {
        TycjAssert.isAllNotNull(version, "version must is not null");
        this.version = version;
    }

    /**
     * 获取缓存服务类
     * @return
     */
    protected abstract RedisCacheService getRedisCacheService();

    /**
     * 模板方法，更新
     */
    protected abstract void updateNow();

    /**
     * 获取缓存key
     * @return
     */
    protected abstract String getCacheKey();
}
