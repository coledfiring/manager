package com.whaty.framework.api.grant.utils;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.utils.StaticBeanUtils;

/**
 * 授权工具类
 *
 * @author weipengsen
 */
public class BxqkGrantUtils {

    /**
     * 根据系统常量的name获取简单登录的loginId
     * @param variableName
     * @return
     */
    public static String getSimpleLoginLoginId(String variableName) {
        return StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select value from system_variables where name = ? and site_code = ?",
                        variableName, SiteUtil.getSiteCode());
    }

}
