package com.whaty.products.service.information;

import java.util.Map;

/**
 * 管理员个人信息服务类接口
 *
 * @author weipengsen
 */
public interface ManagerPersonInfoService {

    /**
     * 获取个人信息
     * @return
     */
    Map<String,Object> getPersonInfo();

    /**
     * 更新个人信息
     * @param personInfo
     */
    void updatePersonInfo(Map<String, Object> personInfo);

    /**
     * 微信解绑
     *
     */
    void doUnbindWeChat();

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 获取单位以及单位管理员信息
     * @return
     */
    Map<String, Object> getBusinessUnitInfo();

    /**
     * 更新单位以及单位管理员信息
     * @param businessUnitInfo
     */
    void updateBusinessUnitInfo(Map<String, Object> businessUnitInfo);
}
