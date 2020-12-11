package com.whaty.products.service.businessunit.constants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.whaty.products.service.businessunit.utils.Md5;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * 北交大易智付对接在线支付常量
 * @author shanshuai
 */
public class EasyPayUtils {

    /**
     * 易智付订单数字指纹 key值md5加密
     * @param key
     * @return
     * @throws IOException
     */
    public static String hmac_Md5(String key) throws IOException {
        Md5 md5 = new Md5("");
        md5.hmac_Md5(key, EasyPayConstant.EASY_PAY_KEY);
        return md5.stringify(md5.getDigest());
    }


    /**
     * 易智付查询订单信息
     * @param orderId
     * @return
     * @throws Exception
     */
    public static String queryEasyPayOrderInfo(String orderId) throws Exception {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        Client client = Client.create();
        WebResource webResource = client.resource(EasyPayConstant.EASY_PAY_SINGLE_CHECK_URL);
        MultivaluedMap multivaluedMap = new MultivaluedMapImpl();
        multivaluedMap.add("v_mid", EasyPayConstant.EASY_PAY_MID);
        multivaluedMap.add("v_oid", orderId);
        multivaluedMap.add("v_mac", hmac_Md5(EasyPayConstant.EASY_PAY_MID + orderId));
        return webResource.queryParams(multivaluedMap).get(String.class);
    }

}
