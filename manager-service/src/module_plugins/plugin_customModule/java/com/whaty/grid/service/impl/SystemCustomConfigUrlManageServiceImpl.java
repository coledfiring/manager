package com.whaty.grid.service.impl;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.custom.bean.SystemCustomConfigUrl;
import com.whaty.custom.handler.SystemCustomHandler;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定制配置与url关联管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("systemCustomConfigUrlManageService")
public class SystemCustomConfigUrlManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<SystemCustomConfigUrl> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "systemCustomHandler")
    private SystemCustomHandler systemCustomHandler;

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        List<Object> urlList = this.myGeneralDao
                .getBySQL("select url from system_custom_config_url where " + CommonUtils.madeSqlIn(idList, "id"));
        SiteUtil.listSite().forEach(e ->
            urlList.forEach(url -> this.systemCustomHandler.removeCustomConfigCache(e.getCode(), (String) url)));
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
