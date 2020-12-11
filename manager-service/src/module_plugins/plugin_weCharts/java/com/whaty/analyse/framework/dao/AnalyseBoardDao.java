package com.whaty.analyse.framework.dao;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardCondition;
import com.whaty.analyse.framework.domain.bean.AnalyseBoardConfig;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计看板dao
 *
 * @author weipengsen
 */
@Component("analyseBoardDao")
public class AnalyseBoardDao {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 通过id获取看板配置
     * @param id
     * @return
     */
    public AnalyseBoardConfig getById(String id) {
        return this.generalDao.getById(AnalyseBoardConfig.class, id);
    }

    /**
     * 列举看板的条件DO
     * @param id
     * @return
     */
    public List<AnalyseBoardCondition> listConditions(String id) {
        return this.generalDao.getByHQL("from AnalyseBoardCondition where analyseBoardConfig.id = ?", id);
    }

}
