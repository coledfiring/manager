package com.whaty.framework.api.grant.service.impl;

import com.whaty.framework.api.grant.service.CheckSignService;
import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.factory.CheckSignStrategyFactory;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.exception.UncheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 签名校验服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("checkSignServiceImpl")
public class CheckSignServiceImpl implements CheckSignService {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private final static Logger logger = LoggerFactory.getLogger(CheckSignServiceImpl.class);

    @Override
    public boolean checkSign(Map<String, String> params, String sign) {
        params.forEach((k, v) -> {
            try {
                params.put(k, v == null ? null : URLDecoder.decode(v, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("参数" + k + "解码失败");
                }
                throw new UncheckException(e);
            }
        });
        if (!CheckSignStrategyFactory.newInstance(GrantConstant.CHECK_SIGN_TYPE_SORT_MD5)
                .checkSign(params, sign)) {
            if (logger.isWarnEnabled()) {
                logger.warn("校验签名失败");
            }
            return false;
        }
        if (this.redisCacheService.getFromCache(sign) != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("签名已过期");
            }
            return false;
        }
        this.redisCacheService.putToCache(sign, sign);
        return true;
    }

}
