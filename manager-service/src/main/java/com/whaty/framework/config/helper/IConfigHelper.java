package com.whaty.framework.config.helper;

import com.whaty.domain.bean.PeWebSiteConfig;
import com.whaty.framework.config.domain.IConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.util.ValidateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置辅助类接口
 *
 * @author weipengsen
 */
public interface IConfigHelper<T extends IConfig> {

    /**
     * 加载配置
     */
    void loadConfig();

    /**
     * 获取配置
     * @return
     */
    default Map<String, T> listConfigs() {
        DetachedCriteria dc = DetachedCriteria.forClass(PeWebSiteConfig.class);
        dc.createCriteria("peWebSite", "peWebSite")
                .add(Restrictions.eq("activeStatus", 1));
        dc.createCriteria("enumConstByFlagConfigType", "enumConstByFlagConfigType")
                .add(Restrictions.eq("code", this.getConfigTypeCode()));
        List<PeWebSiteConfig> configList = this.getGeneralDao().getList(dc);
        if (CollectionUtils.isEmpty(configList)) {
            return null;
        }
        Map<String, T> result = configList.stream()
                .collect(Collectors.toMap(e -> e.getPeWebSite().getCode(),
                        e -> (T) JSONObject.toBean(JSONObject.fromObject(e.getConfig()),
                                this.getConfigClass())));
        Map<String, List<String>> violation = result.entrySet().stream()
                .filter(e -> !ValidateUtils.checkPassConstraintValidate(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> ValidateUtils.checkConstraintValidate(e.getValue()).stream()
                        .map(v -> String.format("%s %s", v.getPropertyPath(), v.getMessage()))
                                .collect(Collectors.toList())));
        if (MapUtils.isNotEmpty(violation)) {
            StringBuilder message = new StringBuilder(String
                    .format("found constraint violation in %s config :", this.getConfigClass()));
            violation.forEach((k, v) -> message.append(String.format("{%s: %s}", k, v)));
            throw new IllegalArgumentException(message.toString());
        }
        return result;
    }

    /**
     * 获取查询使用的dao
     * @return
     */
    GeneralDao getGeneralDao();

    /**
     * 获取配置类型code
     * @return
     */
    String getConfigTypeCode();

    /**
     * 获取配置的class
     * @return
     */
    Class<T> getConfigClass();

}
