package com.whaty.products.service.superadmin.impl;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.common.string.StringUtils;
import com.whaty.products.service.superadmin.DistributedLockInfoService;
import com.whaty.redisson.RedissonManager;
import com.whaty.redisson.constants.RedissonConstants;
import com.whaty.redisson.domain.LockInfo;
import com.whaty.redisson.domain.WaitLockInfo;
import org.apache.commons.collections.MapUtils;
import org.redisson.api.RLock;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分布式锁信息
 *
 * @author weipengsen
 */
@Lazy
@Service("distributedLockInfoService")
public class DistributedLockInfoServiceImpl implements DistributedLockInfoService {

    @Resource(name = "redisCacheService")
    private RedisCacheService redisCacheService;

    @Override
    public List<LockInfo> listLockInfo(String key, String url, String siteCode) {
        Map<String, Object> lockInfoMap = this.redisCacheService.getMap(RedissonConstants.LOCK_CACHE_MAP_KEY);
        if (MapUtils.isEmpty(lockInfoMap)) {
            return null;
        }
        return lockInfoMap.values().stream().map(e -> (LockInfo) e).filter(e -> {
            boolean confirm = true;
            if (StringUtils.isNotBlank(key)) {
                confirm = e.getKey().contains(key);
            }
            if (StringUtils.isNotBlank(url)) {
                confirm = confirm && e.getRequestUrl().contains(url);
            }
            if (StringUtils.isNotBlank(siteCode)) {
                confirm = confirm && e.getPeWebSite().getCode().contains(siteCode);
            }
            return confirm;
        }).sorted(Comparator.comparing(LockInfo::getLockTime)).collect(Collectors.toList());
    }

    @Override
    public void removeLock(List<String> keys) {
        keys.stream().peek(e -> redisCacheService.removeFromMap(RedissonConstants.LOCK_CACHE_MAP_KEY, e))
                .map(e -> RedissonManager.getRedisson().getLock(e)).forEach(RLock::forceUnlock);
    }

    @Override
    public List<WaitLockInfo> listWaitLockInfo(String key, String url, String siteCode) {
        Map<String, Object> lockInfoMap = this.redisCacheService.getMap(RedissonConstants.WAIT_LOCK_CACHE_MAP_KEY);
        if (MapUtils.isEmpty(lockInfoMap)) {
            return null;
        }
        return lockInfoMap.values().stream().map(e -> (WaitLockInfo) e).filter(e -> {
            boolean confirm = true;
            if (StringUtils.isNotBlank(key)) {
                confirm = e.getWaitKey().contains(key);
            }
            if (StringUtils.isNotBlank(url)) {
                confirm = confirm && e.getRequestUrl().contains(url);
            }
            if (StringUtils.isNotBlank(siteCode)) {
                confirm = confirm && e.getPeWebSite().getCode().contains(siteCode);
            }
            return confirm;
        }).peek(WaitLockInfo::countWaitedTime)
                .sorted(Comparator.comparing(WaitLockInfo::getRequestHoldTime))
                .collect(Collectors.toList());
    }
}
