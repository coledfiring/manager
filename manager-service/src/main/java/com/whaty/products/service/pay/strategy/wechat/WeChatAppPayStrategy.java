package com.whaty.products.service.pay.strategy.wechat;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.PayConstant;
import com.whaty.products.service.pay.constant.WeChatApiConstant;
import org.dom4j.DocumentException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信app支付策略
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("weChatAppPayStrategy")
public class WeChatAppPayStrategy extends AbstractWeChatPayStrategy {

    @Override
    protected Map<String, String> addThirdPartyExtraParams(Map<String, Object> orderInfo) {
        Map<String, String> extraParams = new HashMap<>(8);
        return extraParams;
    }

    @Override
    protected Map<String, Object> convertDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData) {
        return null;
    }

    @Override
    public Map<String, Object> payOrderAgain(String orderNo, AbstractPayOrderCommand command)
            throws IOException, DocumentException {
        Map<String, Object> orderInfo = command.payOrderAgain(orderNo);
        String orderParams = this.collectThirdPartyOrder(orderInfo);
        this.invokeThirdParty(orderParams,
                SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_WX).getWechatPayUrl());
        return Collections.singletonMap("info", this.collectThirdPartyOrder(orderInfo));
    }

    @Override
    protected String getTradeType() {
        return WeChatApiConstant.APP_TRADE_TYPE;
    }


}
