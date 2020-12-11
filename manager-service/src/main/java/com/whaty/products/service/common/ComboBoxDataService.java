package com.whaty.products.service.common;

import com.whaty.framework.exception.ServiceException;

import java.util.List;

/**
 * comboBox数据查询服务类
 * @author weipengsen
 */
public interface ComboBoxDataService {

    /**
     * 根据sqlId获得id，name数据
     * @param sqlId
     * @param params
     * @return
     * @throws ServiceException
     */
    List<Object[]> getIdNameListBySqlId(String sqlId, String params) throws ServiceException;

    /**
     * 根据beanName获得id，name数据
     * @param beanName
     * @return
     * @throws ServiceException
     */
    List<Object[]> getIdNameListByBeanName(String beanName) throws ServiceException;

    /**
     * 基础数据查询方法
     * @param beanName 基础bean的名称
     * @param beanIsActive bean是否必须有效
     * @return
     */
    List<Object[]> getBasicBeanDataList(String beanName, boolean beanIsActive);

    /**
     * 列出所有用户可以看到的角色
     * @return
     */
    List<Object[]> listRoleUserCanView();
}
