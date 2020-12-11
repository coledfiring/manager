package com.whaty.products.service.superadmin.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * redis缓存管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("redisCacheManageService")
public class RedisCacheManageServiceImpl extends TycjGridServiceAdapter {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    /**
     * 搜索缓存
     * @param cacheKey
     * @return
     */
    @LogAndNotice("搜索redis缓存")
    public List<Map<String, String>> searchCache(String cacheKey) {
        Map<String, Object> currentCached = this.redisCacheService.getMap(CacheKeys.CURRENT_CACHED_KEYS.getKey());
        if (StringUtils.isNotBlank(cacheKey)) {
            currentCached = currentCached.entrySet().stream().filter(e -> e.getKey().contains(cacheKey))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return currentCached.entrySet().stream().map(e -> {
            Map<String, String> cacheMap = new HashMap<>(2);
            cacheMap.put("key", e.getKey());
            cacheMap.put("value", String.valueOf(e.getValue()));
            return cacheMap;
        }).collect(Collectors.toList());
    }

    /**
     * 移除缓存
     * @param cacheKeys
     */
    @LogAndNotice("移除redis缓存")
    public void removeCache(List<String> cacheKeys) {
        cacheKeys.forEach(e -> {
            if (this.redisCacheService.checkIsMap(e)) {
                String[] keyAndField = this.redisCacheService.extractKeyAndField(e);
                this.redisCacheService.removeFromMap(keyAndField[0], keyAndField[1]);
            } else {
                this.redisCacheService.remove(e);
            }
        });
    }
}
