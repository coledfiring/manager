package com.whaty.products.service.consumable.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeConsumableDetail;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 易耗品管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("peConsumableDetailManageService")
public class PeConsumableDetailManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeConsumableDetail> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(PeConsumableDetail bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddAndUpdate(bean);
        if (this.generalDao.checkNotEmpty("select 1 from pe_consumable_detail " +
                        " where fk_consumable_id = ? AND fk_consumable_item_id = ? ",
                bean.getPeConsumable().getId(), bean.getPeConsumableItem().getId())) {
            throw new EntityException("要添加的物品已经存在");
        }
    }

    /**
     * @param bean
     */
    private void checkBeforeAddAndUpdate(PeConsumableDetail bean) throws EntityException {
        TycjParameterAssert.isAllNotBlank(bean.getPeConsumable().getId());
        //只有待申请的可以修改
        if (this.generalDao.checkNotEmpty("select 1 from pe_consumable pc " +
                "INNER JOIN enum_const ec ON ec.id = pc.flag_use_status " +
                "where ec.`CODE` <> '0' AND pc.id = ? ", bean.getPeConsumable().getId())) {
            throw new EntityException("只有未提交申请的可以修改");
        }
    }


    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from pe_consumable pc " +
                " INNER JOIN pe_consumable_detail pcd ON  pc.id = pcd.fk_consumable_id " +
                " INNER JOIN enum_const ec ON ec.id = pc.flag_use_status " +
                " where ec.`CODE` <> '0' AND " + CommonUtils.madeSqlIn(idList, "pcd.id") +
                " GROUP BY pc.id ")) {
            throw new EntityException("只有未提交申请的可以删除");
        }
    }

    @Override
    public void checkBeforeUpdate(PeConsumableDetail bean) throws EntityException {
        super.checkBeforeUpdate(bean);
        this.checkBeforeAddAndUpdate(bean);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peConsumable.id";
    }
}