package com.whaty.products.service.pay.command;

import java.util.Map;

/**
 * 抽象支付订单命令对象
 *
 * @author weipengsen
 */
public interface AbstractPayOrderCommand {

    /**
     * 创建并生成订单
     * @param orderParams 业务订单信息
     * @return
     */
    Map<String, Object> createAndSaveOrder(Map<String, Object> orderParams);

    /**
     * 再次支付订单
     * @param orderNo
     * @return
     */
    Map<String,Object> payOrderAgain(String orderNo);

    /**
     * 完成订单
     * @param orderNo
     */
    void finishOrder(String orderNo, String payWay);
}
