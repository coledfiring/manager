package com.whaty.products.service.pay.command;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeOrder;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 在线培训报名订单支付命令对象
 *l
 * @author suoqiangqiang
 */
@Lazy
@Component("olStudentCourseEnrollPayOrderCommand")
public class OlStudentCourseEnrollPayOrderCommand implements AbstractPayOrderCommand {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Override
    public Map<String, Object> payOrderAgain(String orderNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                                     ");
        sql.append("   efo.order_number as orderNo,                                                             ");
        sql.append("   efo.fee_amount as totalPrice,                                                            ");
        sql.append("   efo.input_date as orderDate,                                                             ");
        sql.append("   FROM_UNIXTIME(efo.sign,'%Y-%m-%d %T') as orderDateAli,                                   ");
        sql.append("   FROM_UNIXTIME(efo.sign,'%Y%m%d%H%i%s') as orderDateWeChat,                               ");
        sql.append("   FROM_UNIXTIME(unix_timestamp(now())+1800,'%Y-%m-%d %T') as orderNewDateAli,              ");
        sql.append("   FROM_UNIXTIME(unix_timestamp(now())+1800,'%Y%m%d%H%i%s') as orderNewDateWeChat,          ");
        sql.append("   type.name as type,                                                                       ");
        sql.append("   'http://" + SiteUtil.getSite().getDomain() + "/ws/olPcWeb/open/oltrain/order/index'      ");
        sql.append("   as returnUrl,                                                                            ");
        sql.append("   'olStudentCourseEnroll' as attach,                                                       ");
        sql.append("   efo.name as description                                                                  ");
        sql.append(" from                                                                                       ");
        sql.append("   ol_pe_order efo                                                                          ");
        sql.append(" inner join enum_const pay on pay.id = efo.flag_is_pay                                      ");
        sql.append(" inner join enum_const type on type.id = efo.flag_order_type                                ");
        sql.append(" where                                                                                      ");
        sql.append("   efo.order_number = '" + orderNo + "'                                                     ");
        sql.append(" and  pay.code = '0'                                                                        ");
        Map<String, Object> orderInfo = this.myGeneralDao.getOneMapBySQL(sql.toString());
        if (MapUtils.isEmpty(orderInfo)) {
            throw new ServiceException("订单已完成或正在支付中，请刷新后重试");
        }
        Date date = CommonUtils.changeStringToDateTime((String) orderInfo.get("orderDateAli"));
        orderInfo.put("timeoutTime", orderInfo.get("orderDateWeChat"));
        orderInfo.put("timeExpire", orderInfo.get("orderDateAli"));
        if (!CommonUtils.compareDateDetail(new Date(), date)) {
            String refreshOrderNo = new OlPeOrder().generateOrderNo();
            this.myGeneralDao.executeBySQL("update ol_pe_order set sign = unix_timestamp(now())+1800,trade_no = ?" +
                    " where order_number = ?", refreshOrderNo, orderNo);
            orderInfo.put("timeoutTime", orderInfo.get("orderNewDateWeChat"));
            orderInfo.put("timeExpire", orderInfo.get("orderNewDateAli"));
            orderInfo.put("orderNo", refreshOrderNo);
        }
        return orderInfo;
    }

    @Override
    public Map<String, Object> createAndSaveOrder(Map<String, Object> orderParams) {
        throw new ServiceException("功能未完成");
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
        String eleId = CommonUtils.generateUUIDNoSign();
        sql.append(" SELECT                                                                                   ");
        sql.append("   stu.id as stuId,                                                                       ");
        sql.append("   cou.id as courseId,                                                                    ");
        sql.append("   stu.login_id as loginId,                                                               ");
        sql.append("   stu.true_name as name                                                                  ");
        sql.append(" FROM                                                                                     ");
        sql.append(" 	ol_pe_order po                                                                        ");
        sql.append(" INNER JOIN ol_pe_student stu on stu.fk_sso_user_id=po.fk_student_id                      ");
        sql.append(" INNER JOIN ol_pe_course cou on cou.id=po.item_id                                         ");
        sql.append(" left join enum_const isSync on isSync.id = stu.flag_is_synchronize                       ");
        sql.append(" and isSync.code = '1'                                                                    ");
        sql.append(" where                                                                                    ");
        sql.append("   po.trade_no = ?                                                                        ");
        Map<String, Object> stuInfoMap = this.myGeneralDao.getOneMapBySQL(sql.toString(), orderNo);
        if (MapUtils.isEmpty(stuInfoMap)) {
            return;
        }
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO ol_pr_tch_stu_elective (                 ");
        sql.append("   id,                                                ");
        sql.append("   fk_stu_id,                                         ");
        sql.append("   fk_course_id,                                      ");
        sql.append("   elective_date                                      ");
        sql.append(" )                                                    ");
        sql.append(" VALUES                                               ");
        sql.append("   (                                                  ");
        sql.append("     ?,                                               ");
        sql.append("     ?,                                               ");
        sql.append("     ?,                                               ");
        sql.append("     now()                                            ");
        sql.append("   )                                                  ");
        this.myGeneralDao.executeBySQL(sql.toString(), eleId, stuInfoMap.get("stuId"), stuInfoMap.get("courseId"));
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            this.syncStuAndEleToLearnSpace(stuInfoMap, eleId);
        }
    }

    /**
     * 保存学生选课信息
     *
     * @param stuInfoMap
     * @param eleId
     */
    private void syncStuAndEleToLearnSpace(Map<String, Object> stuInfoMap, String eleId) {
        StringBuilder sql = new StringBuilder();
        if ("1".equals(stuInfoMap.get("isSync"))) {
            try {
                if (!learningSpaceWebService.saveElective(eleId, (String) stuInfoMap.get("stuId"),
                        (String) stuInfoMap.get("courseId"), new java.sql.Date(System.currentTimeMillis()).toString(),
                        false, LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new ServiceException("学生选课同步课程空间失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("学生选课同步课程空间失败!");
            }
        } else {
            try {
                if (!learningSpaceWebService.saveSudent((String) stuInfoMap.get("stuId"),
                        (String) stuInfoMap.get("loginId"), (String) stuInfoMap.get("name"),
                        (String) stuInfoMap.get("stuId"), null,
                        LearnSpaceUtil.getLearnSpaceSiteCode(), (String) stuInfoMap.get("loginId"))) {
                    throw new ServiceException("抱歉，学生信息同步课程空间失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("学生信息同步课程空间失败!");
            }
            sql.delete(0, sql.length());
            sql.append(" update                                                               ");
            sql.append("   ol_pe_student stu                                                  ");
            sql.append(" inner join sso_user sso on sso.id = stu.fk_sso_user_id               ");
            sql.append(" inner join enum_const sync on sync.namespace = 'FlagIsSynchronize'   ");
            sql.append(" and sync.code = '1'                                                  ");
            sql.append(" set stu.flag_is_synchronize = sync.id                                ");
            sql.append(" where stu.id = ?                                                     ");
            this.myGeneralDao.executeBySQL(sql.toString(), (String) stuInfoMap.get("stuId"));
            try {
                if (!learningSpaceWebService.saveElective(eleId, (String) stuInfoMap.get("stuId"),
                        (String) stuInfoMap.get("courseId"), new java.sql.Date(System.currentTimeMillis()).toString(),
                        false, LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new ServiceException("学生选课同步课程空间失败!");
                }
            } catch (Exception e) {
                try {
                    learningSpaceWebService.removeStudent((String) stuInfoMap.get("stuId"),
                            LearnSpaceUtil.getLearnSpaceSiteCode());
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                throw new ServiceException("学生选课同步课程空间失败!");
            }
        }
    }
}
