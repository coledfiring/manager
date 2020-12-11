package com.whaty.products.service.oltrain.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlEnrollColumnRegulation;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 报名规则
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olEnrollColumnRegulationService")
public class OlEnrollColumnRegulationServiceImpl extends TycjGridServiceAdapter<OlEnrollColumnRegulation> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 设置为默认
     * @param ids
     */
    public void doSetToDefault(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        EnumConst noDefault = this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_IS_DEFAULT, "0");
        EnumConst isDefault = this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_IS_DEFAULT, "1");
        this.generalDao.executeBySQL("update ol_enroll_column_regulation set flag_is_default = ? where site_code = ?",
                noDefault.getId(), SiteUtil.getSiteCode());
        this.generalDao.executeBySQL("update ol_enroll_column_regulation set flag_is_default = ? where id = ?",
                isDefault.getId(), ids);
    }

    @Override
    public void checkBeforeAdd(OlEnrollColumnRegulation bean) throws EntityException {
        bean.setCreateBy(UserUtils.getCurrentUserId());
        bean.setCreateDate(new Date());
        bean.setEnumConstByFlagActive(this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ACTIVE, "1"));
        bean.setEnumConstByFlagIsDefault(this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_IS_DEFAULT, "0"));
    }

    @Override
    public void checkBeforeUpdate(OlEnrollColumnRegulation bean) throws EntityException {
        bean.setUpdateBy(UserUtils.getCurrentUserId());
        bean.setUpdateDate(new Date());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select from ol_enroll_column_regulation reg " +
                "inner join enum_const de on de.id = reg.flag_is_default where de.code = '1' where " +
                CommonUtils.madeSqlIn(idList, "reg.id"))) {
            throw new EntityException("不能删除默认规则");
        }
        if (this.generalDao.checkNotEmpty("select from ol_enroll_column_regulation reg " +
                "inner join enum_const ac on ac.id = reg.flag_active where ac.code = '1' where " +
                CommonUtils.madeSqlIn(idList, "reg.id"))) {
            throw new EntityException("不能删除有效的规则");
        }
    }

    @Override
    protected String getSiteCode() {
        return SiteUtil.getSiteCode();
    }

}
