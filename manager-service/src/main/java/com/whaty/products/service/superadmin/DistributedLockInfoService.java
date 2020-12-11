package com.whaty.products.service.superadmin;

import com.whaty.redisson.domain.LockInfo;
import com.whaty.redisson.domain.WaitLockInfo;

import java.util.List;

/**
 * 分布式锁信息
 *
 * @author weipengsen
 */
public interface DistributedLockInfoService {

    /**
     * 列举锁信息
     * @return
     * @param key
     * @param url
     * @param siteCode
     */
    List<LockInfo> listLockInfo(String key, String url, String siteCode);

    /**
     * 移除锁信息
     * @param keys
     */
    void removeLock(List<String> keys);

    /**
     * 列举锁等待信息
     * @param key
     * @param url
     * @param siteCode
     * @return
     */
    List<WaitLockInfo> listWaitLockInfo(String key, String url, String siteCode);
}
