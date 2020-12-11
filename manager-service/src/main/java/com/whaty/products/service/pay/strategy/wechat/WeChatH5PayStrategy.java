package com.whaty.products.service.pay.strategy.wechat;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.pay.constant.WeChatApiConstant;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付策略
 *
 * @author weipengsen
 */
@Lazy
@Component("weChatH5PayStrategy")
public class WeChatH5PayStrategy extends AbstractWeChatPayStrategy {

    @Override
    protected Map<String, Object> convertDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData) {
        Map<String, Object> viewData = new HashMap<>(4);
        viewData.put("payUrl", responseData.get(WeChatApiConstant.RESPONSE_NOTIFY_URL) + "&redirect_url="
                + orderInfo.get("returnUrl"));
        return viewData;
    }

    @Override
    protected Map<String, String> addThirdPartyExtraParams(Map<String, Object> orderInfo) {
        Map<String, String> extraParams = new HashMap<>(2);
        extraParams.put(WeChatApiConstant.PARAM_SCENE_INFO, this.generateSceneInfo());
        return extraParams;
    }

    /**
     * 生成场景信息
     * @return
     */
    private String generateSceneInfo() {
        Map<String, Object> sceneInfo = new HashMap<>(2);
        Map<String, String> infoMap = new HashMap<>(8);
        sceneInfo.put(WeChatApiConstant.PARAM_H5_INFO, infoMap);
        infoMap.put(WeChatApiConstant.PARAM_TYPE, "Wap");
        infoMap.put(WeChatApiConstant.PARAM_WAP_URL, "http://" + SiteUtil.getSite().getDomain());
        infoMap.put(WeChatApiConstant.PARAM_WAP_NAME, SiteUtil.getSite().getName());
        return JSONObject.fromObject(sceneInfo).toString();
    }

    @Override
    protected String getTradeType() {
        return WeChatApiConstant.H5_TRADE_TYPE;
    }
}
