package com.whaty.products.service.hbgr.warning.strategy;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.httpClient.helper.HttpClientHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractWarningStrategy {

    /**
     * 获取报警预警规则
     *
     * @param linkId
     * @return
     */
    Map<String, Object> getRestriction(String linkId);

    /**
     * 发送报警预警消息
     */
    void sendWarning() throws IOException;

    /**
     * 添加规则
     *
     * @param title
     * @param operate
     * @param code
     * @param unit
     * @param regEXP
     * @param regText
     * @param content
     * @author zhangzhe
     */
    default Map<String, Object> addRestriction(String type, String title, String operate, String code,
                                               String value, String unit, String regEXP, String regText,
                                               List<Object[]> content) {
        //把当前规则存入集合
        Map map = new HashMap();
        map.put("type", type);
        map.put("title", title);
        map.put("operate", operate);
        map.put("code", code);
        map.put("value", value);
        map.put("unit", unit);
        map.put("regEXP", regEXP);
        map.put("regText", regText);
        map.put("content", content);
        return map;
    }

    /**
     * 添加文本类型规则
     *
     * @param title
     * @param code
     * @param value
     * @return
     * @author zhangzhe
     */
    default Map<String, Object> addTextRestriction(String title, String code, String value) {
        return this.addRestriction("text", title, "", code, value, "", "", "",
                null);
    }

    /**
     * 添加文本输入类型规则
     *
     * @param title
     * @param operate
     * @param code
     * @param value
     * @param unit
     * @param regEXP
     * @param regText
     * @return
     * @author zhangzhe
     */
    default Map<String, Object> addInputRestriction(String title, String operate, String code, String value,
                                                    String unit, String regEXP, String regText) {
        return this.addRestriction("input", title, operate, code, value, unit, regEXP, regText, null);
    }

    /**
     * 添加选择框类型规则
     *
     * @param title
     * @param operate
     * @param code
     * @param unit
     * @param content
     * @return
     */
    default Map<String, Object> addSelectRestriction(String title, String operate, String code, String value,
                                                     String unit, List<Object[]> content) {
        return this.addRestriction("select", title, operate, code, value, unit, "", "", content);
    }

    /**
     * 将毕业规则或学位规则中的具体规则内容转换成String类型
     *
     * @param value
     * @return
     * @author zhangzhe
     */
    default String convertValueToString(Object value) {
        return value == null ? null : value.toString();
    }

    default void sendDingTalk(String msg) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时");
        String s = "{'msgtype':'text'," +
                " 'text': {'content':'111 报警预警通知 \n\n" +
                "报警时间：" + format.format(new Date()) + "\n\n" +
                "报警信息：" + msg + "\n\n'}," +
                " 'at': {'isAtAll': true}}";
        System.out.println(new HttpClientHelper().doPostJSON(CommonConstant.DING_DING_ROBOT_URL, s).getContent());
    }

}
