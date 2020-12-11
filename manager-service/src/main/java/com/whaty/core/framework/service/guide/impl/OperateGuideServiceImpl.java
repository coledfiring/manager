package com.whaty.core.framework.service.guide.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.service.guide.OperateGuideService;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 操作指导服务类
 * @author weipengsen
 */
@Lazy
@Service("operateGuideService")
public class OperateGuideServiceImpl implements OperateGuideService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public List<Map<String, Object>> listOperateGuideDescriptionCanShow() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                             ");
        sql.append(" 	des. NAME AS name,                                              ");
        sql.append(" 	des.icon AS icon,                                               ");
        sql.append(" 	des.description AS description,                                 ");
        sql.append(" 	fl.id AS guideId                                                ");
        sql.append(" FROM                                                               ");
        sql.append(" 	operate_guide_description des                                   ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = des.flag_active                ");
        sql.append(" INNER JOIN flow_config fl ON fl.id = des.fk_flow_config_id         ");
        sql.append(" WHERE                                                              ");
        sql.append(" 	ac. CODE = '1'                                                  ");
        sql.append(" AND des.fk_web_site_id = '" + SiteUtil.getSiteId() + "'            ");
        sql.append(" ORDER BY                                                           ");
        sql.append("    des.serial_number                                               ");
        return this.myGeneralDao.getMapBySQL(sql.toString());
    }

    @Override
    public void doSetOperateGuideIcon(String ids, String icon) {
        if (StringUtils.isBlank(ids) || StringUtils.isBlank(icon)) {
            throw new ParameterIllegalException();
        }
        this.myGeneralDao.executeBySQL("update operate_guide_description set icon = '" + icon + "' where "
                + CommonUtils.madeSqlIn(ids, "id"));
    }
}
