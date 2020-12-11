package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.WeChatTemplateMessageGroup;
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

/**
 * 微信模板消息组管理
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatTemplateGroupManageService")
public class WeChatTemplateGroupManageServiceImpl extends TycjGridServiceAdapter<WeChatTemplateMessageGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(WeChatTemplateMessageGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(WeChatTemplateMessageGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        boolean codeIsChange = !this.myGeneralDao
                .checkNotEmpty("select 1 from wechat_template_message_group where code = ? and id = ?",
                bean.getCode(), bean.getId());
        boolean codeIsReference = this.myGeneralDao.checkNotEmpty("select 1 from wechat_template_message_group g " +
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
    private void checkBeforeAddOrUpdate(WeChatTemplateMessageGroup bean) throws EntityException {
        String additional = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from wechat_template_message_group where name = '"
                        + bean.getName() + "'" + additional);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同名称的组");
        }
        list = this.myGeneralDao
                .getBySQL("select 1 from wechat_template_message_group where code = '"
                        + bean.getCode() + "'" + additional);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同编号的组");
        }
        if (!bean.getDataSql().contains("openId")) {
            throw new EntityException("sql必须查询出open_id，且以openId作为别名");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from wechat_template_message_group g " +
                        "inner join enum_const ac on ac.id = g.flag_active where ac.code = '1' AND "
                        + CommonUtils.madeSqlIn(idList, "g.id"));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("不能删除有效的数据");
        }
        boolean codeIsReference = this.myGeneralDao.checkNotEmpty("select 1 from wechat_template_message_group g " +
                "inner join send_message_type t on t.message_code = g.code where " +
                CommonUtils.madeSqlIn(idList, "g.id"));
        if (codeIsReference) {
            throw new EntityException("此组被发送消息组引用，无法删除");
        }
    }
}
