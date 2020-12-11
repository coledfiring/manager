package com.whaty.products.service.flow.strategy.check;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import com.whaty.products.service.flow.domain.tab.CardInfoTab;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 费用结算流程策略
 *
 * @author weipengsen
 */
@Lazy
@Component("settleAccountCheckFlowStrategy")
public class SettleAccountCheckFlowStrategy extends AbstractCheckFlowStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Boolean checkCanApply(String itemId) {
        return this.generalDao.checkNotEmpty("SELECT 1 FROM pe_class c " +
                "INNER JOIN enum_const st ON st.id = c.flag_settle_account_status " +
                "WHERE st. CODE IN ('0', '3', '4') AND c.id = ?", itemId);
    }

    @Override
    public void applyCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagSettleAccountStatus' AND st. CODE = '1' " +
                "SET c.flag_settle_account_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public void passCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagSettleAccountStatus' AND st. CODE = '2'" +
                " SET c.flag_settle_account_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public void cancelCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagSettleAccountStatus' AND st. CODE = '0'" +
                " SET c.flag_settle_account_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public void noPassCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagSettleAccountStatus' AND st. CODE = '3'" +
                " SET c.flag_settle_account_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public String getItemName(String itemId) {
        return this.generalDao.getOneBySQL("select name from pe_class where id = ?", itemId);
    }

    @Override
    public List<AbstractBaseTab> getBusiness(String itemId) {
        Map<String, Object> baseInfo = this.generalDao.getOneMapBySQL("SELECT CODE AS `培训班编号`, NAME AS `培训班名称`, " +
                "training_total_fee AS `培训费总额`, teacher_fee AS `师资费`, transport_fee AS `交通费`, " +
                "teaching_fee AS `现场教学费`, food_fee AS `餐饮费`, room_fee AS `住宿费`, enroll_fee AS `报名费`, " +
                "material_fee AS `资料费`, other_fee AS `其他费用`, site_fee AS `场地费`, period_fee AS `劳务带班费` " +
                "FROM pe_class WHERE id = ?", itemId);
        String[] baseHeader = {"培训班编号", "培训班名称", "培训费总额", "师资费", "现场教学费", "交通费", "餐饮费",
                "住宿费", "报名费", "资料费", "劳务带班费", "场地费", "其他费用"};
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                          ");
        sql.append(" 	c.code as `培训班编号`,                                        ");
        sql.append(" 	c.name as `培训班名称`,                                        ");
        sql.append(" 	tt.name as `培训类型`,                                         ");
        sql.append(" 	tta.name as `培训对象`,                                        ");
        sql.append(" 	tm.name as `培训形式`,                                         ");
        sql.append(" 	c.training_person_number as `培训人数`,                        ");
        sql.append(" 	m.true_name as `班主任`,                                       ");
        sql.append(" 	date_format(c.start_time, '%Y-%m-%d') as `开班日期`,           ");
        sql.append(" 	date_format(c.end_time, '%Y-%m-%d') as `结束日期`,             ");
        sql.append(" 	c.training_total_fee as `培训费总额`                           ");
        sql.append(" FROM                                                            ");
        sql.append(" 	pe_class c                                                   ");
        sql.append(" INNER JOIN training_item ti ON ti.id = c.fk_training_item_id    ");
        sql.append(" INNER JOIN enum_const tt ON tt.id = ti.flag_training_type       ");
        sql.append(" INNER JOIN enum_const tta ON tta.id = ti.flag_training_target   ");
        sql.append(" INNER JOIN enum_const tm ON tm.id = c.flag_training_mode        ");
        sql.append(" LEFT JOIN pe_manager m ON m.id = c.fk_class_master_id           ");
        sql.append(" WHERE                                                           ");
        sql.append(" 	c.id = ?                                                     ");
        Map<String, Object> classInfo = this.generalDao.getOneMapBySQL(sql.toString(), itemId);
        String[] classHeader = {"培训班编号", "培训班名称", "培训类型", "培训对象", "培训形式", "培训人数", "班主任",
                "开班日期", "结束日期", "培训费总额"};
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                      ");
        sql.append(" 	cou. NAME AS title,                                      ");
        sql.append(" 	ifnull(concat(                                           ");
        sql.append(" 		date_format(                                         ");
        sql.append(" 			cct.training_date,                               ");
        sql.append(" 			'%Y-%m-%d'                                       ");
        sql.append(" 		),                                                   ");
        sql.append(" 		' ', cct.start_time,                                 ");
        sql.append(" 		' - ', cct.end_time                                  ");
        sql.append(" 	), '-') as `courseTime_icon-time-stroke`,                ");
        sql.append(" 	tea.true_name as `teacherName_icon-user-stroke`,         ");
        sql.append(" 	ifnull(p.name, '-') as `place_icon-locat-stroke`,        ");
        sql.append(" 	concat('师资费', ifnull(cct.teacher_fee, '0'), ' / 场地费', ifnull(p.charges, '0')) ");
        sql.append("      as `fee_icon-fee-stroke`                               ");
        sql.append(" FROM                                                        ");
        sql.append(" 	class_course_timetable cct                               ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id");
        sql.append(" INNER JOIN pe_course cou ON cou.id = cc.fk_course_id        ");
        sql.append(" INNER JOIN pe_class c on c.id = cc.fk_class_id              ");
        sql.append(" INNER JOIN pe_teacher tea ON tea.id = cou.fk_teacher_id     ");
        sql.append(" LEFT JOIN pe_place p ON p.id = cct.fk_place_id              ");
        sql.append(" WHERE                                                       ");
        sql.append(" 	cct.training_date is not null                            ");
        sql.append(" AND cc.fk_class_id = ?                                      ");
        List<Map<String, Object>> feeInfo = this.generalDao.getMapBySQL(sql.toString(), itemId);
        return Arrays.asList(new BaseInfoTab("费用信息", baseHeader, baseInfo),
                new BaseInfoTab("班级详情", classHeader, classInfo), new CardInfoTab("课程表", feeInfo));
    }

    @Override
    public String getScopeId(String itemId) {
        return this.generalDao.getOneBySQL("select fk_unit_id from pe_class where id = ?", itemId);
    }
}
