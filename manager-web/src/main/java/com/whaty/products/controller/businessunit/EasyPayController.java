package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.util.RequestUtils;
import com.whaty.domain.bean.PeBusinessOrder;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessOrderServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 北交大易智付对外接口controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/open/easyPay")
public class EasyPayController extends TycjGridBaseControllerAdapter<PeBusinessOrder> {

    @Resource(name = "peBusinessOrderService")
    private PeBusinessOrderServiceImpl peBusinessOrderService;

    /**
     * 易智付支付后同步回调方法    （接收易智付缴费状态信息）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/syncReceiveEasyPayOrderInfo")
    @OperateRecord(value = "易智付支付后同步回调方法：接收易智付缴费状态信息",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void syncReceiveEasyPayOrderInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> requestMap = RequestUtils.getRequestMap(request);
        peBusinessOrderService.doSyncReceiveEasyPayOrderInfo(requestMap);
        response.sendRedirect("/");
    }

    /**
     * 易智付支付后异步回调方法
     * @param request
     * @throws IOException
     */
    @PostMapping("/asyncReceiveEasyPayOrderInfo")
    @OperateRecord(value = "易智付支付后异步回调方法：接收易智付缴费状态信息",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void asyncReceiveEasyPayOrderInfo(HttpServletRequest request) throws IOException {
        Map<String, String> requestMap = RequestUtils.getRequestMap(request);
        peBusinessOrderService.doAsyncReceiveEasyPayOrderInfo(requestMap);
    }

}
