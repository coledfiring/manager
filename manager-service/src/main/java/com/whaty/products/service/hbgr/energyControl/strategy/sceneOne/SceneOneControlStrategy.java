package com.whaty.products.service.hbgr.energyControl.strategy.sceneOne;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.energy.EnergyCondition;
import com.whaty.domain.bean.hbgr.energy.EnergyControlConstant;
import com.whaty.domain.bean.hbgr.warning.WarningConstant;
import com.whaty.handler.SiemensS7NetHandler;
import com.whaty.products.service.hbgr.energyControl.strategy.AbstractEnergyControlStrategy;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Lazy
@Service("sceneOneControlStrategy")
public class SceneOneControlStrategy implements AbstractEnergyControlStrategy {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    /**
     * 设置规则
     *
     * @param linkId
     * @return
     */
    @Override
    public List<Map<String, Object>> getRestriction(String linkId) throws Exception {
        //获取规则内容
        Map<String, Object> configMap = this.openGeneralDao.getOneMapBySQL("SELECT id as id, name as name," +
                " config as config FROM pr_service_config WHERE link = ?", linkId);
        //规则设置页面布局map
        List<Map<String, Object>> restrictionList = new ArrayList<>();
        Map<String, Object> restrictionMap = new HashMap<>(4);
        List<List<Map<String, Object>>> options = new ArrayList<>();
        restrictionMap.put("options", options);
        restrictionMap.put("restrictionName", "站内设备控制：");
        restrictionMap.put("restrictionId", configMap.get("id"));
        EnergyCondition energyCondition;
        if (configMap.get("config") != null) {
            energyCondition = CommonUtils
                    .convertMapToBean((Map<String, Object>) JSON.parse((String) configMap.get("config")),
                            EnergyCondition.class);
        } else {
            energyCondition = new EnergyCondition();
        }
        AtomicReference<String> boillerNum = new AtomicReference<>("00000000");
        AtomicReference<String> pumpNum = new AtomicReference<>("00000000");
        AtomicReference<String> autoNum = new AtomicReference<>("00000000");
        AtomicReference<String> valveNum1 = new AtomicReference<>("00000000");
        AtomicReference<String> valveNum2 = new AtomicReference<>("00000000");
        boolean flagController = StringUtils.isBlank(energyCondition.getLinkController());
        new SiemensS7NetHandler("220.194.141.5", 10080, e -> {
            boillerNum.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M11").Content));
            pumpNum.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M2").Content));
            autoNum.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M1").Content));
            valveNum1.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M14").Content));
            valveNum2.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M15").Content));
        }).handle();
        AtomicReference<String> pumpNum2 = new AtomicReference<>("00000000");
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            pumpNum2.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M1").Content));
        }).handle();
        AtomicReference<String> pumpNum3 = new AtomicReference<>("00000000");
        new SiemensS7NetHandler("220.194.141.5", 30080, e -> {
            pumpNum3.set(CommonUtils.getBinaryStrFromByte(e.ReadByte("M1").Content));
        }).handle();
        List<Map<String, Object>> optionsAuto = new ArrayList<>();
        optionsAuto.add(this.addInputRestriction("切泵时间间隔：", null, EnergyControlConstant.timePumpAuto, this.convertValueToString(energyCondition.getTimePumpAuto()),
                "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        optionsAuto.add(this.addInputRestriction("压差给定：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "Mpa", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> options0 = new ArrayList<>();
        options0.add(this.addCheckRestriction("联动控制", EnergyControlConstant.linkController,
                this.convertValueToString(energyCondition.getLinkController())));
        List<Map<String, Object>> options1 = new ArrayList<>();
        options1.add(this.addTextRestriction("1号锅炉", EnergyControlConstant.boiler1,
                this.convertValueToString(boillerNum.get(), 1)));
        options1.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler1tem, this.convertValueToString(energyCondition.getBoiler1tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options1.add(this.addTextRestriction("1号炉后阀门", EnergyControlConstant.boiler1valve,
                this.convertValueToString(valveNum1.get(), 1)));
        List<Map<String, Object>> options2 = new ArrayList<>();
        options2.add(this.addTextRestriction("2号锅炉", EnergyControlConstant.boiler2,
                this.convertValueToString(boillerNum.get(), 2)));
        options2.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler2tem, this.convertValueToString(energyCondition.getBoiler2tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options2.add(this.addTextRestriction("2号炉后阀门", EnergyControlConstant.boiler2valve,
                this.convertValueToString(valveNum1.get(), 3)));
        List<Map<String, Object>> options3 = new ArrayList<>();
        options3.add(this.addTextRestriction("3号锅炉", EnergyControlConstant.boiler3,
                this.convertValueToString(boillerNum.get(), 3)));
        options3.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler3tem, this.convertValueToString(energyCondition.getBoiler3tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options3.add(this.addTextRestriction("3号炉后阀门", EnergyControlConstant.boiler3valve,
                this.convertValueToString(valveNum1.get(), 5)));
        List<Map<String, Object>> options4 = new ArrayList<>();
        options4.add(this.addTextRestriction("4号锅炉", EnergyControlConstant.boiler4,
                this.convertValueToString(boillerNum.get(), 4)));
        options4.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler4tem, this.convertValueToString(energyCondition.getBoiler4tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options4.add(this.addTextRestriction("4号炉后阀门", EnergyControlConstant.boiler4valve,
                this.convertValueToString(valveNum1.get(), 7)));
        List<Map<String, Object>> options5 = new ArrayList<>();
        options5.add(this.addTextRestriction("5号锅炉", EnergyControlConstant.boiler5,
                this.convertValueToString(boillerNum.get(), 5)));
        options5.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler5tem, this.convertValueToString(energyCondition.getBoiler5tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options5.add(this.addTextRestriction("5号炉后阀门", EnergyControlConstant.boiler5valve,
                this.convertValueToString(valveNum2.get(), 1)));
        List<Map<String, Object>> options6 = new ArrayList<>();
        options6.add(this.addTextRestriction("6号锅炉", EnergyControlConstant.boiler6,
                this.convertValueToString(boillerNum.get(), 6)));
        options6.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler6tem, this.convertValueToString(energyCondition.getBoiler6tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options6.add(this.addTextRestriction("6号炉后阀门", EnergyControlConstant.boiler6valve,
                this.convertValueToString(valveNum2.get(), 3)));
        List<Map<String, Object>> options7 = new ArrayList<>();
        options7.add(this.addTextRestriction("7号锅炉", EnergyControlConstant.boiler7,
                this.convertValueToString(boillerNum.get(), 7)));
        options7.add(this.addInputRestriction("供温设定：", null, EnergyControlConstant.boiler7tem, this.convertValueToString(energyCondition.getBoiler7tem()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options7.add(this.addTextRestriction("7号炉后阀门", EnergyControlConstant.boiler7valve,
                this.convertValueToString(valveNum2.get(), 5)));
        List<Map<String, Object>> options80 = new ArrayList<>();
        options80.add(this.addInputRestriction("一网水泵频率：", null, EnergyControlConstant.w1pumprate, this.convertValueToString(energyCondition.getW1pumprate()),
                "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> options8 = new ArrayList<>();
        options8.add(this.addTextRestriction("一网1号水泵", EnergyControlConstant.w11pump,
                flagController ? this.convertValueToString(pumpNum.get(), 5) : this.convertValueToString(autoNum.get(), 8)));
        options8.add(this.addTextRestriction("地暖5号水泵", EnergyControlConstant.dn5pump,
                this.convertValueToString(pumpNum3.get(), 1)));
        List<Map<String, Object>> options9 = new ArrayList<>();
        options9.add(this.addTextRestriction("一网2号水泵", EnergyControlConstant.w12pump,
                this.convertValueToString(pumpNum.get(), flagController ? 6 : 1)));
        options9.add(this.addTextRestriction("地暖6号水泵", EnergyControlConstant.dn6pump,
                this.convertValueToString(pumpNum3.get(), 2)));
        List<Map<String, Object>> options10 = new ArrayList<>();
        options10.add(this.addTextRestriction("一网3号水泵", EnergyControlConstant.w13pump,
                this.convertValueToString(pumpNum.get(), flagController ? 7 : 2)));
        List<Map<String, Object>> options11 = new ArrayList<>();
        options11.add(this.addTextRestriction("一网4号水泵", EnergyControlConstant.w14pump,
                this.convertValueToString(pumpNum.get(), flagController ? 8 : 3)));
        List<Map<String, Object>> options120 = new ArrayList<>();
        options120.add(this.addInputRestriction("二网水泵频率：", null, EnergyControlConstant.w2pumprate, this.convertValueToString(energyCondition.getW2pumprate()),
                "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> options12 = new ArrayList<>();
        options12.add(this.addTextRestriction("二网1号水泵", EnergyControlConstant.w21pump,
                this.convertValueToString(pumpNum2.get(), 1)));
        List<Map<String, Object>> options13 = new ArrayList<>();
        options13.add(this.addTextRestriction("二网2号水泵", EnergyControlConstant.w22pump,
                this.convertValueToString(pumpNum2.get(), 2)));
        List<Map<String, Object>> options14 = new ArrayList<>();
        options14.add(this.addTextRestriction("二网3号水泵", EnergyControlConstant.w23pump,
                this.convertValueToString(pumpNum2.get(), 3)));
        List<Map<String, Object>> options15 = new ArrayList<>();
        options15.add(this.addTextRestriction("二网4号水泵", EnergyControlConstant.w24pump,
                this.convertValueToString(pumpNum2.get(), 4)));
        options.add(options0);
        options.add(optionsAuto);
        options.add(options1);
        options.add(options2);
        options.add(options3);
        options.add(options4);
        options.add(options5);
        options.add(options6);
        options.add(options7);
        options.add(options80);
        options.add(options8);
        options.add(options9);
        options.add(options10);
        options.add(options11);
        options.add(options120);
        options.add(options12);
        options.add(options13);
        options.add(options14);
        options.add(options15);
        restrictionList.add(restrictionMap);
        return restrictionList;
    }

    @Override
    public List<Map<String, Object>> getParameterInfo(String linkId) throws Exception {
        //获取规则内容
        Map<String, Object> configMap = this.openGeneralDao.getOneMapBySQL("SELECT id as id, name as name," +
                " config as config FROM pr_service_config WHERE link = ?", linkId);
        //规则设置页面布局map
        List<Map<String, Object>> restrictionList = new ArrayList<>();
        Map<String, Object> restrictionMap = new HashMap<>(4);
        Map<String, Object> restrictionMap3 = new HashMap<>(4);
        List<List<Map<String, Object>>> waterOptions = new ArrayList<>();
        restrictionMap3.put("options", waterOptions);
        restrictionMap3.put("restrictionName", "水箱液位参数设置：");
        Map<String, Object> restrictionMap2 = new HashMap<>(4);
        List<List<Map<String, Object>>> w2Options = new ArrayList<>();
        restrictionMap2.put("options", w2Options);
        restrictionMap2.put("restrictionName", "二网空调泵参数设置：");
        List<List<Map<String, Object>>> boilerOptions = new ArrayList<>();
        restrictionMap.put("options", boilerOptions);
        restrictionMap.put("restrictionName", "锅炉出水温度参数设置：");
        restrictionMap.put("restrictionId", configMap.get("id"));
        Map<String, Object> restrictionMap1 = new HashMap<>(4);
        List<List<Map<String, Object>>> options = new ArrayList<>();
        restrictionMap1.put("options", options);
        restrictionMap1.put("restrictionName", "一网循环泵参数设置：");
        EnergyCondition energyCondition;
        if (configMap.get("config") != null) {
            energyCondition = CommonUtils
                    .convertMapToBean((Map<String, Object>) JSON.parse((String) configMap.get("config")),
                            EnergyCondition.class);
        } else {
            energyCondition = new EnergyCondition();
        }
        new SiemensS7NetHandler("220.194.141.5", 10080, e -> {
            List<Map<String, Object>> options1 = new ArrayList<>();
            options1.add(this.addInputRestriction("切泵时间间隔：", null, EnergyControlConstant.timePumpAuto, e.ReadInt16("DB1.74").Content.toString(),
                    "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options1.add(this.addInputRestriction("频率上限：", null, EnergyControlConstant.w1RateUpLimit, e.ReadFloat("DB1.184").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options1.add(this.addInputRestriction("频率下限：", null, EnergyControlConstant.w1RateDownLimit, e.ReadFloat("DB1.188").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            List<Map<String, Object>> options2 = new ArrayList<>();
            options2.add(this.addInputRestriction("增泵频率：", null, EnergyControlConstant.w1AddRate, e.ReadFloat("DB1.192").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options2.add(this.addInputRestriction("减泵频率：", null, EnergyControlConstant.w1SubRate, e.ReadFloat("DB1.196").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options2.add(this.addInputRestriction("一网定压差：", null, EnergyControlConstant.w1Pressure, e.ReadFloat("DB1.176").Content.toString(),
                    "Mpa", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            List<Map<String, Object>> options3 = new ArrayList<>();
            options3.add(this.addInputRestriction("压差死区：", null, EnergyControlConstant.w1PressureZone, e.ReadFloat("DB1.180").Content.toString(),
                    "Mpa", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options3.add(this.addInputRestriction("频率增量：", null, EnergyControlConstant.w1RateAdd, e.ReadFloat("DB1.8").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options3.add(this.addInputRestriction("增量周期：", null, EnergyControlConstant.w1AddCycle, e.ReadInt16("DB1.72").Content.toString(),
                    "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options.add(options1);
            options.add(options2);
            options.add(options3);
        }).handle();
        List<Map<String, Object>> boilerOption1 = new ArrayList<>();
        boilerOption1.add(this.addInputRestriction("二供温控制死区：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        boilerOption1.add(this.addInputRestriction("二供温控制增量：", null, EnergyControlConstant.w1RateUpLimit, this.convertValueToString(energyCondition.getPressureAuto()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        boilerOption1.add(this.addInputRestriction("二供温控制周期：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> boilerOption2 = new ArrayList<>();
        boilerOption2.add(this.addInputRestriction("增炉温度：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        boilerOption2.add(this.addInputRestriction("减炉温度：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> boilerOption3 = new ArrayList<>();
        boilerOption3.add(this.addInputRestriction("炉温设定上限：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        boilerOption3.add(this.addInputRestriction("炉温设定下限：", null, EnergyControlConstant.pressureAuto, this.convertValueToString(energyCondition.getPressureAuto()),
                "℃", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        boilerOptions.add(boilerOption1);
        boilerOptions.add(boilerOption2);
        boilerOptions.add(boilerOption3);
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            List<Map<String, Object>> w2Option1 = new ArrayList<>();
            w2Option1.add(this.addInputRestriction("切泵时间间隔：", null, EnergyControlConstant.w2TimePumpAuto, e.ReadInt16("DB1.118").Content.toString(),
                    "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option1.add(this.addInputRestriction("频率上限：", null, EnergyControlConstant.w2RateUpLimit, e.ReadFloat("DB1.48").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option1.add(this.addInputRestriction("频率下限：", null, EnergyControlConstant.w2RateDownLimit, e.ReadFloat("DB1.52").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            List<Map<String, Object>> w2Option2 = new ArrayList<>();
            w2Option2.add(this.addInputRestriction("增泵频率：", null, EnergyControlConstant.w2AddRate, e.ReadFloat("DB1.56").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option2.add(this.addInputRestriction("减泵频率：", null, EnergyControlConstant.w2SubRate, e.ReadFloat("DB1.60").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option2.add(this.addInputRestriction("二网定压差：", null, EnergyControlConstant.w2Pressure, e.ReadFloat("DB1.100").Content.toString(),
                    "Mpa", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            List<Map<String, Object>> w2Option3 = new ArrayList<>();
            w2Option3.add(this.addInputRestriction("压差死区：", null, EnergyControlConstant.w2PressureZone, e.ReadFloat("DB1.104").Content.toString(),
                    "Mpa", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option3.add(this.addInputRestriction("频率增量：", null, EnergyControlConstant.w2RateAdd, e.ReadFloat("DB1.64").Content.toString(),
                    "HZ", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Option3.add(this.addInputRestriction("增量周期：", null, EnergyControlConstant.w2AddCycle, e.ReadInt16("DB1.116").Content.toString(),
                    "0.1s", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            w2Options.add(w2Option1);
            w2Options.add(w2Option2);
            w2Options.add(w2Option3);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            List<Map<String, Object>> waterOption1 = new ArrayList<>();
            waterOption1.add(this.addInputRestriction("水箱阀液位上限：", null, EnergyControlConstant.waterBoxUp, e.ReadFloat("DB1.68").Content.toString(),
                    "m", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            waterOption1.add(this.addInputRestriction("水箱阀液位下限：", null, EnergyControlConstant.waterBoxDown, e.ReadFloat("DB1.76").Content.toString(),
                    "m", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            waterOption1.add(this.addInputRestriction("膨胀水箱阀液位上限：", null, EnergyControlConstant.pWaterBoxUp, e.ReadFloat("DB1.84").Content.toString(),
                    "m", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            waterOption1.add(this.addInputRestriction("膨胀水箱阀液位下限：", null, EnergyControlConstant.pWaterBoxDown, e.ReadFloat("DB1.88").Content.toString(),
                    "m", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            waterOptions.add(waterOption1);
        }).handle();
        restrictionList.add(restrictionMap);
        restrictionList.add(restrictionMap1);
        restrictionList.add(restrictionMap2);
        restrictionList.add(restrictionMap3);
        return restrictionList;
    }

    @Override
    public List<Map<String, Object>> getTubeWellControl(String linkId) throws Exception {
        //获取规则内容
        Map<String, Object> configMap = this.openGeneralDao.getOneMapBySQL("SELECT id as id, name as name," +
                " config as config FROM pr_service_config WHERE link = ?", linkId);
        //规则设置页面布局map
        List<Map<String, Object>> restrictionList = new ArrayList<>();
        Map<String, Object> restrictionMap = new HashMap<>(4);
        List<List<Map<String, Object>>> options = new ArrayList<>();
        restrictionMap.put("options", options);
        restrictionMap.put("restrictionName", "1#管井控制：");
        restrictionMap.put("restrictionId", configMap.get("id"));
        Map<String, Object> restrictionMap1 = new HashMap<>(4);
        List<List<Map<String, Object>>> options1 = new ArrayList<>();
        restrictionMap1.put("options", options1);
        restrictionMap1.put("restrictionName", "2#管井控制：");
        Map<String, Object> restrictionMap2 = new HashMap<>(4);
        List<List<Map<String, Object>>> options2 = new ArrayList<>();
        restrictionMap2.put("options", options2);
        restrictionMap2.put("restrictionName", "3#管井控制：");
        Map<String, Object> restrictionMap3 = new HashMap<>(4);
        List<List<Map<String, Object>>> options3 = new ArrayList<>();
        restrictionMap3.put("options", options3);
        restrictionMap3.put("restrictionName", "4#管井控制：");
        Map<String, Object> restrictionMap4 = new HashMap<>(4);
        List<List<Map<String, Object>>> options4 = new ArrayList<>();
        restrictionMap4.put("options", options4);
        restrictionMap4.put("restrictionName", "5#管井控制：");
        Map<String, Object> restrictionMap5 = new HashMap<>(4);
        List<List<Map<String, Object>>> options5 = new ArrayList<>();
        restrictionMap5.put("options", options5);
        restrictionMap5.put("restrictionName", "6#管井控制：");
        new SiemensS7NetHandler("220.194.141.5", 30081, e -> {
        List<Map<String, Object>> option1 = new ArrayList<>();
        option1.add(this.addDisInputRestriction("DN200裙楼回水1温度：    ", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
        option1.add(this.addInputRestriction("DN200裙楼回水1开启度 ：    ", null, EnergyControlConstant.tubeWell11, e.ReadFloat("DB1.1194").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option1.add(this.addDisInputRestriction("DN80裙楼负一层温度：         ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option1.add(this.addInputRestriction("DN80裙楼负一层开启度：          ", null, EnergyControlConstant.tubeWell12, e.ReadFloat("DB1.1312").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> option2 = new ArrayList<>();
        option2.add(this.addDisInputRestriction("DN125裙楼回水温度：          ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option2.add(this.addInputRestriction("DN125裙楼回水开启度       ： ", null, EnergyControlConstant.tubeWell13, e.ReadFloat("DB1.1428").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option2.add(this.addDisInputRestriction("DN200裙楼回水2温度：     ", "",e.ReadFloat("DB1.1456").Content.toString(), "℃"));
        option2.add(this.addInputRestriction("DN200裙楼回水2开启度       ：", null, EnergyControlConstant.tubeWell14, e.ReadFloat("DB1.1544").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> option3 = new ArrayList<>();
        option3.add(this.addDisInputRestriction("塔楼盘管回水 温度  ：", "",e.ReadFloat("DB1.1572").Content.toString(), "℃"));
        option3.add(this.addInputRestriction("塔楼盘管回水开启度     ：", null, EnergyControlConstant.tubeWell15, e.ReadFloat("DB1.1660").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options.add(option1);
        options.add(option2);
        options.add(option3);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30082, e -> {
        List<Map<String, Object>> option21 = new ArrayList<>();
        option21.add(this.addDisInputRestriction("DN250裙楼回水温度：     ", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
        option21.add(this.addInputRestriction("DN250裙楼回水开启度：     ", null, EnergyControlConstant.tubeWell21, e.ReadFloat("DB1.1194").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option21.add(this.addDisInputRestriction("DN150裙楼回水温度：     ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option21.add(this.addInputRestriction("DN150裙楼回水开启度：     ", null, EnergyControlConstant.tubeWell22, e.ReadFloat("DB1.1312").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> option22 = new ArrayList<>();
        option22.add(this.addDisInputRestriction("DN125裙楼回水温度：    ", "",e.ReadFloat("DB1.1340").Content.toString(), "℃"));
        option22.add(this.addInputRestriction("DN125裙楼回水开启度：     ", null, EnergyControlConstant.tubeWell23, e.ReadFloat("DB1.1428").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option22.add(this.addDisInputRestriction("塔楼盘管回水 温度：     ", "",e.ReadFloat("DB1.1456").Content.toString(), "℃"));
        option22.add(this.addInputRestriction("塔楼盘管回水开启度：      ", null, EnergyControlConstant.tubeWell24, e.ReadFloat("DB1.1544").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options1.add(option21);
        options1.add(option22);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30083, e -> {
        List<Map<String, Object>> option31 = new ArrayList<>();
        option31.add(this.addDisInputRestriction("裙楼 + 负一层 温度：     ", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
        option31.add(this.addInputRestriction("裙楼 + 负一层开启度：      ", null, EnergyControlConstant.tubeWell31, e.ReadFloat("DB1.1194").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option31.add(this.addDisInputRestriction("裙楼负一层回水温度：     ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option31.add(this.addInputRestriction("裙楼负一层回水开启度：     ", null, EnergyControlConstant.tubeWell32, e.ReadFloat("DB1.1312").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options2.add(option31);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30084, e -> {
        List<Map<String, Object>> option41 = new ArrayList<>();
        option41.add(this.addDisInputRestriction("裙楼 + 负一层 温度：    ", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
        option41.add(this.addInputRestriction("裙楼 + 负一层开启度：     ", null, EnergyControlConstant.tubeWell41, e.ReadFloat("DB1.1194").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option41.add(this.addDisInputRestriction("裙楼负一层回水温度：   ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option41.add(this.addInputRestriction("裙楼负一层回水开启度：   ", null, EnergyControlConstant.tubeWell42, e.ReadFloat("DB1.1312").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> option42 = new ArrayList<>();
        option42.add(this.addDisInputRestriction("塔楼盘管回水 温度：    ", "",e.ReadFloat("DB1.1340").Content.toString(), "℃"));
        option42.add(this.addInputRestriction("塔楼盘管回水开启度：      ", null, EnergyControlConstant.tubeWell43, e.ReadFloat("DB1.1428").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options3.add(option41);
        options3.add(option42);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30085, e -> {
        List<Map<String, Object>> option51 = new ArrayList<>();
        option51.add(this.addDisInputRestriction("裙楼负一层回水温度：  ", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
        option51.add(this.addInputRestriction("裙楼负一层回水开启度：    ", null, EnergyControlConstant.tubeWell51, e.ReadFloat("DB1.1194").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        option51.add(this.addDisInputRestriction("裙楼 + 负一层温度：   ", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
        option51.add(this.addInputRestriction("裙楼 + 负一层开启度：       ", null, EnergyControlConstant.tubeWell52, e.ReadFloat("DB1.1312").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        List<Map<String, Object>> option52 = new ArrayList<>();
        option52.add(this.addDisInputRestriction("塔楼盘管 回水 温度：", "",e.ReadFloat("DB1.1340").Content.toString(), "℃"));
        option52.add(this.addInputRestriction("塔楼盘管回水开启度：       ", null, EnergyControlConstant.tubeWell53, e.ReadFloat("DB1.1428").Content.toString(),
                "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
        options4.add(option51);
        options4.add(option52);
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30086, e -> {
            List<Map<String, Object>> option61 = new ArrayList<>();
            option61.add(this.addDisInputRestriction("裙楼 + 负一层 温度：", "",e.ReadFloat("DB1.1106").Content.toString(), "℃"));
                option61.add(this.addInputRestriction("裙楼 + 负一层开启度：      ", null, EnergyControlConstant.tubeWell61, e.ReadFloat("DB1.1194").Content.toString(),
                    "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            option61.add(this.addDisInputRestriction("裙楼负一层回水温度：", "",e.ReadFloat("DB1.1222").Content.toString(), "℃"));
            option61.add(this.addInputRestriction("裙楼负一层回水 开启度：      ", null, EnergyControlConstant.tubeWell62, e.ReadFloat("DB1.1312").Content.toString(),
                    "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            List<Map<String, Object>> option62 = new ArrayList<>();
            option62.add(this.addDisInputRestriction("塔楼盘管回水  温度：  ", "",e.ReadFloat("DB1.1340").Content.toString(), "℃"));
            option62.add(this.addInputRestriction("塔楼盘管回水开启度：      ", null, EnergyControlConstant.tubeWell63, e.ReadFloat("DB1.1428").Content.toString(),
                    "%", "^(\\-|\\+)?\\d+(\\.\\d+)?$", "请输入数字！"));
            options5.add(option61);
            options5.add(option62);
        }).handle();
        restrictionList.add(restrictionMap);
        restrictionList.add(restrictionMap1);
        restrictionList.add(restrictionMap2);
        restrictionList.add(restrictionMap3);
        restrictionList.add(restrictionMap4);
        restrictionList.add(restrictionMap5);
        return restrictionList;
    }

    /**
     * @param controlMap
     * @throws Exception
     */
    public void setParameterInfo(Map controlMap) throws Exception {
        new SiemensS7NetHandler("220.194.141.5", 10080, e -> {
            e.Write("DB1.74", Short.parseShort((String) controlMap.get(EnergyControlConstant.timePumpAuto)));
            e.Write("DB1.184", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateUpLimit)));
            e.Write("DB1.188", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateDownLimit)));
            e.Write("DB1.192", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1AddRate)));
            e.Write("DB1.196", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1SubRate)));
            e.Write("DB1.176", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1Pressure)));
            e.Write("DB1.180", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1PressureZone)));
            e.Write("DB1.8", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateAdd)));
            e.Write("DB1.72", Short.parseShort((String) controlMap.get(EnergyControlConstant.w1AddCycle)));
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            e.Write("DB1.118", Short.parseShort((String) controlMap.get(EnergyControlConstant.timePumpAuto)));
            e.Write("DB1.48", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateUpLimit)));
            e.Write("DB1.52", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateDownLimit)));
            e.Write("DB1.56", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1AddRate)));
            e.Write("DB1.60", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1SubRate)));
            e.Write("DB1.100", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1Pressure)));
            e.Write("DB1.104", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1PressureZone)));
            e.Write("DB1.64", Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1RateAdd)));
            e.Write("DB1.116", Short.parseShort((String) controlMap.get(EnergyControlConstant.w1AddCycle)));
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            e.Write("DB1.68", Float.parseFloat((String) controlMap.get(EnergyControlConstant.waterBoxUp)));
            e.Write("DB1.76", Float.parseFloat((String) controlMap.get(EnergyControlConstant.waterBoxDown)));
            e.Write("DB1.84", Float.parseFloat((String) controlMap.get(EnergyControlConstant.pWaterBoxUp)));
            e.Write("DB1.88", Float.parseFloat((String) controlMap.get(EnergyControlConstant.pWaterBoxDown)));
        }).handle();
    }

    /**
     * @param controlMap
     * @throws Exception
     */
    public void setTubeWellControl(Map controlMap) throws Exception {
        new SiemensS7NetHandler("220.194.141.5", 30081, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell11)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell12)));
            e.Write("DB1.1428", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell13)));
            e.Write("DB1.1544", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell14)));
            e.Write("DB1.1660", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell15)));
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30082, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell21)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell22)));
            e.Write("DB1.1428", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell23)));
            e.Write("DB1.1544", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell24)));
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30083, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell31)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell32)));
        }).handle();

        new SiemensS7NetHandler("220.194.141.5", 30084, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell41)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell42)));
            e.Write("DB1.1428", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell43)));
        }).handle();

        new SiemensS7NetHandler("220.194.141.5", 30085, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell51)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell52)));
            e.Write("DB1.1428", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell53)));
        }).handle();

        new SiemensS7NetHandler("220.194.141.5", 30086, e -> {
            e.Write("DB1.1194", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell61)));
            e.Write("DB1.1312", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell62)));
            e.Write("DB1.1428", Float.parseFloat((String) controlMap.get(EnergyControlConstant.tubeWell63)));
        }).handle();
    }

    /**
     * 设置控制设备
     *
     * @param controlMap
     * @throws Exception
     */
    public void setControlDevice(Map controlMap) throws Exception {
        setBoilerDevice(controlMap);
    }

    private void setBoilerDevice(Map controlMap) throws Exception {
        boolean controllerFlag = StringUtils.isBlank((String) controlMap.get(EnergyControlConstant.linkController));
        byte boiler1 = 1;//1#锅炉开启
        byte boiler2 = 2;//2#锅炉开启
        byte boiler3 = 4;//3#锅炉开启
        byte boiler4 = 8;//4#锅炉开启
        byte boiler5 = 16;//5#锅炉开启
        byte boiler6 = 32;//6#锅炉开启
        byte boiler7 = 64;//7#锅炉开启
        byte pump1 = (byte) (controllerFlag ? 16 : -128);//1#泵开启
        byte pump2 = (byte) (controllerFlag ? 32 : 1);//2#泵开启
        byte pump3 = (byte) (controllerFlag ? 64 : 2);//3#泵开启
        byte pump4 = (byte) (controllerFlag ? -128 : 4);//4#泵开启
        byte valve1open = 1;//阀门1开启
        byte valve1down = 2;//阀门1关闭
        byte valve2open = 4;//阀门2开启
        byte valve2down = 8;//阀门2关闭
        byte valve3open = 16;//阀门3开启
        byte valve3down = 32;//阀门3关闭
        byte valve4open = 64;//阀门4开启
        byte valve4down = -128;//阀门4关闭
        byte valve5open = 1;//阀门5开启
        byte valve5down = 2;//阀门5关闭
        byte valve6open = 4;//阀门6开启
        byte valve6down = 8;//阀门6关闭
        byte valve7open = 16;//阀门7开启
        byte valve7down = 32;//阀门7关闭
        AtomicReference<Byte> sumValve1 = new AtomicReference<>((byte) 0);//1-4阀门开启情况
        AtomicReference<Byte> sumValve2 = new AtomicReference<>((byte) 0);//5-7阀门开启情况
        AtomicReference<Byte> sumPump = new AtomicReference<>((byte) 0);//全部一网泵开启情况
        AtomicReference<Byte> sumBoiler = new AtomicReference<>((byte) 0);//全部炉开启情况
        AtomicReference<Byte> autoM1 = new AtomicReference<>((byte) 0);//全部炉开启情况
        new SiemensS7NetHandler("220.194.141.5", 10080, e -> {
            sumBoiler.set(e.ReadByte("M11").Content);
            autoM1.set(e.ReadByte("M1").Content);
            String autoM1s = CommonUtils.getBinaryStrFromByte(autoM1.get());
            String boiler = CommonUtils.getBinaryStrFromByte(sumBoiler.get());
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler1)) && !"1".equals(boiler.substring(0, 1))) {//开启1#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler1));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler1)) && "1".equals(boiler.substring(0, 1))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler1));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler2)) && !"1".equals(boiler.substring(1, 2))) {//开启2#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler2));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler2)) && "1".equals(boiler.substring(1, 2))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler2));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler3)) && !"1".equals(boiler.substring(2, 3))) {//开启3#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler3));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler3)) && "1".equals(boiler.substring(2, 3))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler3));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler4)) && !"1".equals(boiler.substring(3, 4))) {//开启4#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler4));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler4)) && "1".equals(boiler.substring(3, 4))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler4));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler5)) && !"1".equals(boiler.substring(4, 5))) {//开启5#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler5));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler5)) && "1".equals(boiler.substring(4, 5))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler5));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler6)) && !"1".equals(boiler.substring(5, 6))) {//开启6#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler6));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler6)) && "1".equals(boiler.substring(5, 6))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler6));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.boiler7)) && !"1".equals(boiler.substring(6, 7))) {//开启7#锅炉
                sumBoiler.set((byte) (sumBoiler.get() + boiler7));
            } else if (!"1".equals(controlMap.get(EnergyControlConstant.boiler7)) && "1".equals(boiler.substring(6, 7))) {
                sumBoiler.set((byte) (sumBoiler.get() - boiler7));
            }
            // 一网水泵开关
            if (!controllerFlag) {
                sumPump.set((byte) 8);
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w11pump))) {
                if (!controllerFlag && !"1".equals(autoM1s.substring(7, 8))) {
                    autoM1.set((byte) (autoM1.get() + pump1));
                } else {
                    sumPump.set((byte) (sumPump.get() + pump1));
                }
            } else {
                if (!controllerFlag && "1".equals(autoM1s.substring(7, 8))) {
                    autoM1.set((byte) (autoM1.get() - pump1));
                }
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w12pump))) {
                sumPump.set((byte) (sumPump.get() + pump2));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w13pump))) {
                sumPump.set((byte) (sumPump.get() + pump3));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w14pump))) {
                sumPump.set((byte) (sumPump.get() + pump4));
            }
            if (!controllerFlag) {
                if (!"1".equals(autoM1s.substring(0, 1))) {
                    autoM1.set((byte) (autoM1.get() + 1));
                }
            } else {
                // 锅炉阀门设定
                if ("1".equals(autoM1s.substring(0, 1))) {
                    autoM1.set((byte) (autoM1.get() - 1));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler1valve))) {
                    sumValve1.set((byte) (sumValve1.get() + valve1open));
                } else {
                    sumValve1.set((byte) (sumValve1.get() + valve1down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler2valve))) {
                    sumValve1.set((byte) (sumValve1.get() + valve2open));
                } else {
                    sumValve1.set((byte) (sumValve1.get() + valve2down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler3valve))) {
                    sumValve1.set((byte) (sumValve1.get() + valve3open));
                } else {
                    sumValve1.set((byte) (sumValve1.get() + valve3down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler4valve))) {
                    sumValve1.set((byte) (sumValve1.get() + valve4open));
                } else {
                    sumValve1.set((byte) (sumValve1.get() + valve4down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler5valve))) {
                    sumValve2.set((byte) (sumValve2.get() + valve5open));
                } else {
                    sumValve2.set((byte) (sumValve2.get() + valve5down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler6valve))) {
                    sumValve2.set((byte) (sumValve2.get() + valve6open));
                } else {
                    sumValve2.set((byte) (sumValve2.get() + valve6down));
                }
                if ("1".equals(controlMap.get(EnergyControlConstant.boiler7valve))) {
                    sumValve2.set((byte) (sumValve2.get() + valve7open));
                } else {
                    sumValve2.set((byte) (sumValve2.get() + valve7down));
                }
            }
            //切泵时间间隔
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.timePumpAuto))) {
                e.Write("DB1.74", Short.parseShort((String) controlMap.get(EnergyControlConstant.timePumpAuto)));
            }
            //压差
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.pressureAuto))) {
                e.Write("DB1.176", Float.parseFloat((String) controlMap.get(EnergyControlConstant.pressureAuto)));
            }
            // 锅炉出水温度
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler1tem))) {
                e.Write("DB1.508", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler1tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler2tem))) {
                e.Write("DB1.532", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler2tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler3tem))) {
                e.Write("DB1.556", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler3tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler4tem))) {
                e.Write("DB1.580", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler4tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler5tem))) {
                e.Write("DB1.604", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler5tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler6tem))) {
                e.Write("DB1.628", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler6tem)));
            }
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.boiler7tem))) {
                e.Write("DB1.652", this.converStringToByte((String) controlMap.get(EnergyControlConstant.boiler7tem)));
            }
            // 一网泵 给定频率e
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.w1pumprate))) {
                float rate = Float.parseFloat((String) controlMap.get(EnergyControlConstant.w1pumprate));
                e.Write("DB1.172", rate);
            }
            //---------------------------------------------------------
            e.Write("M14", sumValve1.get());//操作1-4阀门指令所在位置-M14
            e.Write("M15", sumValve2.get());//操作5-7阀门指令所在位置-M15
            e.Write("M2", sumPump.get());//操作泵指令所在位置-M2
            e.Write("M3", sumBoiler.get());//操作泵指令所在位置-M11
            e.Write("M1", autoM1.get());
        }).handle();
        byte pump21 = 1;//1#泵开启
        byte pump22 = 2;//2#泵开启
        byte pump23 = 4;//3#泵开启
        byte pump24 = 8;//4#泵开启
        AtomicReference<Byte> sumPump2 = new AtomicReference<>((byte) 0);//全部泵开启情况
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            // 二网四个泵
            if ("1".equals(controlMap.get(EnergyControlConstant.w21pump))) {//1#泵选择
                sumPump2.set((byte) (sumPump2.get() + pump21));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w22pump))) {//1#泵选择
                sumPump2.set((byte) (sumPump2.get() + pump22));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w23pump))) {//1#泵选择
                sumPump2.set((byte) (sumPump2.get() + pump23));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.w24pump))) {//1#泵选择
                sumPump2.set((byte) (sumPump2.get() + pump24));
            }
            // 二网泵 给定频率e
            if (StringUtils.isNotBlank((String) controlMap.get(EnergyControlConstant.w2pumprate))) {
                double rate = Double.parseDouble((String) controlMap.get(EnergyControlConstant.w2pumprate));
                e.Write("DB1.680", rate);
                e.Write("DB1.684", rate);
                e.Write("DB1.688", rate);
                e.Write("DB1.692", rate);
            }
            //---------------------------------------------------------
            e.Write("M1", sumPump2.get());//操作泵指令所在位置-M1
        }).handle();
        byte pump5 = 1;//5#泵开启
        byte pump6 = 2;//6#泵开启
        AtomicReference<Byte> sumPump3 = new AtomicReference<>((byte) 0);//全部泵开启情况
        new SiemensS7NetHandler("220.194.141.5", 30080, e -> {
            // 二网四个泵
            if ("1".equals(controlMap.get(EnergyControlConstant.dn5pump))) {//1#泵选择
                sumPump3.set((byte) (sumPump3.get() + pump5));
            }
            if ("1".equals(controlMap.get(EnergyControlConstant.dn6pump))) {//1#泵选择
                sumPump3.set((byte) (sumPump3.get() + pump6));
            }
            e.Write("M1", sumPump3.get());//操作泵指令所在位置-M1
        }).handle();
    }

    @Override
    public void sendWarning() throws IOException {

    }
}
