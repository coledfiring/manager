package com.whaty.products.service.pay.strategy.wechat;

import com.whaty.products.service.pay.constant.WeChatApiConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付策略
 *
 * @author weipengsen
 */
@Lazy
@Component("weChatMiniProgramPayStrategy")
public class WeChatMiniProgramPayStrategy extends AbstractWeChatPayStrategy {

    @Override
    protected Map<String, String> addThirdPartyExtraParams(Map<String, Object> orderInfo) {
        Map<String, String> extraParams = new HashMap<>(8);
        extraParams.put(WeChatApiConstant.PARAM_OPEN_ID, (String) orderInfo.get("openId"));
        return extraParams;
    }

    @Override
    protected Map<String, Object> convertDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData) {
        Map<String, Object> viewData = new TreeMap<>();
        viewData.put("appId", responseData.get(WeChatApiConstant.PARAM_APP_ID));
        viewData.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        viewData.put("nonceStr", responseData.get(WeChatApiConstant.PARAM_NONCE_STR));
        viewData.put("package", "prepay_id=" + responseData.get(WeChatApiConstant.PARAM_PREPAY_ID));
        viewData.put("signType", "MD5");
        viewData.put("paySign", this.generateSign(viewData));
        return viewData;
    }

    @Override
    protected String getTradeType() {
        return WeChatApiConstant.JS_API_TRADE_TYPE;
    }
}
