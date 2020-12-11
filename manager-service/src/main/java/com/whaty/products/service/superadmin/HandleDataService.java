package com.whaty.products.service.superadmin;

/**
 * 数据处理服务类接口
 *
 * @author weipengsen
 */
public interface HandleDataService {

    /**
     * 保存openApi的登录用户
     * @param loginId
     * @param password
     * @param siteCode
     */
    void saveOpenApiLoginUser(String loginId, String password, String siteCode);
}
