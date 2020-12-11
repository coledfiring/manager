package com.whaty.products.service.siteconfig;

import java.util.Map;

/**
 * 站点enumConst管理服务类
 * @author suoqiangqiang
 */
public interface SiteConfigInfoManageService {

    /**
     * 获取规则json
     * @param name
     * @return
     */
    Map<String,Object> getRegularJson(String name);

    /**
     * 保存自定义规则json
     * @param name
     * @param regularJson
     */
    void saveRegularJson(String name, String regularJson);

    /**
     * 获取学生报名信息
     * @return
     */
    Map<String,Object> getEnrollInfoConfig();
}
