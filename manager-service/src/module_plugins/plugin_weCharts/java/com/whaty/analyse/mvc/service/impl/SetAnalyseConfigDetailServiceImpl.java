package com.whaty.analyse.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.mvc.service.SetAnalyseConfigDetailService;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 设置统计配置详情
 *
 * @author weipengsen
 */
@Lazy
@Service("setAnalyseConfigDetailService")
public class SetAnalyseConfigDetailServiceImpl implements SetAnalyseConfigDetailService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getConfigById(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        Map<String, Object> info = this.generalDao
                .getOneMapBySQL("SELECT conf.id AS id, conf.name as name, ty. NAME AS type, ty. CODE AS typeCode," +
                        " de.config AS config FROM analyse_basic_config conf " +
                        " INNER JOIN enum_const ty ON ty.id = conf.flag_analyse_type" +
                        " INNER JOIN analyse_config_detail de ON de.fk_basic_config_id = conf.id" +
                        " WHERE conf.id = ?", id);
        TycjParameterAssert.isAllNotNull(info);
        /*info.computeIfPresent("config", (k, o) ->
                JSON.parseObject((String) o, AnalyseType.getConfigDO((String) info.get("typeCode"))));*/
        return info;
    }

    @Override
    public void saveConfig(String id, String typeCode, String config) {
        TycjParameterAssert.isAllNotBlank(id, typeCode, config);
        TycjParameterAssert.validatePass(JSON.parseObject(config, AnalyseType.getConfigDO(typeCode)));
        EnumConst analyseType = this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ANALYSE_TYPE, typeCode);
        TycjParameterAssert.isAllNotNull(analyseType);
        this.generalDao.executeBySQL("update analyse_basic_config set flag_analyse_type = ? where id = ?",
                analyseType.getId(), id);
        this.generalDao.executeBySQL("insert into analyse_config_detail(id, config, fk_basic_config_id) " +
                "values(replace(uuid(), '-', ''), ?, ?) on duplicate key update config = ?", config, id, config);
    }

}
