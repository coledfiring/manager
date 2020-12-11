package com.whaty.products.service.consumable.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeConsumableItem;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 易耗品管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("peConsumableItemManageService")
public class PeConsumableItemManageServiceImpl extends TycjGridServiceAdapter<PeConsumableItem> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(PeConsumableItem bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
        bean.setCreateTime(new Date());
        super.checkBeforeAdd(bean);
    }

    /**
     * 验证物品名称，种类，站点是否重复
     *
     * @param bean
     */
    private void checkBeforeAddAndUpdate(PeConsumableItem bean) throws EntityException {
        TycjParameterAssert.isAllNotBlank(bean.getName());
        TycjParameterAssert.isAllNotNull(bean.getEnumConstByFlagConsumableType());
        if (this.generalDao.checkNotEmpty(String.format(" SELECT 1 FROM pe_consumable_item " +
                        " WHERE flag_consumable_type = ? AND site_code = ? AND name = ? %s",
                StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "' "),
                bean.getEnumConstByFlagConsumableType(), SiteUtil.getSiteCode(), bean.getName())) {
            throw new EntityException("已存在物品名称及种类均相同的数据");
        }
    }

    @Override
    public void checkBeforeUpdate(PeConsumableItem bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
        super.checkBeforeUpdate(bean);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        //删除验证
        if (this.generalDao.checkNotEmpty("select 1 from pe_consumable_item item " +
                "inner join enum_const fiv on fiv.id = item.flag_is_valid where fiv.code = '1' and " +
                CommonUtils.madeSqlIn(idList, "item.id"))) {
            throw new EntityException("有效的数据无法删除");
        }
        super.checkBeforeDelete(idList);
    }
}