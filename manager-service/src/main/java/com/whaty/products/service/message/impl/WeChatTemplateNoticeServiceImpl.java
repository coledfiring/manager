package com.whaty.products.service.message.impl;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.message.WeChatTemplateNoticeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信模板消息服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatTemplateNoticeService")
public class WeChatTemplateNoticeServiceImpl implements WeChatTemplateNoticeService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getTemplateConfig(String templateCode) {
        if (StringUtils.isBlank(templateCode)) {
            throw new ParameterIllegalException();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                        ");
        sql.append("  grou.name as name                                                            ");
        sql.append(" FROM                                                                          ");
        sql.append(" 	wechat_template_message_group grou                                         ");
        sql.append(" INNER JOIN wechat_template_message_site s ON s.fk_template_group_id = grou.id ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = grou.flag_active                          ");
        sql.append(" WHERE                                                                         ");
        sql.append(" 	grou.code = '" + templateCode + "'                                         ");
        sql.append(" AND s.fk_web_site_id = '" + SiteUtil.getSite().getId() + "'                   ");
        sql.append(" AND ac.code = '1'                                                             ");
        String name = this.generalDao.getOneBySQL(sql.toString());
        if (StringUtils.isBlank(name)) {
            throw new ServiceException("此模板未配置");
        }
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                               ");
        sql.append("  col.label as label,                                                                 ");
        sql.append("  col.sign as sign,                                                                   ");
        sql.append("  col.is_dynamic as isDynamic,                                                        ");
        sql.append("  col.template_text as template                                                       ");
        sql.append(" FROM                                                                                 ");
        sql.append(" 	wechat_template_message_column col                                                ");
        sql.append(" INNER JOIN wechat_template_message_group grou on grou.id = col.fk_template_group_id  ");
        sql.append(" INNER JOIN wechat_template_message_site s ON s.fk_template_group_id = grou.id        ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = grou.flag_active                                 ");
        sql.append(" INNER JOIN enum_const ac2 ON ac2.id = col.flag_active                                ");
        sql.append(" WHERE                                                                                ");
        sql.append(" 	grou.code = '" + templateCode + "'                                                ");
        sql.append(" AND s.fk_web_site_id = '" + SiteUtil.getSite().getId() + "'                          ");
        sql.append(" AND ac.code = '1'                                                                    ");
        sql.append(" AND ac2.code = '1'                                                                   ");
        List<Map<String, Object>> templateConfig = this.generalDao.getMapBySQL(sql.toString());
        if (CollectionUtils.isEmpty(templateConfig)) {
            throw new ServiceException("此模板未配置");
        }
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("name", name);
        resultMap.put("templateConfig", templateConfig);
        return resultMap;
    }

}
