package com.whaty.products.service.basic.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.ColumnConfig;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.EntrustedUnit;
import com.whaty.domain.bean.EntrustedUnitLinkman;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.training.domain.EntrustedUnitWithLink;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 委托单位管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("entrustedUnitManageService")
public class EntrustedUnitManageServiceImpl extends TycjGridServiceAdapter<EntrustedUnit> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "entrustedUnitLinkmanManageService")
    private EntrustedUnitLinkmanManageServiceImpl entrustedUnitLinkmanManageService;

    @Override
    public GridConfig initGrid(String siteCode, String actionId, Map<String, Object> mapParam) {
        GridConfig gridConfig = super.initGrid(siteCode, actionId, mapParam);
        ColumnConfig colum = gridConfig.getColumByDateIndex("peArea.name");
        if (colum != null) {
            colum.setType("remote_select");
        }
        return gridConfig;
    }

    @Override
    public void checkBeforeAdd(EntrustedUnit bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(EntrustedUnit bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     */
    private void checkBeforeAddOrUpdate(EntrustedUnit bean) throws EntityException {
        String additionSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from entrusted_unit where name = ? and site_code = ?"
                + additionSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称已存在");
        }
    }

    /**
     * 创建委托单位
     * @param bean
     */
    public void addEntrustedUnit(EntrustedUnitWithLink bean) {
        EntrustedUnit unit = bean.generateEntrustedUnit();
        try {
            this.checkBeforeAddOrUpdate(unit);
            this.myGeneralDao.save(unit);
            this.afterAdd(unit);
        } catch (EntityException e) {
            throw new ServiceException(e.getMessage());
        }

        try {
            EntrustedUnitLinkman linkman = bean.generateEntrustedUnitLinkman(unit);
            this.entrustedUnitLinkmanManageService.checkBeforeAdd(linkman);
            this.myGeneralDao.save(linkman);
            this.entrustedUnitLinkmanManageService.afterAdd(linkman);
        } catch (EntityException e) {
            throw new ServiceException(e.getMessage());
        }

    }
}
