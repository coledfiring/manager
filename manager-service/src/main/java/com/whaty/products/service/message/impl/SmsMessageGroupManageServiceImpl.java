package com.whaty.products.service.message.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.message.SmsMessageGroup;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 短信消息组管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("smsMessageGroupManageService")
public class SmsMessageGroupManageServiceImpl extends TycjGridServiceAdapter<SmsMessageGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        String sql = "select 1 from sms_message_group g inner join enum_const ac on ac.id = g.flag_active"
                + " where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "g.id");
        if (this.generalDao.checkNotEmpty(sql)) {
            throw new EntityException("存在有效的数据无法删除");
        }
    }
}
