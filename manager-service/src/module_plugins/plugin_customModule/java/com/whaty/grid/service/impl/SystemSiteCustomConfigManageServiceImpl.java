package com.whaty.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.custom.bean.SystemSiteCustomConfig;
import com.whaty.custom.handler.SystemCustomHandler;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 定制配置与站点关联管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("systemSiteCustomConfigManageService")
public class SystemSiteCustomConfigManageServiceImpl
        extends AbstractTwoLevelListGridServiceImpl<SystemSiteCustomConfig> {

    @Resource(name = "systemCustomHandler")
    private SystemCustomHandler systemCustomHandler;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(SystemSiteCustomConfig bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        bean.setPeWebSite(this.myGeneralDao.getById(PeWebSite.class, bean.getPeWebSite().getId()));
        this.removeCache(bean.getSystemCustomConfig().getId(), bean.getPeWebSite().getCode());
    }

    @Override
    public void checkBeforeUpdate(SystemSiteCustomConfig bean) throws EntityException {
        bean.setPeWebSite(this.myGeneralDao.getById(PeWebSite.class, bean.getPeWebSite().getId()));
        this.removeCache(bean.getSystemCustomConfig().getId(), bean.getPeWebSite().getCode());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        List<Object[]> configIdList = this.myGeneralDao
                .getBySQL("select cast(fk_system_custom_id as char) as confId, s.code as siteCode " +
                        "from system_site_custom_config conf inner join pe_web_site s on s.id = conf.fk_web_site_id" +
                        " where " + CommonUtils.madeSqlIn(idList, "conf.id"));
        configIdList.forEach(e -> this.removeCache((String) e[0], (String) e[1]));
    }

    /**
     * 添加或修改前检查
     * @param configId
     * @param siteCode
     */
    private void removeCache(String configId, String siteCode) {
        List<Object> urlList = this.myGeneralDao
                .getBySQL("select url from system_custom_config_url where fk_system_custom_id = '" + configId + "'");
        urlList.forEach(url -> this.systemCustomHandler.removeCustomConfigCache(siteCode, (String) url));
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "systemCustomConfig.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "systemCustomConfig.id";
    }

}
