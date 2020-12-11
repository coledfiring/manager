package com.whaty.products.service.businessunit.domain;

import com.whaty.schedule.util.CommonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 易智付三方接口参数领域对象
 *
 * @author shanshuai
 */
public class EasyPayParameter implements Serializable {

    private static final long serialVersionUID = 8153996065829359708L;

    /**
     * 商户编号(固定)
     */
    private String mid;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 接收人姓名    (建议使用商户编号的值代替)
     */
    private String receiveName;

    /**
     * 接收人地址    (建议使用商户编号的值代替)
     */
    private String receiveAddress;

    /**
     * 接收人电话    (建议使用商户编号的值代替)
     */
    private String receiveTel;

    /**
     * 收货人邮政编码    (建议使用商户编号的值代替)
     */
    private String receivePost;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 订单产生日期
     */
    private String orderDate;

    /**
     * 商户配货状态
     */
    private String orderStatus;

    /**
     * 订货人姓名    (建议使用商户编号的值代替)
     */
    private String orderName;

    /**
     * 币种 0 为人民币
     */
    private String moneytype;

    /**
     * 反馈地址 返回商户页面地址
     */
    private String callBackUrl;

    /**
     * 跳转易智付支付接口
     */
    public String easyPayUrl;

    /**
     * md5加密    订单数字指纹
     */
    private String md5Info;

    /**
     * 支付方式
     */
    private String payMode;

    /**
     * 支付状态
     */
    private String payStatus;

    public EasyPayParameter() {
        this.mid = "9261";
        this.receiveName = "9261";
        this.receiveAddress = "9261";
        this.receiveTel = "9261";
        this.receivePost = "9261";
        this.moneytype = "0";
        this.orderStatus = "1";
        this.orderName = "9261";
        this.callBackUrl = CommonUtils.getBasicUrl() + "/open/easyPay/syncReceiveEasyPayOrderInfo";
        this.easyPayUrl = "https://pay.yizhifubj.com/customer/gb/pay_bank.jsp";
    }

    public EasyPayParameter(BigDecimal amount, String orderDate) {
        this();
        this.amount = amount;
        this.orderDate = orderDate;
    }

    /**
     * 自增数
     */
    private static volatile int incrementNum = 1;

    /**
     * 获取自增数字
     * @return
     */
    private static synchronized int getIncrement() {
        return incrementNum ++;
    }

    private final static int ID_SUBSTRING_LENGTH = 4;

    /**
     * 生成易智付订单编号
     *
     * @return
     */
    public String generateOrderNo(String orderDate, String businessOrderId) {
        String timeStr = CommonUtils.changeDateToString(new Date(), "HHmmss");
        String randNumStr = String.valueOf(Double.valueOf(Math.random() * 100).intValue());
        return orderDate + "-" + this.getMid() + "-" + timeStr + randNumStr + "-" + businessOrderId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveTel() {
        return receiveTel;
    }

    public void setReceiveTel(String receiveTel) {
        this.receiveTel = receiveTel;
    }

    public String getReceivePost() {
        return receivePost;
    }

    public void setReceivePost(String receivePost) {
        this.receivePost = receivePost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMoneytype() {
        return moneytype;
    }

    public void setMoneytype(String moneytype) {
        this.moneytype = moneytype;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getEasyPayUrl() {
        return easyPayUrl;
    }

    public void setEasyPayUrl(String easyPayUrl) {
        this.easyPayUrl = easyPayUrl;
    }

    public String getMd5Info() {
        return md5Info;
    }

    public void setMd5Info(String md5Info) {
        this.md5Info = md5Info;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public static int getIncrementNum() {
        return incrementNum;
    }

    public static void setIncrementNum(int incrementNum) {
        EasyPayParameter.incrementNum = incrementNum;
    }

    public static int getIdSubstringLength() {
        return ID_SUBSTRING_LENGTH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EasyPayParameter that = (EasyPayParameter) o;
        return Objects.equals(mid, that.mid) &&
                Objects.equals(orderNo, that.orderNo) &&
                Objects.equals(receiveName, that.receiveName) &&
                Objects.equals(receiveAddress, that.receiveAddress) &&
                Objects.equals(receiveTel, that.receiveTel) &&
                Objects.equals(receivePost, that.receivePost) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(orderDate, that.orderDate) &&
                Objects.equals(orderStatus, that.orderStatus) &&
                Objects.equals(orderName, that.orderName) &&
                Objects.equals(moneytype, that.moneytype) &&
                Objects.equals(callBackUrl, that.callBackUrl) &&
                Objects.equals(easyPayUrl, that.easyPayUrl) &&
                Objects.equals(md5Info, that.md5Info) &&
                Objects.equals(payMode, that.payMode) &&
                Objects.equals(payStatus, that.payStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mid, orderNo, receiveName, receiveAddress, receiveTel, receivePost, amount,
                orderDate, orderStatus, orderName, moneytype, callBackUrl, easyPayUrl, md5Info, payMode, payStatus);
    }

    @Override
    public String toString() {
        return "EasyPayParameter{" +
                "mid='" + mid + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receiveAddress='" + receiveAddress + '\'' +
                ", receiveTel='" + receiveTel + '\'' +
                ", receivePost='" + receivePost + '\'' +
                ", amount=" + amount +
                ", orderDate='" + orderDate + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderName='" + orderName + '\'' +
                ", moneytype='" + moneytype + '\'' +
                ", callBackUrl='" + callBackUrl + '\'' +
                ", easyPayUrl='" + easyPayUrl + '\'' +
                ", md5Info='" + md5Info + '\'' +
                ", payMode='" + payMode + '\'' +
                ", payStatus='" + payStatus + '\'' +
                '}';
    }
}
