package com.whaty.domain.bean.online;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.util.CommonUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 订单表
 * @author weipengsen
 */
@Entity(
        name = "OlPeOrder"
)
@Table(
        name = "OL_PE_ORDER"
)
@Data
public class OlPeOrder extends AbstractBean {

    private static final long serialVersionUID = -4511537507009579774L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @Column(
            name = "order_number"
    )
    private String orderNumber;

    @Column(
            name = "input_date"
    )
    private Date inputDate;

    /**
     * 签名信息
     */
    @Column(
            name = "sign"
    )
    private String sign;
    @Column(
            name = "item_id"
    )
    private String itemId;

    /**
     * 是否支付成功
     */
    @Column(
            name = "flag_is_pay"
    )
    private EnumConst enumConstByFlagIsPAy;

    /**
     * 截取id位数
     */
    @Transient
    private final static int ID_SUBSTRING_LENGTH = 4;

    /**
     * 自增数
     */
    @Transient
    private static volatile int incrementNum = 1;

    /**
     * 获取自增数字
     * @return
     */
    private static synchronized int getIncrement() {
        return incrementNum ++;
    }

    /**
     * 生成订单号
     * yy + MM + dd + hh + mm + ss + 毫秒 + id后四位 + 2randNum
     * @return
     */
    public String generateOrderNo() {
        String timeStr = CommonUtils.getStringDate(new Date(), CommonConstant.NO_SIGN_DATETIME_TO_MILLIS_STR);
        String serialNum = String.valueOf(getIncrement());
        String idStr;
        if (serialNum.length() <= ID_SUBSTRING_LENGTH) {
            idStr = CommonUtils.leftAddZero(serialNum, ID_SUBSTRING_LENGTH);
        } else {
            idStr = serialNum.substring(serialNum.length() - ID_SUBSTRING_LENGTH);
        }
        String randNumStr = String.valueOf(Double.valueOf(Math.random() * 100).intValue());
        this.setOrderNumber(timeStr + idStr + randNumStr);
        return this.getOrderNumber();
    }
}
