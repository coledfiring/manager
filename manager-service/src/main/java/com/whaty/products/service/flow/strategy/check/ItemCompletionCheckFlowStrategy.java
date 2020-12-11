package com.whaty.products.service.flow.strategy.check;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import com.whaty.products.service.flow.domain.tab.CardInfoTab;
import com.whaty.products.service.flow.domain.tab.ListInfoTab;
import com.whaty.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目结业审核策略
 *
 * @author weipengsen
 */
@Lazy
@Component("itemCompletionCheckFlowStrategy")
public class ItemCompletionCheckFlowStrategy extends AbstractCheckFlowStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    private final static int SERIAL_LIMIT = 5;

    @Override
    public Boolean checkCanApply(String itemId) {
        return this.generalDao.checkNotEmpty("SELECT 1 FROM pe_class c " +
                "INNER JOIN enum_const st ON st.id = c.flag_apply_certificate_status " +
                "WHERE st. CODE IN ('0', '3') AND c.id = ?", itemId);
    }

    @Override
    public void applyCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagApplyCertificateStatus' AND st. CODE = '1' " +
                "SET c.flag_apply_certificate_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public void passCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagApplyCertificateStatus' AND st. CODE = '2'" +
                " SET c.flag_apply_certificate_status = st.id WHERE c.id = ?", itemId);
        // 生成证书编号
        String code = this.generalDao.getOneBySQL("select un.code from pe_class c " +
                "inner join pe_unit un on un.id = c.fk_unit_id where c.id = ?", itemId);
        String prefix = CommonUtils.changeDateToString(new Date(), "yyyy") + code;
        String maxCode = this.generalDao
                .getOneBySQL("select max(certificate_number) from pe_student " +
                                "where substr(certificate_number, 1, ?) = ? and length(certificate_number) = ?",
                        prefix.length(), prefix, prefix.length() + SERIAL_LIMIT);
        int maxSerial = 0;
        if (StringUtils.isNotBlank(maxCode)) {
            maxSerial = Integer.parseInt(maxCode.replace(prefix, ""));
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE pe_student stu                                         ");
        sql.append(" INNER JOIN (                                                  ");
        sql.append(" 	SELECT                                                     ");
        sql.append(" 		@row_number :=@row_number + 1 AS number,               ");
        sql.append(" 		stu.id AS id                                           ");
        sql.append(" 	FROM                                                       ");
        sql.append(" 		(                                                      ");
        sql.append(" 			SELECT                                             ");
        sql.append(" 				id                                             ");
        sql.append(" 			FROM                                               ");
        sql.append(" 				pe_student                                     ");
        sql.append("            WHERE                                              ");
        sql.append("                fk_class_id = ?                                ");
        sql.append(" 			ORDER BY                                           ");
        sql.append(" 				IFNULL(order_no,99999),                        ");
        sql.append(" 				CONVERT (true_name USING 'gbk')                ");
        sql.append(" 		) stu                                                  ");
        sql.append(" 	INNER JOIN (SELECT @row_number := 0) r                     ");
        sql.append(" ) num ON num.id = stu.id                                      ");
        sql.append(" SET certificate_number = concat(?, lpad(num.number + ?, 5, '0'))");
        sql.append(" WHERE                                                         ");
        sql.append(" 	stu.fk_class_id = ?                                        ");
        this.generalDao.executeBySQL(SQLHandleUtils.handleSignInSQL(sql.toString()), itemId, prefix, maxSerial, itemId);
    }

    @Override
    public void noPassCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagApplyCertificateStatus' AND st. CODE = '3'" +
                " SET c.flag_apply_certificate_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public void cancelCheck(String itemId) {
        this.generalDao.executeBySQL("UPDATE pe_class c INNER JOIN enum_const st " +
                "ON st.namespace = 'flagApplyCertificateStatus' AND st. CODE = '0'" +
                " SET c.flag_apply_certificate_status = st.id WHERE c.id = ?", itemId);
    }

    @Override
    public String getItemName(String itemId) {
        return this.generalDao.getOneBySQL("select name from pe_class where id = ?", itemId);
    }

    @Override
    public List<AbstractBaseTab> getBusiness(String itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append(" 	c.code as `开班编号`,                                       ");
        sql.append(" 	c.name as `培训班名称`,                                     ");
        sql.append(" 	ti.name as `所属项目`,                                      ");
        sql.append(" 	u.name as `培训单位`,                                       ");
        sql.append(" 	tt.name as `培训种类`,                                      ");
        sql.append(" 	hc.name as `是否有证书`,                                    ");
        sql.append(" 	date_format(c.start_time, '%Y-%m-%d') as `开班日期`,        ");
        sql.append(" 	date_format(c.end_time, '%Y-%m-%d') as `结束日期`,          ");
        sql.append(" 	c.training_day_number as `培训天数`,                        ");
        sql.append(" 	c.period as `学时`,                                         ");
        sql.append(" 	c.credit as `学分`,                                         ");
        sql.append(" 	m.true_name as `班主任`,                                    ");
        sql.append(" 	m.mobile as `班主任电话`                                    ");
        sql.append(" FROM                                                           ");
        sql.append(" 	pe_class c                                                  ");
        sql.append(" INNER JOIN training_item ti ON ti.id = c.fk_training_item_id   ");
        sql.append(" INNER JOIN pe_unit u ON u.id = c.fk_unit_id                    ");
        sql.append(" INNER JOIN enum_const tt ON tt.id = ti.flag_training_item_type ");
        sql.append(" INNER JOIN enum_const hc ON hc.id = c.flag_has_certificate     ");
        sql.append(" LEFT JOIN pe_manager m ON m.id = c.fk_class_master_id          ");
        sql.append(" WHERE                                                          ");
        sql.append(" 	c.id = ?                                                    ");
        Map<String, Object> baseInfo = this.generalDao.getOneMapBySQL(sql.toString(), itemId);
        String[] baseHeader = {"开班编号", "培训班名称", "所属项目", "培训单位", "培训种类", "是否有证书", "开班日期", "结束日期",
                "培训天数", "学时", "学分", "班主任", "班主任电话"};
        List<Map<String, Object>> studentList = this.generalDao
                .getMapBySQL("select stu.true_name as `姓名`, gen.name as `性别`, stu.mobile as `手机号` " +
                        "from pe_Student stu inner join enum_const gen on gen.id = stu.flag_gender " +
                        "where stu.fk_class_id = ?", itemId);
        for (int i = 0; i < studentList.size(); i++) {
            studentList.get(i).put("序号", i + 1);
        }
        String[] studentHeader = {"序号", "姓名", "性别", "手机号"};
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                   ");
        sql.append(" 	cou.name as title,                                    ");
        sql.append(" 	tea.true_name as `teacher_icon-user-stroke`,          ");
        sql.append(" 	ct.name as `courseType_icon-type-stroke`              ");
        sql.append(" FROM                                                     ");
        sql.append(" 	class_course cc                              ");
        sql.append(" INNER JOIN pe_course cou on cou.id = cc.fk_course_id    ");
        sql.append(" INNER JOIN pe_teacher tea on tea.id = cou.fk_teacher_id  ");
        sql.append(" INNER JOIN enum_const ct on ct.id = cou.flag_course_type ");
        sql.append(" WHERE                                                    ");
        sql.append(" 	cc.fk_class_id = ?                           ");
        List<Map<String, Object>> courseInfo = this.generalDao.getMapBySQL(sql.toString(), itemId);
        return Arrays.asList(new BaseInfoTab("班级信息", baseHeader, baseInfo),
                new ListInfoTab("学员列表", studentHeader, studentList), new CardInfoTab("课程列表", courseInfo));
    }

    @Override
    public String getScopeId(String itemId) {
        return this.generalDao.getOneBySQL("select fk_unit_id from pe_class where id = ?", itemId);
    }
}
