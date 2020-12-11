package com.whaty.products.service.superadmin.impl;

import com.whaty.domain.bean.PeWebSiteConfig;
import com.whaty.framework.config.BasicConfigHelperManagement;
import com.whaty.framework.config.domain.IConfig;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.ValidateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 站点配置管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("webSiteConfigManageService")
public class WebSiteConfigManageServiceImpl extends TycjGridServiceAdapter<PeWebSiteConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeWebSiteConfig bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(PeWebSiteConfig bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateDate(new Date());
    }

    /**
     * 添加或修改前检查
     *
     * @param bean
     */
    private void checkBeforeAddOrUpdate(PeWebSiteConfig bean) throws EntityException {
        bean.setEnumConstByFlagConfigType(this.myGeneralDao.getById(EnumConst.class,
                bean.getEnumConstByFlagConfigType().getId()));
        String additional = StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'";
        // 检查站点类型唯一
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from pe_web_site_config where fk_web_site_id = '" + bean.getPeWebSite().getId() +
                        "' AND flag_config_type = '" + bean.getEnumConstByFlagConfigType().getId() + "'" + additional);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("此站点已存在同类型配置");
        }
        // 检查类型样例唯一
        if ("1".equals(bean.getIsExample())) {
            list = this.myGeneralDao
                    .getBySQL("select 1 from pe_web_site_config where flag_config_type = '" +
                            bean.getEnumConstByFlagConfigType().getId() + "' and is_example = '1'" + additional);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new EntityException("此类型已存在样例配置");
            }
        }
        // 检查配置不为空
        if (StringUtils.isBlank(bean.getConfig())) {
            throw new EntityException("配置不能为空");
        }
        // 检查配置合法
        this.checkConfigInvalid(bean);
    }

    @Override
    protected void afterAdd(PeWebSiteConfig bean) throws EntityException {
        this.afterAddOrUpdate(bean);
    }

    @Override
    protected void afterUpdate(PeWebSiteConfig bean) throws EntityException {
        this.afterAddOrUpdate(bean);
    }

    /**
     * 添加或修改后的操作
     * @param bean
     */
    private void afterAddOrUpdate(PeWebSiteConfig bean) {
        BasicConfigHelperManagement.getHelperByType(bean.getEnumConstByFlagConfigType().getCode())
                .updateRemoteVersion(System.currentTimeMillis());
    }

    /**
     * 检查【配置是否合法
     * @param bean
     * @throws EntityException
     */
    private void checkConfigInvalid(PeWebSiteConfig bean) throws EntityException {
        Class<? extends IConfig> configClass = BasicConfigHelperManagement
                .getHelperByType(bean.getEnumConstByFlagConfigType().getCode()).getConfigClass();
        Set<ConstraintViolation<IConfig>> violations =  ValidateUtils.checkConstraintValidate(
                (IConfig) JSONObject.toBean(JSONObject.fromObject(bean.getConfig()), configClass));
        if (CollectionUtils.isNotEmpty(violations)) {
            StringBuilder message = new StringBuilder("配置非法:");
            violations.stream().map(e -> String.format("%s %s;", e.getPropertyPath(), e.getMessage()))
                    .forEach(message::append);
            throw new EntityException(message.toString());
        }
    }

    @Override
    public void afterExcelImport(List<PeWebSiteConfig> beanList) throws EntityException {
        beanList.stream().map(e -> e.getEnumConstByFlagConfigType().getCode()).distinct()
                .forEach(e -> BasicConfigHelperManagement.getHelperByType(e)
                        .updateRemoteVersion(System.currentTimeMillis()));
    }
}
