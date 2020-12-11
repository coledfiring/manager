package com.whaty.framework.aop.operatelog.param;

import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 普通对象提取状态机
 *
 * @author weipengsen
 */
@Lazy
@Component("pojoParamSate")
public class POJOParamState implements AbstractParamState {

    private final static String BUSINESS_PACKAGE_PREFIX = "com.whaty";

    private final static String ARG_NAME = "arg";

    @Override
    public Map<String, Object> extract(Object param) {
        if (!param.getClass().getPackage().getName().startsWith(BUSINESS_PACKAGE_PREFIX)) {
            return Collections.singletonMap(ARG_NAME, param);
        }
        return CommonUtils.convertObjectToMap(param);
    }

}
