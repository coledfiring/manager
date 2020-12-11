package com.whaty.products.service.hbgr.energyControl;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.rule.PrServiceConfig;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.hbgr.energyControl.strategy.AbstractEnergyControlStrategy;
import com.whaty.products.service.hbgr.energyControl.strategy.factory.EnergyControlFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Lazy
@Service("peEnergyControlService")
public class PeEnergyControlServiceImpl extends TycjGridServiceAdapter<EnumConst> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 获取控制规则
     *
     * @return
     */
    @LogAndNotice("获取控制规则")
    public List<Map<String, Object>> getRestriction(String parentId) throws Exception {
        return getEnergyControlStrategy(parentId).getRestriction(parentId);
    }

    private AbstractEnergyControlStrategy getEnergyControlStrategy(String parentId) {
        return this.getEnergyConstolStrategy((String) this.myGeneralDao
                .getBySQL("SELECT `CODE` FROM  enum_const WHERE id = ?", parentId).get(0));
    }

    /**
     * 获取l联动控制规则
     *
     * @return
     */
    @LogAndNotice("获取控制规则")
    public List<Map<String, Object>> getParameterInfo(String parentId) throws Exception {
        return getEnergyControlStrategy(parentId).getParameterInfo(parentId);
    }

    /**
     * 获取管井控制
     *
     * @return
     */
    @LogAndNotice("获取管井控制")
    public List<Map<String, Object>> getTubeWellControl(String parentId) throws Exception {
        return getEnergyControlStrategy(parentId).getTubeWellControl(parentId);
    }

    /**
     * 设置控制规则
     *
     * @param restrictionId
     * @param map
     */
    public void setControlRestrictions(String restrictionId, Map<String, Object> map) throws Exception {
        PrServiceConfig prServiceConfig;
        if (StringUtils.isBlank(restrictionId)) {
            throw new ParameterIllegalException();
        }
        prServiceConfig = this.myGeneralDao.getById(PrServiceConfig.class, restrictionId);
        if (prServiceConfig == null) {
            throw new ServiceException("规则不存在");
        }
        Map controlMap = JSON.parseObject(prServiceConfig.getConfig(), Map.class);
        controlMap.putAll(map);
        prServiceConfig.setConfig(JSON.toJSONString(controlMap));
        this.myGeneralDao.executeBySQL("UPDATE pr_service_config SET config = ? WHERE id = ?",
                prServiceConfig.getConfig(), prServiceConfig.getId());
        getEnergyControlStrategy(prServiceConfig.getLink()).setControlDevice(controlMap);
    }

    public void setParameterInfo(String restrictionId, Map<String, Object> map) throws Exception {
        PrServiceConfig prServiceConfig;
        if (StringUtils.isBlank(restrictionId)) {
            throw new ParameterIllegalException();
        }
        prServiceConfig = this.myGeneralDao.getById(PrServiceConfig.class, restrictionId);
        if (prServiceConfig == null) {
            throw new ServiceException("规则不存在");
        }
        getEnergyControlStrategy(prServiceConfig.getLink()).setParameterInfo(map);
    }

    public void setTubeWellControl(String restrictionId, Map<String, Object> map) throws Exception {
        PrServiceConfig prServiceConfig;
        if (StringUtils.isBlank(restrictionId)) {
            throw new ParameterIllegalException();
        }
        prServiceConfig = this.myGeneralDao.getById(PrServiceConfig.class, restrictionId);
        if (prServiceConfig == null) {
            throw new ServiceException("规则不存在");
        }
        getEnergyControlStrategy(prServiceConfig.getLink()).setTubeWellControl(map);
    }


    private AbstractEnergyControlStrategy getEnergyConstolStrategy(String code) {
        return EnergyControlFactory.newInstance(code);
    }
}
