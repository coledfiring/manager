package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;

/**
 * 带有站点code字段的基础bean超类
 *
 * @author weipengsen
 */
public abstract class AbstractSiteBean extends AbstractBean {

    /**
     * 设置站点编号
     * @param siteCode
     */
    public abstract void setSiteCode(String siteCode);

    /**
     * 获取站点编号
     * @return
     */
    public abstract String getSiteCode();

}
