package com.whaty.products.service.enroll.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.common.UtilService;
import com.whaty.products.service.enroll.constant.StudentEnrollConstant;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 报名信息管理
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("studentEnrollInfoManageService")
public class StudentEnrollInfoManageServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    /**
     * 学生交费
     *
     * @param ids
     */
    @LogAndNotice("报名信息管理：交费")
    public void doPayFee(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        sql.append(" where                                                       ");
        sql.append("   code = '1'                                                ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作未交费的学生");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_student stu " +
                "inner join enum_const st on st.id = stu.flag_enroll_check_status " +
                "WHERE ifnull(st.code, '0') in ('1', '3') AND " + CommonUtils.madeSqlIn(ids, "stu.id"))) {
            throw new ServiceException("只能为审核通过或无需审核的学生缴费");
        }
        sql.delete(0, sql.length());
        sql.append(" update                                                      ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.namespace='FlagFeeStatus'    ");
        sql.append(" and ft.code='1'                                             ");
        sql.append(" INNER JOIN enum_const pay ON pay.namespace = 'flagIsPay'    ");
        sql.append(" and pay.code='1'                                            ");
        sql.append(" inner join enum_const pw on pw.namespace = 'FlagPayWay'     ");
        sql.append(" and pw.code = '0'                                           ");
        sql.append(" LEFT JOIN pe_order po ON po.fk_student_id = stu.id          ");
        sql.append(" AND po.item_id = stu.fk_training_item_id                    ");
        sql.append(" set stu.flag_fee_status=ft.id,                              ");
        sql.append(" stu.flag_pay_way = pw.id,                                   ");
        sql.append(" stu.fee_operate_user = ?,                                   ");
        sql.append(" stu.fee_operate_time = now(),                               ");
        sql.append(" po.flag_is_pay = pay.id,                                    ");
        sql.append(" po.remainder_num = 3                                        ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString(), UserUtils.getCurrentUserId());
    }

    /**
     * 取消交费
     *
     * @param ids
     */
    @LogAndNotice("报名信息管理：取消交费")
    public void doCancelPay(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        sql.append(" where                                                       ");
        sql.append("   code = '0'                                                ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作已交费的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append(" and stu.fk_class_id is not null                             ");
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作未分班的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" update                                                      ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.namespace='FlagFeeStatus'    ");
        sql.append(" and ft.code='0'                                             ");
        sql.append(" INNER JOIN enum_const pay ON pay.namespace = 'flagIsPay'    ");
        sql.append(" and pay.code='0'                                            ");
        sql.append(" LEFT JOIN pe_order po ON po.fk_student_id = stu.id          ");
        sql.append(" AND po.item_id = stu.fk_training_item_id                    ");
        sql.append(" set stu.flag_fee_status=ft.id,                              ");
        sql.append(" stu.fee_operate_user = ?,                                   ");
        sql.append(" stu.fee_operate_time = now(),                               ");
        sql.append(" po.flag_is_pay = pay.id                                     ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString(), UserUtils.getCurrentUserId());
    }

    /**
     * 退学
     *
     * @param ids
     */
    @LogAndNotice("报名信息管理：退学")
    public void doQuitSchool(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append(" and stu.fk_class_id is null                                 ");
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作已分班的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" update pe_student stu                                           ");
        sql.append(" inner join enum_const st on st.namespace='FlagStudentStatus'    ");
        sql.append(" and st.code='2'                                                 ");
        sql.append(" set stu.fee_operate_user=?,                                     ");
        sql.append(" stu.flag_student_status = st.id,                                ");
        sql.append(" stu.quit_school_date = now(),                                   ");
        sql.append(" stu.fee_operate_time = now()                                    ");
        sql.append(" where                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString(), UserUtils.getCurrentUserId());
    }

    /**
     * 获取勾选学生的可分配班级
     *
     * @param itemId
     */
    public List<Object> getSelectedArrangeClazz(String itemId) {
        TycjParameterAssert.isAllNotBlank(itemId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   cl.id as id,                                              ");
        sql.append("   cl.name as name                                           ");
        sql.append(" from                                                        ");
        sql.append("   training_item item                                        ");
        sql.append(" inner join pe_class cl on cl.fk_training_item_id=item.id    ");
        sql.append(" where                                                       ");
        sql.append("   item.id = ?                                               ");
        return this.myGeneralDao.getBySQL(sql.toString(), itemId);
    }

    /**
     * 学生分班
     *
     * @param ids
     * @param clazzId
     */
    @LogAndNotice("报名信息管理：学生分班")
    public void doArrangeStudentClass(String ids, String clazzId) {
        TycjParameterAssert.isAllNotBlank(ids, clazzId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        sql.append(" where                                                       ");
        sql.append("   code = '0'                                                ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作已交费的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" select                                                      ");
        sql.append("   stu.fk_training_item_id as id                             ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append(" group by stu.fk_training_item_id                            ");
        List checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(checkList) && checkList.size() > 1) {
            throw new ServiceException("只能操作报名相同培训项目的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append(" and stu.fk_class_id is not null                             ");
        checkList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new ServiceException("只能操作未分班的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" update pe_student stu                                           ");
        sql.append(" inner join enum_const st on st.namespace='FlagStudentStatus'    ");
        sql.append(" and st.code='1'                                                 ");
        sql.append(" set stu.fk_class_id='" + clazzId + "',                          ");
        sql.append(" stu.flag_student_status = st.id                                 ");
        sql.append(" where                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString());

        String code = this.myGeneralDao
                .getOneBySQL("select code from pe_class where id = ?", clazzId);
        sql.delete(0, sql.length());
        sql.append("select max(login_id) from sso_user where length(login_id) = ? and login_id like '" + code + "%'");
        String maxCode = this.myGeneralDao.getOneBySQL(sql.toString(), code.length() + 4);
        int startCode = StringUtils.isNotBlank(maxCode) ? Integer.parseInt(maxCode.replace(code, "")) : 1;
        List<String> loginIdList = new ArrayList<>();
        for (int i = 0; i < ids.split(CommonConstant.SPLIT_ID_SIGN).length; i++) {
            startCode++;
            loginIdList.add(code + CommonUtils.leftAddZero(startCode, 4));
        }

        List<String> idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));

        sql.delete(0, sql.length());
        sql.append(" INSERT INTO sso_user (                                 ");
        sql.append(" 	ID,                                                 ");
        sql.append(" 	LOGIN_ID,                                           ");
        sql.append(" 	PASSWORD,                                           ");
        sql.append(" 	FK_ROLE_ID,                                         ");
        sql.append(" 	FLAG_ISVALID,                                       ");
        sql.append(" 	LEARNSPACE_SITE_CODE,                               ");
        sql.append(" 	SITE_CODE                                           ");
        sql.append(" ) values                                               ");
        String roleId = this.myGeneralDao.getById(PePriRole.class, "0").getId();
        String validId = this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, "1").getId();
        String siteCode = SiteUtil.getSiteCode();
        String learnSpaceSiteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
        for (int i = 0; i < idList.size(); i++) {
            sql.append(" (                                               ");
            sql.append("    '" + idList.get(i) + "',                     ");
            sql.append("    '" + loginIdList.get(i) + "',                ");
            sql.append("    md5('" + loginIdList.get(i) + "'),           ");
            sql.append("    '" + roleId + "',                            ");
            sql.append("    '" + validId + "',                           ");
            sql.append("    '" + learnSpaceSiteCode + "',                ");
            sql.append("    '" + siteCode + "'                           ");
            sql.append(" ),                                              ");
        }
        sql.delete(sql.lastIndexOf(CommonConstant.SPLIT_ID_SIGN), sql.length());
        this.myGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" update pe_student stu                                           ");
        sql.append(" inner join sso_user sso on sso.id=stu.id                        ");
        sql.append(" set stu.fk_sso_user_id=sso.id                                   ");
        sql.append(" where                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 取消学生分班
     *
     * @param ids
     */
    @LogAndNotice("报名信息管理：取消学生分班")
    public void doCancelArrangeStudentClass(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append(" and stu.fk_class_id is null                                 ");
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作已分班的学生");
        }
        sql.delete(0, sql.length());
        sql.append(" update pe_student stu                                           ");
        sql.append(" inner join enum_const st on st.namespace='FlagStudentStatus'    ");
        sql.append(" and st.code = '0'                                               ");
        sql.append(" set stu.fk_class_id = null,                                     ");
        sql.append(" stu.fk_sso_user_id = null,                                      ");
        sql.append(" stu.flag_student_status = st.id                                 ");
        sql.append(" where                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
        this.myGeneralDao.executeBySQL("delete sso from pe_student stu " +
                "inner join sso_user sso on sso.id = stu.fk_sso_user_id where" + CommonUtils.madeSqlIn(ids, "stu.id"));
    }

    /**
     * 导入票据号模版
     *
     * @param out
     */
    public void downloadInvoiceNoTemplate(OutputStream out) {
        this.utilService.generateExcelAndWrite(out, null, StudentEnrollConstant.UPLOAD_EXCEL_INVOICE_NO);
    }

    /**
     * 导入票据号
     *
     * @param file
     * @return
     */
    @LogAndNotice("导入票据号")
    public Map<String, Object> doUploadStudentInvoiceNo(File file) {
        List<String[]> checkList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" EXISTS (                                                                      ");
        sql.append(" 	SELECT                                                                     ");
        sql.append(" 		1                                                                      ");
        sql.append(" 	FROM                                                                       ");
        sql.append(" 		(                                                                      ");
        sql.append(" 			SELECT                                                             ");
        sql.append(" 				t.id,                                                          ");
        sql.append(" 				t.column5                                                      ");
        sql.append(" 			FROM                                                               ");
        sql.append(" 				util_excel_table t                                             ");
        sql.append(" 			WHERE                                                              ");
        sql.append(" 				t.namespace = ${namespace}       					           ");
        sql.append(" 		) a                                                                    ");
        sql.append(" 	WHERE                                                                      ");
        sql.append("        a.column5 = e.COLUMN5                                                  ");
        sql.append(" 	AND a.id <> e.id                                                           ");
        sql.append(" )                                                                             ");
        checkList.add(new String[]{"文件中存在相同的票据号;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" EXISTS (                                                                      ");
        sql.append(" 	SELECT                                                                     ");
        sql.append(" 		1                                                                      ");
        sql.append(" 	FROM                                                                       ");
        sql.append(" 		(                                                                      ");
        sql.append(" 			SELECT                                                             ");
        sql.append(" 				t.id,                                                          ");
        sql.append(" 				t.column1,                                                     ");
        sql.append(" 				t.column2,                                                     ");
        sql.append(" 				t.column3,                                                     ");
        sql.append(" 				t.column4                                                      ");
        sql.append(" 			FROM                                                               ");
        sql.append(" 				util_excel_table t                                             ");
        sql.append(" 			WHERE                                                              ");
        sql.append(" 				t.namespace = ${namespace}       					           ");
        sql.append(" 		) a                                                                    ");
        sql.append(" 	WHERE                                                                      ");
        sql.append("        a.column1 = e.COLUMN1                                                  ");
        sql.append("    and a.column2 = e.COLUMN2                                                  ");
        sql.append("    and a.column3 = e.COLUMN3                                                  ");
        sql.append("    and a.column4 = e.COLUMN4                                                  ");
        sql.append(" 	AND a.id <> e.id                                                           ");
        sql.append(" )                                                                             ");
        checkList.add(new String[]{"文件中存在相同的学生;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" not exists (                                                                  ");
        sql.append(" 	SELECT                                                                     ");
        sql.append(" 		1                                                                      ");
        sql.append(" 	FROM                                                                       ");
        sql.append(" 	  pe_student stu	                                                       ");
        sql.append(" 	inner join training_item item on stu.fk_training_item_id=item.id           ");
        sql.append(" 	WHERE                                                                      ");
        sql.append("        stu.true_name = e.COLUMN1                                              ");
        sql.append("    and stu.mobile = e.COLUMN2                                                 ");
        sql.append("    and item.name = e.COLUMN3                                                  ");
        sql.append("    and item.code = e.COLUMN4                                                  ");
        sql.append(" )                                                                             ");
        checkList.add(new String[]{"学生项目的报名信息不存在;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" EXISTS (                                                               ");
        sql.append(" SELECT                                                                 ");
        sql.append(" 	1                                                                   ");
        sql.append(" FROM                                                                   ");
        sql.append(" 	pe_student stu                                                      ");
        sql.append(" INNER JOIN enum_const ft ON ft.id = stu.flag_fee_status                ");
        sql.append(" INNER JOIN training_item item ON stu.fk_training_item_id = item.id     ");
        sql.append(" WHERE                                                                  ");
        sql.append(" 	stu.mobile = e.COLUMN2                                              ");
        sql.append(" AND stu.true_name = e.COLUMN1                                          ");
        sql.append(" AND ft.`CODE` = '0'                                                    ");
        sql.append(" AND item.name = e.COLUMN3                                              ");
        sql.append(" AND item.code = e.COLUMN4                                              ");
        sql.append(" )                                                                      ");
        checkList.add(new String[]{"文件中存在未缴费的学生;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" UPDATE pe_student stu                                                  ");
        sql.append(" inner join training_item item on stu.fk_training_item_id=item.id       ");
        sql.append(" INNER JOIN util_excel_table e ON stu.true_name = e.COLUMN1             ");
        sql.append(" and stu.mobile = e.COLUMN2                                             ");
        sql.append(" and item.name = e.COLUMN3                                              ");
        sql.append(" and item.code = e.COLUMN4                                              ");
        sql.append(" SET stu.invoice_no = e.COLUMN5                                         ");
        sql.append(" where e.namespace = ${namespace}                                       ");
        sql.append(" AND e.valid = '1'                                                      ");
        return this.utilService.doUploadFile(file, StudentEnrollConstant.UPLOAD_EXCEL_INVOICE_NO,
                checkList, Arrays.asList(sql.toString()), 0, "导入票据号");
    }

    @LogAndNotice("汇款单审核")
    public int doCheckMoneyOrder(String ids, String checkId, String checkReason) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("至少勾选一个学生");
        }
        if (StringUtils.isBlank(checkId)) {
            throw new ServiceException("请选择是否审核通过");
        }
        StringBuilder checkSql = new StringBuilder();
        checkSql.append(" select                                                      ");
        checkSql.append("   1                                                         ");
        checkSql.append(" from                                                        ");
        checkSql.append("   pe_student stu                                            ");
        checkSql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        checkSql.append(" where                                                       ");
        checkSql.append("   code = '1'                                                ");
        checkSql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(checkSql.toString()))) {
            throw new ServiceException("只能操作未交费的学生");
        }
        EnumConst check = this.myGeneralDao.getEnumConstByNamespaceCode("flagCheckStatus", "2");
        EnumConst isPay;
        EnumConst feeStatus;
        int remainderNum = 0;
        if (check.getId().equals(checkId)) {
            isPay = this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsPay", "1");
            feeStatus = this.myGeneralDao.getEnumConstByNamespaceCode("FlagFeeStatus", "1");
            remainderNum = 3;
        } else {
            isPay = this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsPay", "0");
            feeStatus = this.myGeneralDao.getEnumConstByNamespaceCode("FlagFeeStatus", "0");
        }
        String sql = "update pe_order set flag_check_status = '" + checkId + "' , check_reason = '" + checkReason + "' " +
                ", flag_is_pay = '" + isPay.getId() + "' , remainder_num = " + remainderNum + " where " +
                CommonUtils.madeSqlIn(ids, "fk_student_id");
        int count = this.myGeneralDao.executeBySQL(sql);
        //生成学号
        String classId = this.myGeneralDao.getBySQL("select fk_class_id from pe_student where"
                + CommonUtils.madeSqlIn(ids, "id")).get(0).toString();
        Map<String, Object> map = this.myGeneralDao.getMapBySQL("SELECT pc.code code,MAX(RIGHT(ps.reg_no, 3)) num " +
                "FROM pe_student ps INNER JOIN pe_class pc ON ps.fk_class_id = pc.id " +
                "WHERE pc.id = '" + classId + "'").get(0);
        String code = (String) map.get("code");
        Object num = map.get("num");
        String regNo = num == null ? "001" : ((Integer.parseInt((String) num) + 1) + "");
        this.myGeneralDao.executeBySQL("update pe_student set reg_no = concat('" + code + "', lpad('" + regNo + "',3,0)), " +
                "flag_fee_status = '" + feeStatus.getId() + "' where " + CommonUtils.madeSqlIn(ids, "id"));
        return count;
    }

    @LogAndNotice("调整学生期次")
    public int doChangeClass(String ids, String checkId) {
        String oldItemId = (String) this.myGeneralDao.getBySQL("select pc.fk_training_item_id from pe_student ps inner join" +
                " pe_class pc on pc.id = ps.fk_class_id where" + CommonUtils.madeSqlIn(ids, "ps.id")).get(0);
        String newItemId = (String) this.myGeneralDao.getBySQL("select fk_training_item_id from pe_class " +
                " where id = '" + checkId + "'").get(0);
        if (!newItemId.equals(oldItemId)) {
            throw new ServiceException("调整后期次与原期次项目不一致");
        }
        int count = this.myGeneralDao.executeBySQL("update pe_student set fk_class_id = '" + checkId + "' where " +
                CommonUtils.madeSqlIn(ids, "id"));
        return count;
    }

    /**
     * 学生交费
     *
     * @param ids
     */
    @LogAndNotice("报名信息管理：线下交费")
    public void doUnderlinePayFee(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        sql.append(" where                                                       ");
        sql.append("   code = '1'                                                ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作未交费的学生");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_student stu " +
                "inner join enum_const st on st.id = stu.flag_enroll_check_status " +
                "WHERE ifnull(st.code, '0') in ('1', '3') AND " + CommonUtils.madeSqlIn(ids, "stu.id"))) {
            throw new ServiceException("只能为审核通过或无需审核的学生缴费");
        }
        //生成学号
        String classId = this.myGeneralDao.getBySQL("select fk_class_id from pe_student where"
                + CommonUtils.madeSqlIn(ids, "id")).get(0).toString();
        Map<String, Object> map = this.myGeneralDao.getMapBySQL("SELECT pc.code code,MAX(RIGHT(ps.reg_no, 3)) num " +
                "FROM pe_student ps INNER JOIN pe_class pc ON ps.fk_class_id = pc.id " +
                "WHERE pc.id = '" + classId + "'").get(0);
        String code = (String) map.get("code");
        Object num = map.get("num");
        String regNo = num == null ? "001" : ((Integer.parseInt((String) num) + 1) + "");
        sql.delete(0, sql.length());
        sql.append(" update                                                      ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.namespace='FlagFeeStatus'    ");
        sql.append(" and ft.code='1'                                             ");
        sql.append(" INNER JOIN enum_const pay ON pay.namespace = 'flagIsPay'    ");
        sql.append(" and pay.code='1'                                            ");
        sql.append(" LEFT JOIN pe_order po ON po.fk_student_id = stu.id          ");
        sql.append(" AND po.item_id = stu.fk_training_item_id                    ");
        sql.append(" set stu.flag_fee_status=ft.id,                              ");
        sql.append(" po.flag_is_pay = pay.id,                                    ");
        sql.append(" po.remainder_num = 3,                                       ");
        sql.append(" stu.reg_no = concat('" + code + "', lpad('" + regNo + "',3,0)) ");
        sql.append(" where                                                       ");
        sql.append(CommonUtils.madeSqlIn(ids, "stu.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 设置报名费
     *
     * @param ids
     * @param enrollFee
     */
    @LogAndNotice("报名信息管理：设置报名费")
    public int doSetEnrollFee(String ids, String enrollFee) {
        TycjParameterAssert.isAllNotBlank(ids, enrollFee);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   1                                                         ");
        sql.append(" from                                                        ");
        sql.append("   pe_student stu                                            ");
        sql.append(" inner join enum_const ft on ft.id=stu.flag_fee_status       ");
        sql.append(" where                                                       ");
        sql.append("   code = '1'                                                ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isNotEmpty(this.myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作未交费的学生");
        }
        return this.myGeneralDao.executeBySQL("update pe_student set enroll_fee = ? where "
                + CommonUtils.madeSqlIn(ids, "id"), enrollFee);
    }
}
