package com.whaty.products.service.pay.command;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 类注释
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("olStudentClassEnrollPayOrderCommand")
public class OlStudentClassEnrollPayOrderCommand extends OlStudentCourseEnrollPayOrderCommand {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Override
    public Map<String, Object> payOrderAgain(String orderNo) {
        Map<String, Object> orderInfo = super.payOrderAgain(orderNo);
        orderInfo.put("attach", "olStudentClassEnroll");
        return orderInfo;
    }

    @Override
    public void finishOrder(String orderNo, String payWay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update ol_pe_order po                                             ");
        sql.append(" inner join enum_const pt on pt.namespace = 'flagIsPay'            ");
        sql.append(" and pt.code = '1'                                                 ");
        sql.append(" inner join enum_const pw on pw.namespace = 'flagOnlinePayWay'     ");
        sql.append(" and pw.code = ?                                                   ");
        sql.append(" set po.flag_is_pay=pt.id,                                         ");
        sql.append(" flag_online_pay_way=pw.id,                                        ");
        sql.append(" pay_date = now()                                                  ");
        sql.append(" where trade_no = '" + orderNo + "'                                ");
        this.myGeneralDao.executeBySQL(sql.toString(), payWay);
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                                   ");
        sql.append("   stu.id as stuId,                                                                       ");
        sql.append("   cl.id as classId,                                                                      ");
        sql.append("   stu.fk_sso_user_id as userId,                                                          ");
        sql.append("   concat(stu.login_id,'-',COUNT(ocs.id)) as loginId,                                     ");
        sql.append("   stu.true_name as name                                                                  ");
        sql.append(" FROM                                                                                     ");
        sql.append(" 	ol_pe_order po                                                                        ");
        sql.append(" INNER JOIN ol_pe_student stu on stu.fk_sso_user_id=po.fk_student_id                      ");
        sql.append(" INNER JOIN ol_pe_class cl on cl.id=po.item_id                                            ");
        sql.append(" left join ol_class_student ocs on ocs.fk_student_id = stu.id                             ");
        sql.append(" where                                                                                    ");
        sql.append("   po.trade_no = ?                                                                        ");
        sql.append(" GROUP BY stu.id                                                                          ");
        Map<String, Object> stuInfoMap = this.myGeneralDao.getOneMapBySQL(sql.toString(), orderNo);
        if (MapUtils.isEmpty(stuInfoMap)) {
            return;
        }
        sql.delete(0, sql.length());
        String stuId = CommonUtils.generateUUIDNoSign();
        sql.append(" INSERT INTO ol_class_student (                       ");
        sql.append("   id,                                                ");
        sql.append("   fk_student_id,                                     ");
        sql.append("   fk_class_id,                                       ");
        sql.append("   create_time,                                       ");
        sql.append("   create_by,                                         ");
        sql.append("   login_id                                           ");
        sql.append(" )                                                    ");
        sql.append(" VALUES                                               ");
        sql.append("   (                                                  ");
        sql.append("     ?,                                               ");
        sql.append("     ?,                                               ");
        sql.append("     ?,                                               ");
        sql.append("     now(),                                           ");
        sql.append("     ?,                                               ");
        sql.append("     ?                                                ");
        sql.append("   )                                                  ");
        this.myGeneralDao.executeBySQL(sql.toString(), stuId, stuInfoMap.get("stuId"), stuInfoMap.get("classId"),
                stuInfoMap.get("userId"), stuInfoMap.get("loginId"));
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.saveSudent(stuId,
                        (String) stuInfoMap.get("loginId"), (String) stuInfoMap.get("name"), stuId, null,
                        LearnSpaceUtil.getLearnSpaceSiteCode(), (String) stuInfoMap.get("loginId"))) {
                    throw new ServiceException("抱歉，学生信息同步课程空间失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("学生信息同步课程空间失败!");
            }
        }
    }
}
