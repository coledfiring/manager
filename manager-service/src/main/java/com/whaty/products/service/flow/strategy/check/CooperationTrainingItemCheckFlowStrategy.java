package com.whaty.products.service.flow.strategy.check;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 委托培训项目流程策略
 *
 * @author weipengsen
 */
@Lazy
@Component("cooperationTrainingItemCheckFlowStrategy")
public class CooperationTrainingItemCheckFlowStrategy extends AbstractTrainingItemCheckFlowStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    protected BaseInfoTab getBaseInfo(String itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                         ");
        sql.append(" 	ti.code as `项目编号`,                                        ");
        sql.append(" 	ti.name as `项目名称`,                                        ");
        sql.append(" 	entrustedUnit.name as `委托单位`,                              ");
        sql.append(" 	ti.entrust_unit_linkman as `委托单位联系人`,                  ");
        sql.append(" 	tt.name as `培训类型`,                                        ");
        sql.append(" 	tta.name as `培训对象`,                                       ");
        sql.append(" 	date_format(ti.training_start_time, '%Y-%m-%d') as `培训开始时间`, ");
        sql.append(" 	date_format(ti.training_end_time, '%Y-%m-%d') as `培训结束时间`,   ");
        sql.append(" 	ti.training_person_number as `培训人数`,                       ");
        sql.append(" 	ti.total_fee as `培训费总额`                                   ");
        sql.append(" FROM                                                            ");
        sql.append(" 	training_item ti                                             ");
        sql.append(" LEFT JOIN entrusted_unit entrustedUnit on entrustedUnit.id=ti.fk_entrusted_unit_id ");
        sql.append(" LEFT JOIN cooperate_unit cu on cu.id = ti.fk_cooperate_unit_id ");
        sql.append(" INNER JOIN enum_const tt on tt.id = ti.flag_training_type       ");
        sql.append(" INNER JOIN enum_const tta on tta.id = ti.flag_training_target   ");
        sql.append(" WHERE                                                           ");
        sql.append(" 	ti.id = ?                                                    ");
        String[] serialHeader = {"项目编号", "项目名称", "委托单位", "委托单位联系人", "培训类型", "培训对象", "培训开始时间",
                "培训结束时间", "培训人数", "培训费总额"};
        return new BaseInfoTab("基础信息", serialHeader, this.generalDao.getOneMapBySQL(sql.toString(), itemId));
    }

    @Override
    protected GeneralDao getGeneralDao() {
        return this.generalDao;
    }

}
