package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.EmailMessageGroup;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 邮件消息组管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("emailMessageGroupManageService")
public class EmailMessageGroupManageServiceImpl extends TycjGridServiceAdapter<EmailMessageGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        String sql = "select 1 from email_message_group g inner join enum_const ac on ac.id = g.flag_active" +
                " where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "g.id");
        if (this.generalDao.checkNotEmpty(sql)) {
            throw new EntityException("存在有效的数据无法删除");
        }
    }
}
