package com.whaty.products.service.hbgr.energyControl.strategy;

import com.whaty.domain.bean.hbgr.energy.EnergyControlConstant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractEnergyControlStrategy {

    /**
     * 获取报警预警规则
     *
     * @param linkId
     * @return
     */
    List<Map<String, Object>> getRestriction(String linkId) throws Exception;

    /**
     * 联动控制规则
     *
     * @return
     */
    List<Map<String, Object>> getParameterInfo(String linkId) throws Exception;

    /**
     * 管井控制
     *
     * @return
     */
    List<Map<String, Object>> getTubeWellControl(String linkId) throws Exception;

    /**
     * 设备控制
     */
    void setControlDevice(Map controlMap) throws Exception;

    /**
     * 设备控制
     */
    void setParameterInfo(Map controlMap) throws Exception;

    /**
     * 设备管井开度
     */
    void setTubeWellControl(Map controlMap) throws Exception;

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
     * 添加文本类型规则
     *
     * @param title
     * @param code
     * @param value
     * @return
     * @author zhangzhe
     */
    default Map<String, Object> addCheckRestriction(String title, String code, String value) {
        return this.addRestriction("check", title, "", code, value, "", "", "",
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
     * 添加文本输入类型规则
     *
     * @param title
     * @param code
     * @param value
     * @param unit
     * @return
     * @author zhangzhe
     */
    default Map<String, Object> addDisInputRestriction(String title, String code, String value,
                                                    String unit) {
        return this.addRestriction("disInput", title, null, code, value, unit, null, null, null);
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

    /**
     * 根据二进制位数判断设备是否开启
     *
     * @param origin
     * @param index
     * @return
     */
    default String convertValueToString(String origin, int index) {
        return "1".equals(origin.substring(index - 1, index)) ? "1" : null;
    }

    default byte converStringToByte(String bytes) {
        return Byte.parseByte(String.valueOf(Math.round(Double.parseDouble(bytes) / 2.5625)));
    }

}
