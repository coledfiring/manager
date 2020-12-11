package com.whaty.products.service.pay.strategy.wechat;

import com.whaty.products.service.pay.constant.WeChatApiConstant;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付策略
 *
 * @author jingrui
 */
@Lazy
@Component("weChatNativePayStrategy")
public class WeChatNativePayStrategy extends AbstractWeChatPayStrategy {

    @Override
    protected Map<String, String> addThirdPartyExtraParams(Map<String, Object> orderInfo) {
        Map<String, String> extraParams = new HashMap<>(2);
        extraParams.put(WeChatApiConstant.PARAM_PRODUCT_ID, (String) orderInfo.get("orderNo"));
        return extraParams;
    }

    @Override
    protected Map<String, Object> convertDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData) {
        Map<String, Object> viewData = new TreeMap<>();
        viewData.put("totalFee", orderInfo.get("totalPrice"));
        viewData.put("payType", "weChatPay");
        viewData.put("codeUrl", responseData.get(WeChatApiConstant.PARAM_CODE_URL));
        viewData.put("photo", this.imgUrl((String)responseData.get(WeChatApiConstant.PARAM_CODE_URL)));
        return viewData;
    }

    private String imgUrl(String url) {
        ByteArrayOutputStream out= QRCode.from(url).to(ImageType.PNG).stream();
        byte[] data = out.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }


    @Override
    protected String getTradeType() {
        return WeChatApiConstant.NATIVE_TRADE_TYPE;
    }
}
