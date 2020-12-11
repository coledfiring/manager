package com.whaty.products.service.hbgr.warning;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.warning.WarningCondition;
import com.whaty.domain.bean.hbgr.warning.WarningConstant;
import com.whaty.domain.bean.hbgr.yysj.PeWarning;
import com.whaty.domain.bean.rule.PrServiceConfig;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.hbgr.warning.strategy.AbstractWarningStrategy;
import com.whaty.products.service.hbgr.warning.strategy.factory.WarningFactory;
import com.whaty.products.service.hbgr.warning.strategy.sceneOne.SceneOneStrategy;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Lazy
@Service("peWarningManageService")
public class PeWarningManageServiceImpl extends TycjGridServiceAdapter<PeWarning> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeWarning bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PeWarning bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    public void checkBeforeAddOrUpdate(PeWarning bean) {
        if (this.myGeneralDao.getById(EnumConst.class, bean.getEnumConstByFlagIsvalid().getId()).getCode().equals("1")) {
            this.myGeneralDao.executeBySQL("UPDATE pe_warning SET flag_isvalid = '3' WHERE flag_scene = ?",
                    bean.getEnumConstByFlagScene().getId());
        }
    }

    private void checkAfterAddOrUpdate(PeWarning bean) {
        List<String> configIds = this.myGeneralDao.getBySQL("SELECT id FROM pr_service_config" +
                " WHERE link = ?", bean.getId());
        PrServiceConfig prServiceConfig = new PrServiceConfig();
        if (CollectionUtils.isNotEmpty(configIds)) {
            prServiceConfig.setId(configIds.get(0));
        }
        prServiceConfig.setEnumConstByFlagServiceType(this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagServiceType", "1"));
        prServiceConfig.setDatetime(new Date());
        prServiceConfig.setLink(bean.getId());
        prServiceConfig.setName("预警报警");
        prServiceConfig.setConfig("{\"w2b2gz\":\"0\",\"w2b3gz\":\"0\",\"gl1gz\":\"1\",\"w2b1gz\":\"1\",\"w1b1gz\":\"1\",\"w1b2gz\":\"1\",\"w1b3gz\":\"1\",\"w1b4gz\":\"1\",\"gl7gz\":\"1\",\"gl6gz\":\"1\",\"w2b6gz\":\"1\",\"gl3gz\":\"1\",\"gl2gz\":\"1\",\"w2b4gz\":\"1\",\"gl5gz\":\"1\",\"w2b5gz\":\"1\",\"gl4gz\":\"1\"}");
        this.myGeneralDao.save(prServiceConfig);
    }

    @Override
    protected void afterAdd(PeWarning bean) throws EntityException {
        checkAfterAddOrUpdate(bean);
    }

    public void sendWarining() throws IOException {
        SceneOneStrategy sceneOneStrategy = (SceneOneStrategy) getWarningStrategy("1");
        sceneOneStrategy.sendWarning();
    }

    private AbstractWarningStrategy getWarningStrategy(String code) {
        return WarningFactory.newInstance(code);
    }

    /**
     * 获取报警预警规则
     *
     * @return
     */
    @LogAndNotice("获取报警预警规则")
    public Map<String, Object> getRestriction(String parentId) {
        AbstractWarningStrategy abstractWarningStrategy =  this.getWarningStrategy((String) this.myGeneralDao
                .getBySQL("SELECT e.`CODE` FROM pe_warning w " +
                " INNER JOIN enum_const e ON e.ID = w.flag_scene " +
                " WHERE w.id = ?", parentId).get(0));
        return abstractWarningStrategy.getRestriction(parentId);
    }

    /**
     * 保存规则
     *
     * @return
     * @author zhangzhe
     */
    public void saveRestriction(String restrictionId, Map<String, Object> map) {
        PrServiceConfig prServiceConfig;
        if (StringUtils.isBlank(restrictionId)) {
            throw new ParameterIllegalException();
        }
        prServiceConfig = this.myGeneralDao.getById(PrServiceConfig.class, restrictionId);
        if (prServiceConfig == null) {
            throw new ServiceException("规则不存在");
        }
        prServiceConfig.setConfig(JSON.toJSONString(map));
        this.myGeneralDao.save(prServiceConfig);
    }
}
