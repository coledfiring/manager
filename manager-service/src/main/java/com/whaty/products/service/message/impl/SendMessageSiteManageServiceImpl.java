package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.SendMessageSite;
import com.whaty.domain.bean.message.SendMessageType;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 发送消息站点关联管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("sendMessageSiteManageService")
public class SendMessageSiteManageServiceImpl extends TycjGridServiceAdapter<SendMessageSite> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(SendMessageSite bean, Map<String, Object> params) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(SendMessageSite bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(SendMessageSite bean) throws EntityException {
        SendMessageType type = (SendMessageType) this.myGeneralDao
                .getOneByHQL("from SendMessageType where sendMessageGroup.id = '" +
                        bean.getSendMessageType().getSendMessageGroup().getId() +
                        "' AND enumConstByFlagMessageType.id = '" + bean.getSendMessageType()
                        .getEnumConstByFlagMessageType().getId() + "'");
        if (type == null) {
            throw new EntityException("不存在此消息类型");
        }
        bean.setSendMessageType(type);
    }

}
