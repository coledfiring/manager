package com.whaty.products.service.siteconfig.impl;

import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.SystemVariables;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.supergrid.service.impl.SuperAdminGridServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统常量管理服务类
 * @author suoqiangqiang
 */
@Lazy
@Service("systemVariablesManageService")
public class SystemVariablesManageServiceImpl extends SuperAdminGridServiceImpl<SystemVariables> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Override
    public void checkBeforeUpdate(SystemVariables bean) throws EntityException {
        nameIsExist(bean);
    }

    @Override
    public void checkBeforeAdd(SystemVariables bean) throws EntityException {
        this.redisCacheService.putToCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(), bean.getName(),
                SiteUtil.getSiteCode()), bean.getValue());
    }

    @Override
    protected void afterUpdate(SystemVariables bean) {
        this.redisCacheService.putToCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(), bean.getName(),
                SiteUtil.getSiteCode()), bean.getValue());
    }

    @Override
    protected void afterAdd(SystemVariables bean) throws EntityException {
        this.redisCacheService.putToCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(), bean.getName(),
                SiteUtil.getSiteCode()), bean);
    }

    /**
     * 验证常量名称是否已存在
     * @author suoqiangqiang
     */
    private void nameIsExist(SystemVariables bean) throws EntityException {
        String sql = "select 1 from system_variables where name = ? and site_code = ?";
        if (bean.getId() != null) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        List countList = this.myGeneralDao.getBySQL(sql, bean.getName(), MasterSlaveRoutingDataSource.getDbType());
        if (CollectionUtils.isNotEmpty(countList)) {
            throw new EntityException("操作失败，名称已存在！");
        }
    }
}
