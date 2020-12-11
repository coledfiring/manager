package com.whaty.products.service.suda;

import java.util.Map;

/**
 * 苏大平台对接服务类
 *
 * @author suoqiangqiang
 */
public interface PlatformConnectService {

    /**
     * 获取成教平台URL映射
     *
     * @return
     */
    Map<String, String> getCjPlatformUrlMap();

    /**
     * 获取自考平台URL映射
     *
     * @return
     */
    Map<String, String> getZkPlatformUrlMap();

    /**
     * 获取成教官网地址
     *
     * @return
     */
    String getCjOfficialWebsite();

    /**
     * 获取培训官网地址
     *
     * @return
     */
    String getTrainOfficialWebsite();


}
