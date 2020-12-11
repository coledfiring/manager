package com.whaty.domain.bean.hbgr.energy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whaty.domain.bean.hbgr.warning.WarningConstant;
import com.whaty.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Objects;

@Data
public class EnergyCondition {

    /**
     *  1号锅炉
     */
    private String boiler1;

    /**
     *  2号锅炉
     */
    private String boiler2;

    /**
     *  3号锅炉
     */
    private String boiler3;

    /**
     *  4号锅炉
     */
    private String boiler4;

    /**
     *  5号锅炉
     */
    private String boiler5;

    /**
     *  6号锅炉
     */
    private String boiler6;

    /**
     *  7号锅炉
     */
    private String boiler7;

    /**
     *  1号锅炉供温
     */
    private String boiler1tem;

    /**
     *  2号锅炉供温
     */
    private String boiler2tem;

    /**
     *  3号锅炉供温
     */
    private String boiler3tem;

    /**
     *  4号锅炉供温
     */
    private String boiler4tem;

    /**
     *  5号锅炉供温
     */
    private String boiler5tem;

    /**
     *  6号锅炉供温
     */
    private String boiler6tem;

    /**
     *  7号锅炉供温
     */
    private String boiler7tem;

    /**
     *  1号锅炉后阀门
     */
    private String boiler1valve;

    /**
     *  2号锅炉后阀门
     */
    private String boiler2valve;

    /**
     *  3号锅炉后阀门
     */
    private String boiler3valve;

    /**
     *  4号锅炉后阀门
     */
    private String boiler4valve;

    /**
     *  5号锅炉后阀门
     */
    private String boiler5valve;

    /**
     *  6号锅炉后阀门
     */
    private String boiler6valve;

    /**
     *  7号锅炉后阀门
     */
    private String boiler7valve;

    /**
     *  网11号泵
     */
    private String w11pump;

    /**
     *  网12号泵
     */
    private String w12pump;

    /**
     *  网13号泵
     */
    private String w13pump;

    /**
     *  网14号泵
     */
    private String w14pump;

    /**
     *  网11号泵
     */
    private String w11pumplink;

    /**
     *  网12号泵
     */
    private String w12pumplink;

    /**
     *  网13号泵
     */
    private String w13pumplink;

    /**
     *  网14号泵
     */
    private String w14pumplink;

    /**
     *  一网水泵频率
     */
    private String w1pumprate;

    /**
     *  二网水泵频率
     */
    private String w2pumprate;

    /**
     *  网11号泵
     */
    private String w11pumprate;

    /**
     *  网12号泵
     */
    private String w12pumprate;

    /**
     *  网13号泵
     */
    private String w13pumprate;

    /**
     *  网14号泵
     */
    private String w14pumprate;

    /**
     *  网21号泵
     */
    private String w21pump;

    /**
     *  网22号泵
     */
    private String w22pump;

    /**
     *  网23号泵
     */
    private String w23pump;

    /**
     *  网24号泵
     */
    private String w24pump;

    /**
     *  网21号泵
     */
    private String w21pumprate;

    /**
     *  网22号泵
     */
    private String w22pumprate;

    /**
     *  网23号泵
     */
    private String w23pumprate;

    /**
     *  网24号泵
     */
    private String w24pumprate;

    /**
     *  地暖5号泵
     */
    private String dn5pump;

    /**
     *  地暖6号泵
     */
    private String dn6pump;

    /**
     *  是否联动控制
     */
    private String linkController;

    /**
     *  压差
     */
    private String pressureAuto;

    /**
     *  切泵时间间隔
     */
    private String timePumpAuto;


    /**
     * 网二频率上限
     */
    private String w1RateUpLimit;

    /**
     * 网一频率上限
     */
    private String w1RateDownLimit;

    /**
     * 网一增泵频率
     */
    private String w1AddRate;

    /**
     * 网一减泵频率
     */
    private String w1SubRate;

    /**
     * 网一定压差
     */
    private String w1Pressure;

    /**
     * 网一定压死区
     */
    private String w1PressureZone;

    /**
     *  一胺网频率增量
     */
    private String w1RateAdd;

    /**
     * 一网增量周期
     */
    private String w1AddCycle;


    /**
     * 网二切泵时间间隔
     */
    private String w2TimePumpAuto;

    /**
     * 网二频率上限
     */
    private String w2RateUpLimit;

    /**
     * 网二频率上限
     */
    private String w2RateDownLimit;

    /**
     * 网二增泵频率
     */
    private String w2AddRate;

    /**
     * 网二减泵频率
     */
    private String w2SubRate;

    /**
     * 网二定压差
     */
    private String w2Pressure;

    /**
     * 网二定压死区
     */
    private String w2PressureZone;

    /**
     *  二胺网频率增量
     */
    private String w2RateAdd;

    /**
     * 二网增量周期
     */
    private String w2AddCycle;

    /**
     * 水箱液位上限
     */
    private String waterBoxUp;

    /**
     * 水箱液位下限
     */
    private String waterBoxDown;

    /**
     * 膨胀水箱液位上限
     */
    private String pWaterBoxUp;

    /**
     * 膨胀水箱液位下限
     */
    private String pWaterBoxDown;


    public EnergyCondition() {
    }

    /**
     * 从字符串类型的xml构造对象
     *
     * @author 杨子尧
     */
    public EnergyCondition(String json) {
        try {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
            CommonUtils.convertMapToBean(map, EnergyCondition.class);
            /*if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler1))) {
                boiler1 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler1));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler2))) {
                boiler2 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler2));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler3))) {
                boiler3 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler3));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler4))) {
                boiler4 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler4));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler5))) {
                boiler5 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler5));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler6))) {
                boiler6 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler6));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler7))) {
                boiler7 = Integer.valueOf((String)map.get(EnergyControlConstant.boiler7));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler1tem))) {
                boiler1tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler1tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler2tem))) {
                boiler2tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler2tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler3tem))) {
                boiler3tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler3tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler4tem))) {
                boiler4tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler4tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler5tem))) {
                boiler5tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler5tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler6tem))) {
                boiler6tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler6tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler7tem))) {
                boiler7tem = Integer.valueOf((String)map.get(EnergyControlConstant.boiler7tem));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler1valve))) {
                boiler1valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler1valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler2valve))) {
                boiler2valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler2valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler3valve))) {
                boiler3valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler3valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler4valve))) {
                boiler4valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler4valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler5valve))) {
                boiler5valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler5valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler6valve))) {
                boiler6valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler6valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.boiler7valve))) {
                boiler7valve = Integer.valueOf((String)map.get(EnergyControlConstant.boiler7valve));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w11pump))) {
                w11pump = Integer.valueOf((String)map.get(EnergyControlConstant.w11pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w12pump))) {
                w12pump = Integer.valueOf((String)map.get(EnergyControlConstant.w12pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w13pump))) {
                w13pump = Integer.valueOf((String)map.get(EnergyControlConstant.w13pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w14pump))) {
                w14pump = Integer.valueOf((String)map.get(EnergyControlConstant.w14pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w11pumprate))) {
                w11pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w11pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w12pumprate))) {
                w12pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w12pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w13pumprate))) {
                w13pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w13pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w14pumprate))) {
                w14pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w14pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w21pump))) {
                w21pump = Integer.valueOf((String)map.get(EnergyControlConstant.w21pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w22pump))) {
                w22pump = Integer.valueOf((String)map.get(EnergyControlConstant.w22pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w23pump))) {
                w23pump = Integer.valueOf((String)map.get(EnergyControlConstant.w23pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w24pump))) {
                w24pump = Integer.valueOf((String)map.get(EnergyControlConstant.w24pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w21pumprate))) {
                w21pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w21pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w22pumprate))) {
                w22pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w22pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w23pumprate))) {
                w23pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w23pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.w24pumprate))) {
                w24pumprate = Integer.valueOf((String)map.get(EnergyControlConstant.w24pumprate));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.dn5pump))) {
                dn5pump = Integer.valueOf((String)map.get(EnergyControlConstant.dn5pump));
            }
            if (StringUtils.isNotBlank((String) map.get(EnergyControlConstant.dn6pump))) {
                dn6pump = Integer.valueOf((String)map.get(EnergyControlConstant.dn6pump));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
