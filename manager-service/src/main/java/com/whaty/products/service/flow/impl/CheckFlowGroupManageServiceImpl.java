package com.whaty.products.service.flow.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.flow.CheckFlowGroup;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审核流程组管理
 *
 * @author weipengsen
 */
@Lazy
@Service("checkFlowGroupManageService")
public class CheckFlowGroupManageServiceImpl extends TycjGridServiceAdapter<CheckFlowGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(CheckFlowGroup bean, Map<String, Object> params) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setEnumConstByFlagCheckStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "1"));
        bean.setEnumConstByFlagActive(this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "0"));
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(CheckFlowGroup bean) throws EntityException {
        if ("1".equals(bean.getEnumConstByFlagActive().getCode())) {
            throw new EntityException("有效的数据无法修改");
        }
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from check_flow_group g " +
                "inner join enum_const ac on ac.id = g.flag_active where ac.code = '1' and " +
                CommonUtils.madeSqlIn(idList, "g.id"))) {
            throw new EntityException("有效的数据无法删除");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from check_flow_node where " +
                CommonUtils.madeSqlIn(idList, "fk_flow_group_id"))) {
            throw new EntityException("删除的审批流程中存在审批人节点，请全部清空后再删除");
        }
    }

    @Override
    protected void afterDelete(List idList) {
        this.myGeneralDao.executeBySQL("DELETE FROM check_flow_copy_person WHERE " +
                        "id not in (SELECT fk_copy_person_detail_id FROM check_flow_group)");
    }


    private void checkBeforeAddOrUpdate(CheckFlowGroup bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from check_flow_group where name = ? and site_code = ?"
                        + additionalSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称以存在");
        }
    }

    /**
     * 设置有效或者无效
     * @param ids
     * @param code
     */
    public void doSetActive(String ids, String code) {
        TycjParameterAssert.isAllNotBlank(ids, code);
        if (this.myGeneralDao.checkNotEmpty("select 1 from check_flow_group g " +
                "inner join enum_const ac on ac.id = g.flag_active where ac.code = ? and "
                + CommonUtils.madeSqlIn(ids, "g.id"), code)) {
            throw new ServiceException("只有的" + ("1".equals(code) ? "无效" : "有效") + "数据才可以操作");
        }
        if ("1".equals(code)) {
            this.checkAuditorNotEmpty(ids);
        }
        EnumConst active = this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, code);
        this.myGeneralDao.executeBySQL("update check_flow_group set flag_active = ? where " +
                CommonUtils.madeSqlIn(ids, "id"), active.getId());
    }

    /**
     * 检查审批人是否不为空
     * @param groupId
     */
    private void checkAuditorNotEmpty(String groupId) {
        String sql = "SELECT 1 FROM check_flow_group g " +
                " inner join enum_const nc on nc.id = g.flag_need_check " +
                " left join check_flow_node node on g.id = node.fk_flow_group_id" +
                " left join enum_const nt on nt.id = node.flag_node_type " +
                " LEFT JOIN check_flow_auditor aud ON aud.id = node.fk_auditor_detail_id " +
                " WHERE (node.id is null or ((aud.auditor is null or aud.auditor = '') and nt.code = '1')) " +
                " and nc.code = '1'" +
                " and " + CommonUtils.madeSqlIn(groupId, "g.id");
        if (this.myGeneralDao.checkNotEmpty(sql)) {
            throw new ServiceException("存在是否需要审批为是，且没有审批节点或存在指定成员但审批人未选择的审批节点，无法设置为有效");
        }
    }
}
