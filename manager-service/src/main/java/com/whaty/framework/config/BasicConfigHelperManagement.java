package com.whaty.framework.config;

import com.whaty.framework.config.helper.AbstractVersionConfigHelper;
import com.whaty.framework.config.helper.AbstractVersionConfigSuperHelper;
import com.whaty.framework.common.spring.SpringUtil;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置辅助类统一管理
 *
 * @author weipengsen
 */
public class BasicConfigHelperManagement {

    public static Map<String, AbstractVersionConfigHelper> TYPE_HELPER_MAPPER;

    public static Collection<AbstractVersionConfigSuperHelper> ALL_HELPER;

    static {
        TYPE_HELPER_MAPPER = SpringUtil.getApplicationContext()
                .getBeansOfType(AbstractVersionConfigHelper.class).values()
                .stream().collect(Collectors.toMap(AbstractVersionConfigHelper::getConfigTypeCode, e -> e));
        ALL_HELPER = SpringUtil.getApplicationContext()
                .getBeansOfType(AbstractVersionConfigSuperHelper.class).values();
    }

    /**
     * 通过类型编号获取springBean
     * @param typeCode
     * @return
     */
    public static AbstractVersionConfigHelper getHelperByType(String typeCode) {
        return TYPE_HELPER_MAPPER.get(typeCode);
    }

}
