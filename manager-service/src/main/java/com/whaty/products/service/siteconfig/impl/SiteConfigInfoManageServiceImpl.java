package com.whaty.products.service.siteconfig.impl;

import com.whaty.domain.bean.PrColumnsConfig;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.siteconfig.SiteConfigInfoManageService;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点信息配置管理管理服务类
 * @author suoqiangqiang
 */
@Lazy
@Service("siteConfigInfoManageService")
public class SiteConfigInfoManageServiceImpl implements SiteConfigInfoManageService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;


    @Override
    public Map<String, Object> getRegularJson(String name) {
        List<Map<String, Object>> regularMapList = generalDao
                .getMapBySQL(" select value from system_variables where name = ? and site_code = ?", name,
                        SiteUtil.getSiteCode());
        if (CollectionUtils.isEmpty(regularMapList)) {
            throw new ParameterIllegalException();
        }
        String regularStr = (String) regularMapList.get(0).get("value");
        if (StringUtils.isBlank(regularStr)) {
            throw new ParameterIllegalException();
        }
        JSONObject regularJson = JSONObject.fromObject(regularStr);
        return new HashMap<String, Object>() {{
            put("regularJson", regularJson);
        }};
    }


    @Override
    public void saveRegularJson(String name, String regularJson) {
        generalDao.executeBySQL(" update system_variables set value = ? where name = ? and site_code = ?",
                regularJson, name, SiteUtil.getSiteCode());
    }

    @Override
    public Map<String, Object> getEnrollInfoConfig() {
        DetachedCriteria dc = DetachedCriteria.forClass(PrColumnsConfig.class).addOrder(Order.asc("myOrder"));
        List<PrColumnsConfig> configList = this.generalDao.getList(dc);
        List<Map<String, Object>> infoTypeList = this.generalDao
                .getMapBySQL("SELECT id,name FROM enum_const WHERE NAMESPACE = 'FlagInfoType' ORDER BY CODE");
        return new HashMap<String, Object>() {{
            put("config", configList);
            put("infoType", infoTypeList);
        }};
    }
}
