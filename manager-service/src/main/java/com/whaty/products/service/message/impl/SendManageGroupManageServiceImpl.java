package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.SendMessageGroup;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 发送消息组管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("sendManageGroupManageService")
public class SendManageGroupManageServiceImpl extends TycjGridServiceAdapter<SendMessageGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(SendMessageGroup bean, Map<String, Object> params) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(SendMessageGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     * @param bean
     */
    private void checkBeforeAddOrUpdate(SendMessageGroup bean) throws EntityException {
        String additionalSql = StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'";
        List<Object> list = this.myGeneralDao.getBySQL("select 1 from send_message_group where name = '" +
                bean.getName() + "'" + additionalSql);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同名称的组");
        }
        list = this.myGeneralDao.getBySQL("select 1 from send_message_group where code = '" + bean.getId()
                + "'" + additionalSql);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同编号的组");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from send_message_group g inner join enum_const ac on ac.id = g.flag_active " +
                        "where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "g.id"));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("不能删除有效的数据");
        }
    }
}
