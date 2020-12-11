package com.whaty.grid.service.impl;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.custom.bean.SystemCustomConfig;
import com.whaty.custom.handler.SystemCustomHandler;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定制配置管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("systemCustomConfigManageService")
public class SystemCustomConfigManageServiceImpl extends TycjGridServiceAdapter<SystemCustomConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "systemCustomHandler")
    private SystemCustomHandler systemCustomHandler;

    @Override
    public void checkBeforeUpdate(SystemCustomConfig bean) throws EntityException {
        SiteUtil.listSite().forEach(e -> this.removeCache(e.getCode(), bean.getId()));
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        SiteUtil.listSite().forEach(e -> idList.forEach(id -> this.removeCache(e.getCode(), (String) id)));
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
}
