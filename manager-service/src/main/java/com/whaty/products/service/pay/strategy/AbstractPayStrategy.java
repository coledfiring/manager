package com.whaty.products.service.pay.strategy;

import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.Map;

/**
 * 支付抽象策略
 *
 * @author weipengsen
 */
public abstract class AbstractPayStrategy {

    /**
     * 生成订单
     * @param orderParams 业务订单信息
     * @param command 命令对象
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public abstract Map<String, Object> generateOrder(Map<String, Object> orderParams, AbstractPayOrderCommand command)
            throws IOException, DocumentException;
    /**
     * 生成订单不发起第三方支付
     * @param orderParams 业务订单信息
     * @param command 命令对象
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public abstract Map<String, Object> generateOrderWithoutThirdParty(Map<String, Object> orderParams, AbstractPayOrderCommand command)
            throws IOException, DocumentException;

    /**
     * 再次支付订单
     * @param orderNo 业务订单号
     * @param command 命令对象
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public abstract Map<String,Object> payOrderAgain(String orderNo, AbstractPayOrderCommand command)
            throws IOException, DocumentException;

    /**
     * 获取订单信息
     * @param orderNo
     * @param command
     * @return
     */
    public abstract Map<String, Object> getOrderInfo(String orderNo, AbstractPayOrderCommand command) throws IOException, DocumentException;
}
