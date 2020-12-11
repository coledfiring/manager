package com.whaty.products.service.pay.factory;

import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.PayConstant;

/**
 * 支付订单命令工厂
 *
 * @author weipengsen
 */
public class PayOrderCommandFactory {

    /**
     * 生产支付订单命令
     * @param orderType
     * @return
     */
    public static AbstractPayOrderCommand newInstance(String orderType) {
        switch (orderType) {
            case PayConstant.PAY_ORDER_TYPE_STUDENT_ENROLL_ORDER:
                return (AbstractPayOrderCommand) SpringUtil.getBean("studentEnrollOrderCommand");
            case PayConstant.PAY_ORDER_TYPE_OL_STUDENT_COURSE_ENROLL:
                return (AbstractPayOrderCommand) SpringUtil.getBean("olStudentCourseEnrollPayOrderCommand");
            case PayConstant.PAY_ORDER_TYPE_OL_STUDENT_CLASS_ENROLL:
                return (AbstractPayOrderCommand) SpringUtil.getBean("olStudentClassEnrollPayOrderCommand");
            case PayConstant.PAY_ORDER_TYPE_SPECIAL_EXAM_FEE:
                return (AbstractPayOrderCommand) SpringUtil.getBean("specialExamFeePayOrderCommand");
            case PayConstant.PAY_ORDER_TYPE_YYCS_REGULAR_EXAM_FEE:
                return (AbstractPayOrderCommand) SpringUtil.getBean("examSystemRegularPayOrderCommand");
            case PayConstant.PAY_ORDER_TYPE_YYCS_MOCK_EXAM_FEE:
                return (AbstractPayOrderCommand) SpringUtil.getBean("examSystemMockPracticePayOrderCommand");
            default:
                throw new ParameterIllegalException();
        }
    }

}
