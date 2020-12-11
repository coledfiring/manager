package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.StationMessageGroup;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.service.impl.GridServiceImpl;
import com.whaty.dao.GeneralDao;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管站内信组管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("superAdminStationMessageGroupManageService")
public class SuperAdminStationMessageGroupManageServiceImpl extends GridServiceImpl<StationMessageGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(StationMessageGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(StationMessageGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        boolean codeIsChange = !this.myGeneralDao.checkNotEmpty("select 1 from station_message_group where code = ? and id = ?",
                bean.getCode(), bean.getId());
        boolean codeIsReference = this.myGeneralDao.checkNotEmpty("select 1 from station_message_group g " +
                "inner join send_message_type t on t.message_code = g.code where g.id = ?", bean.getId());
        if (codeIsChange && codeIsReference) {
            throw new EntityException("此组被发送消息组引用，无法更改编号");
        }
    }

    /**
     * 添加或修改前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(StationMessageGroup bean) throws EntityException {
        String sql = "select 1 from station_message_group where (code = '" + bean.getCode() + "' or name = '"
                + bean.getName() + "')";
        if (StringUtils.isNotBlank(bean.getId())) {
            sql += " AND id <> '" + bean.getId() + "'";
        }
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("此名称或编号已存在");
        }
        if (!bean.getDataSql().contains("userId")) {
            throw new EntityException("sql中必须存在userId的别名用于定位信息发送对象");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        String sql = "select 1 from station_message_group gro inner join enum_const ac on ac.id = gro.flag_active " +
                "where ac.code = '1' and " + CommonUtils.madeSqlIn(idList, "gro.id");
        List<Object> checkList = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new EntityException("存在有效的数据无法删除");
        }
        boolean codeIsReference = this.myGeneralDao.checkNotEmpty("select 1 from station_message_group g " +
                "inner join send_message_type t on t.message_code = g.code where " +
                CommonUtils.madeSqlIn(idList, "g.id"));
        if (codeIsReference) {
            throw new EntityException("此组被发送消息组引用，无法删除");
        }
    }
}
