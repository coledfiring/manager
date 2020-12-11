package com.whaty.core.framework.service.guide.impl;

import com.whaty.domain.bean.OperateGuideDescription;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.service.impl.GridServiceImpl;
import com.whaty.dao.GeneralDao;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管操作指导描述管理服务类
 * @author weipengsen
 */
@Lazy
@Service("superAdminOperateGuideManageService")
public class SuperAdminOperateGuideManageServiceImpl extends GridServiceImpl<OperateGuideDescription> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 检查对象是否符合添加修改校验
     * @param bean
     */
    private void checkBeforeAddOrUpdate(OperateGuideDescription bean) throws EntityException {
        String sql = "select 1 from operate_guide_description where name = '" + bean.getName()
                + "' and fk_web_site_id = '" + bean.getPeWebSite().getId() + "'";
        if (bean.getId() != null) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        List<Object> list = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("此名称在此站点已存在");
        }
        sql = "select 1 from operate_guide_description where serial_number = '" + bean.getSerialNumber()
                + "' and fk_web_site_id = '" + bean.getPeWebSite().getId() + "'";
        if (bean.getId() != null) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        list = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("此序号在此站点已存在");
        }
    }

    @Override
    public void checkBeforeAdd(OperateGuideDescription bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(OperateGuideDescription bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                ");
        sql.append(" 	1                                                  ");
        sql.append(" FROM                                                  ");
        sql.append(" 	operate_guide_description des                      ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = des.flag_active   ");
        sql.append(" WHERE                                                 ");
        sql.append(" 	ac. CODE = '1'                                     ");
        sql.append(" AND " + CommonUtils.madeSqlIn(idList, "des.id"));
        List<Object> list = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("存在是否有效为是的数据无法删除");
        }
    }
}
