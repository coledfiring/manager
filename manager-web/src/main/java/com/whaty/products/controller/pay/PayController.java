package com.whaty.products.controller.pay;

import com.alipay.api.AlipayApiException;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.products.service.pay.PayService;
import com.whaty.products.service.pay.constant.PayConstant;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.DocumentException;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 微信支付接口controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("payController")
@RequestMapping("/open/payOnline/pay")
public class PayController {

    @Resource(name = "payServiceImpl")
    private PayService payService;

    /**
     * 生成订单
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/generateOrder")
    @OperateRecord(value = "生成在线支付订单",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE, isImportant = true)
    public ResultDataModel generateOrder(@RequestBody ParamsDataModel paramsDataModel)
            throws IOException, DocumentException {
        Map<String, Object> orderParams = (Map<String, Object>) paramsDataModel
                .getParameter(PayConstant.PARAM_ORDER_INFO);
        String payType = paramsDataModel.getStringParameter(PayConstant.PARAM_PAY_TYPE);
        String orderType = paramsDataModel.getStringParameter(PayConstant.PARAM_ORDER_TYPE);
        Map<String, Object> payParams = this.payService.doGenerateOrder(orderParams, payType, orderType);
        return ResultDataModel.handleSuccessResult(payParams);
    }

    /**
     * 再次支付订单
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/payOrderAgain")
    @OperateRecord(value = "再次支付在线订单",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE, isImportant = true)
    public ResultDataModel payOrderAgain(@RequestBody ParamsDataModel paramsDataModel)
            throws IOException, DocumentException {
        String orderNo = paramsDataModel.getStringParameter(PayConstant.PARAM_ORDER_NO);
        String payType = paramsDataModel.getStringParameter(PayConstant.PARAM_PAY_TYPE);
        String orderType = paramsDataModel.getStringParameter(PayConstant.PARAM_ORDER_TYPE);
        Map<String, Object> payParams = this.payService.doPayOrderAgain(orderNo, payType, orderType);
        return ResultDataModel.handleSuccessResult(payParams);
    }

    /**
     * 完成微信订单
     * @param request
     * @return
     */
    @RequestMapping("/finishWeChatOrder")
    @OperateRecord(value = "完成微信订单",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE)
    public void finishWeChatOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException, DocumentException {
        String notifyXml = CommonUtils.getRequestReaderString(request);
        this.payService.doFinishWeChatOrder(response, notifyXml);
    }

    /**
     * 完成支付宝订单
     * @param request
     * @return
     */
    @RequestMapping("/finishAlipayOrder")
    @OperateRecord(value = "完成支付宝订单",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE)
    public void finishAlipayOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException, DocumentException, AlipayApiException {
        Map<String, String> paramsMap = RequestUtils.getRequestMap(request);
        if (StringUtils.isNotBlank(paramsMap.get("fund_bill_list"))) {
            paramsMap.put("fund_bill_list", StringEscapeUtils.unescapeHtml4(paramsMap.get("fund_bill_list")));
        }
        this.payService.doFinishAlipayOrder(response, paramsMap);
    }

    /**
     * 只生成订单不发起支付
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/generateOrderOnly")
    @OperateRecord(value = "只生成订单不发起支付",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE, isImportant = true)
    public ResultDataModel generateOrderOnly(@RequestBody ParamsDataModel paramsDataModel)
            throws IOException, DocumentException {
        Map<String, Object> orderParams = (Map<String, Object>) paramsDataModel
                .getParameter(PayConstant.PARAM_ORDER_INFO);
        String orderType = paramsDataModel.getStringParameter(PayConstant.PARAM_ORDER_TYPE);
        Map<String, Object> payParams = this.payService.doGenerateOrderOnly(orderParams, orderType);
        return ResultDataModel.handleSuccessResult(payParams);
    }

    /**
     * 生成微信签名
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/generateWeChatSign")
    @OperateRecord(value = "生成微信签名",
            moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE, isImportant = true)
    public ResultDataModel generateWeChatSign(@RequestBody ParamsDataModel paramsDataModel) {
        Map<String, Object> orderParams = (Map<String, Object>) paramsDataModel
                .getParameter(PayConstant.PARAM_ORDER_INFO);
        return ResultDataModel.handleSuccessResult(this.payService.generateWeChatSign(orderParams));
    }

}
