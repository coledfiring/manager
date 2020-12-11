package com.whaty.products.service.message.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.message.WeChatGroupNoticeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 微信群发通知服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatGroupNoticeServiceImpl")
public class WeChatGroupNoticeServiceImpl implements WeChatGroupNoticeService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public List<Map<String, Object>> listTemplates() {
        String sql = "SELECT id as id, NAME as name, content as content, create_date as createDate"
                + " FROM wechat_group_message_template";
        return this.generalDao.getMapBySQL(sql);
    }

    @Override
    @LogAndNotice("保存微信群发消息模板")
    public void saveTemplate(String name, String content) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(content)) {
            throw new ParameterIllegalException();
        }
        String sql = "insert into wechat_group_message_template(name, content, create_date) values('"
                + name + "', '" + content + "', now())";
        this.generalDao.executeBySQL(sql);
    }

    @Override
    @LogAndNotice("修改微信群发消息模板")
    public void updateTemplate(String id, String name, String content) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(name) || StringUtils.isBlank(content)) {
            throw new ParameterIllegalException();
        }
        String sql = "update wechat_group_message_template set name = '" + name + "', content = '" + content
                + "' where id = '" + id + "'";
        this.generalDao.executeBySQL(sql);
    }

    @Override
    @LogAndNotice("删除微信群发消息模板")
    public void deleteTemplate(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ParameterIllegalException();
        }
        String sql = "delete from wechat_group_message_template where id = '" + id + "'";
        this.generalDao.executeBySQL(sql);
    }
}
