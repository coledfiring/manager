package com.whaty.products.service.pay.command;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ServiceException;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 抽象支付学生报名考试订单命令对象
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("studentEnrollOrderCommand")
public class StudentEnrollOrderCommand implements AbstractPayOrderCommand {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map<String, Object> payOrderAgain(String orderNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                            ");
        sql.append("   efo.order_number as orderNo,                                                    ");
        sql.append("   efo.fee_amount as totalPrice,                                                   ");
        sql.append("   efo.input_date as orderDate,                                                    ");
        sql.append("   type.name as type,                                                              ");
        sql.append("   'http://whatypx.cep.webtrn.cn/ws/open/studentEnroll/enrollInfo' as returnUrl,   ");
        sql.append("   'studentEnrollOrder' as attach,                                                 ");
        sql.append("   efo.name as description                                                         ");
        sql.append(" from                                                                              ");
        sql.append("   pe_order efo                                                                    ");
        sql.append(" inner join enum_const pay on pay.id = efo.flag_is_pay                             ");
        sql.append(" inner join enum_const type on type.id = efo.flag_order_type                       ");
        sql.append(" where                                                                             ");
        sql.append("   efo.order_number = '" + orderNo + "'                                            ");
        sql.append(" and  pay.code = '0'                                                               ");
        sql.append(" and  type.code = '1'                                                              ");
        Map<String, Object> orderInfo = this.myGeneralDao.getOneMapBySQL(sql.toString());
        if (MapUtils.isEmpty(orderInfo)) {
            throw new ServiceException("订单已完成或正在支付中，请刷新后重试");
        }
        orderInfo.put("timeoutExpress","30m");
        Date date = (Date) orderInfo.get("orderDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date.setTime(Math.addExact(date.getTime(), 30 * 60 * 1000));
        orderInfo.put("timeoutTime", format.format(date));
        return orderInfo;
    }

    @Override
    public Map<String, Object> createAndSaveOrder(Map<String, Object> orderParams) {
        throw new ServiceException("功能未完成");
    }

    @Override
    public void finishOrder(String orderNo, String payWay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update pe_order po                                                ");
        sql.append(" inner join pe_student stu on po.fk_student_id = stu.id            ");
        sql.append(" and stu.fk_training_item_id = po.item_id                          ");
        sql.append(" inner join enum_const pt on pt.namespace = 'flagIsPay'            ");
        sql.append(" and pt.code = '1'                                                 ");
        sql.append(" inner join enum_const ft on ft.namespace = 'flagFeeStatus'        ");
        sql.append(" and ft.code = '1'                                                 ");
        sql.append(" inner join enum_const pw on pw.namespace = 'flagPayWay'           ");
        sql.append(" and pw.code = '2'                                                 ");
        sql.append(" set po.flag_is_pay=pt.id,                                         ");
        sql.append(" stu.flag_fee_status=ft.id,                                        ");
        sql.append(" stu.flag_pay_way=pw.id,                                           ");
        sql.append(" pay_date = now()                                                  ");
        sql.append(" where order_number = '" + orderNo + "'                            ");
        this.myGeneralDao.executeBySQL(sql.toString());
    }
}
