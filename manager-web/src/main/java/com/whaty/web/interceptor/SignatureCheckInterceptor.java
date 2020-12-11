package com.whaty.web.interceptor;

import com.whaty.framework.api.grant.service.CheckSignService;
import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 签名校验拦截器
 *
 * @author weipengsen
 */
public class SignatureCheckInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "checkSignServiceImpl")
    private CheckSignService checkSignService;

    private final static Logger logger = LoggerFactory.getLogger(SignatureCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        Map<String, String> params = RequestUtils.getRequestMap(request);
        if (MapUtils.isEmpty(params)) {
            params = this.convertMapType(CommonUtils.readParamsFromRequestBody(request, ParamsDataModel.class)
                    .getParams());
        }
        if (MapUtils.isEmpty(params)) {
            return false;
        }
        String sign = params.remove(GrantConstant.PARAM_SIGN);
        if (StringUtils.isBlank(sign)) {
            if (logger.isWarnEnabled()) {
                logger.warn("签名校验失败，not found sign :" + params);
            }
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null || "null".equals(entry.getValue())) {
                params.put(entry.getKey(), null);
            }
        }
        boolean isPass = this.checkSignService.checkSign(params, sign);
        if (!isPass && logger.isWarnEnabled()) {
            params.put(GrantConstant.PARAM_SIGN, sign);
            logger.warn("签名校验失败，校验信息:" + params);
            throw new ServiceException("签名校验失败!");
        }
        return isPass;
    }

    /**
     * 将map的泛型进行转换
     * @param params
     * @return
     */
    private Map<String, String> convertMapType(Map<String, Object> params) {
        return params.entrySet().stream().peek(e ->
                e.setValue(e.getValue() == null ? null : String.valueOf(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
    }
}
