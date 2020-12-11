package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.domain.bean.AnalyseConfigDetail;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 超管的统计基础配置管理
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseBasicConfigManageService")
public class AnalyseBasicConfigManageServiceImpl extends TycjGridServiceAdapter<AnalyseBasicConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public void checkBeforeAdd(AnalyseBasicConfig bean) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from analyse_basic_config where code = ?", bean.getCode())) {
            throw new EntityException("编号已存在");
        }
        bean.setCreateDate(new Date());
    }

    @Override
    public void afterAdd(AnalyseBasicConfig bean) throws EntityException {
        AnalyseConfigDetail detail = new AnalyseConfigDetail();
        detail.setAnalyseBasicConfig(bean);
        this.generalDao.save(detail);
    }

    @Override
    public void checkBeforeUpdate(AnalyseBasicConfig bean) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from analyse_basic_config where code = ? AND id <> ?",
                bean.getCode(), bean.getId())) {
            throw new EntityException("编号已存在");
        }
        String originType = this.openGeneralDao
                .getOneBySQL("select flag_analyse_type from analyse_basic_config where id = ?", bean.getId());
        if (!originType.equals(bean.getEnumConstByFlagAnalyseType().getId())) {
            this.generalDao.executeBySQL("update analyse_config_detail set config = null " +
                            "where fk_basic_config_id = ?", bean.getId());
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                                     ");
        sql.append(" 	1                                                                                       ");
        sql.append(" FROM                                                                                       ");
        sql.append(" 	analyse_block_config abc                                                                ");
        sql.append(" INNER JOIN analyse_basic_config con on abc.analyse_config_code LIKE concat(con.code, ';%') ");
        sql.append(" OR abc.analyse_config_code LIKE concat('%;', con.code)                                     ");
        sql.append(" OR abc.analyse_config_code = con.code                                                      ");
        sql.append(" WHERE                                                                                      ");
        sql.append(CommonUtils.madeSqlIn(idList, "con.id"));
        if (this.generalDao.checkNotEmpty(sql.toString())) {
            throw new EntityException("存在被块引用的统计图形");
        }
        this.generalDao.executeBySQL("delete from analyse_config_detail where " +
                CommonUtils.madeSqlIn(idList, "fk_basic_config_id"));
    }
}
