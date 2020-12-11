package com.whaty.products.service.user.impl;

import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.exception.UncheckException;
import com.whaty.products.service.user.UserDataOperateService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 用户数据操作服务类
 * @author suoqiangqiang
 */
@Lazy
@Service("userDataOperateService")
public class UserDataOperateServiceImpl implements UserDataOperateService {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    /**
     * 清除用户缓存
     * @param loginIdList
     * @param serverName
     */
    @Override
    public void removeUserCache(List<String> loginIdList, String serverName){
        for (String loginId : loginIdList) {
            String cacheKey = CacheKeys.LOGIN_CACHE_KEYS.getKeyWithParams(serverName, loginId);
            try {
                //保证编码
                cacheKey = URLDecoder.decode(cacheKey, "UTF-8");
                this.redisCacheService.remove(cacheKey);
                String key = serverName + "_" + loginId;
                this.redisCacheService.remove("com_whaty_store_defaultCache", key);
            } catch (UnsupportedEncodingException e) {
                throw new UncheckException(e);
            }
        }
    }
}
