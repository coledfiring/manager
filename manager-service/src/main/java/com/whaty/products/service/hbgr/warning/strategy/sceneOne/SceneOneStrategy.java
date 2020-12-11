package com.whaty.products.service.hbgr.warning.strategy.sceneOne;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.warning.WarningCondition;
import com.whaty.domain.bean.hbgr.warning.WarningConstant;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.hbgr.warning.strategy.AbstractWarningStrategy;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第一现场策略
 *
 * @author weipengsen
 */
@Lazy
@Service("sceneOneStrategy")
public class SceneOneStrategy implements AbstractWarningStrategy {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public Map<String, Object> getRestriction(String linkId) {
        //规则设置页面布局map
        Map<String, Object> restrictionMap = new HashMap<>(4);
        List<Map<String, Object>> options = new ArrayList<>();
        restrictionMap.put("options", options);

        //获取规则内容
        Map<String, Object> configMap = this.openGeneralDao.getOneMapBySQL("SELECT id as id, name as name," +
                " config as config FROM pr_service_config WHERE link = ?", linkId);
        restrictionMap.put("restrictionName", configMap.get("name"));
        restrictionMap.put("restrictionId", configMap.get("id"));
        WarningCondition warningCondition;
        if (configMap.get("config") != null) {
            warningCondition = new WarningCondition((String) configMap.get("config"));
        } else {
            warningCondition = new WarningCondition();
        }
        //一网一号泵是否故障
        options.add(this.addTextRestriction("一网1#泵", WarningConstant.W1_B1_GZ,
                this.convertValueToString(warningCondition.getW1b1gz())));
        options.add(this.addTextRestriction("一网2#泵", WarningConstant.W1_B2_GZ,
                this.convertValueToString(warningCondition.getW1b2gz())));
        options.add(this.addTextRestriction("一网3#泵", WarningConstant.W1_B3_GZ,
                this.convertValueToString(warningCondition.getW1b3gz())));
        options.add(this.addTextRestriction("一网4#泵", WarningConstant.W1_B4_GZ,
                this.convertValueToString(warningCondition.getW1b4gz())));
        options.add(this.addTextRestriction("二网空调1#泵", WarningConstant.W2_B1_GZ,
                this.convertValueToString(warningCondition.getW2b1gz())));
        options.add(this.addTextRestriction("二网空调2#泵", WarningConstant.W2_B2_GZ,
                this.convertValueToString(warningCondition.getW2b2gz())));
        options.add(this.addTextRestriction("二网空调3#泵", WarningConstant.W2_B3_GZ,
                this.convertValueToString(warningCondition.getW2b3gz())));
        options.add(this.addTextRestriction("二网空调4#泵", WarningConstant.W2_B4_GZ,
                this.convertValueToString(warningCondition.getW2b4gz())));
        options.add(this.addTextRestriction("二网地暖5#泵", WarningConstant.W2_B5_GZ,
                this.convertValueToString(warningCondition.getW2b5gz())));
        options.add(this.addTextRestriction("二网地暖6#泵", WarningConstant.W2_B6_GZ,
                this.convertValueToString(warningCondition.getW2b6gz())));
        options.add(this.addTextRestriction("1#锅炉故障", WarningConstant.GL1_GZ,
                this.convertValueToString(warningCondition.getGl1gz())));
        options.add(this.addTextRestriction("2#锅炉故障", WarningConstant.GL2_GZ,
                this.convertValueToString(warningCondition.getGl2gz())));
        options.add(this.addTextRestriction("3#锅炉故障", WarningConstant.GL3_GZ,
                this.convertValueToString(warningCondition.getGl3gz())));
        options.add(this.addTextRestriction("4#锅炉故障", WarningConstant.GL4_GZ,
                this.convertValueToString(warningCondition.getGl4gz())));
        options.add(this.addTextRestriction("5#锅炉故障", WarningConstant.GL5_GZ,
                this.convertValueToString(warningCondition.getGl5gz())));
        options.add(this.addTextRestriction("6#锅炉故障", WarningConstant.GL6_GZ,
                this.convertValueToString(warningCondition.getGl6gz())));
        options.add(this.addTextRestriction("7#锅炉故障", WarningConstant.GL7_GZ,
                this.convertValueToString(warningCondition.getGl7gz())));
        options.add(this.addInputRestriction("1#炉内压强上限", ">", "yq", "1",
                "/Mpa", "", ""));
        return restrictionMap;
    }

    @Override
    public void sendWarning() throws IOException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                         ");
        sql.append(" 	config.CONFIG                                               ");
        sql.append(" FROM                                                           ");
        sql.append(" 	pe_warning w                                                ");
        sql.append(" 	INNER JOIN enum_const e ON e.ID = w.flag_scene              ");
        sql.append(" 	INNER JOIN enum_const e1 ON e1.id = w.flag_Isvalid          ");
        sql.append(" 	INNER JOIN pr_service_config config ON config.link = w.id   ");
        sql.append(" WHERE                                                          ");
        sql.append(" 	e.CODE = '1'                                                ");
        sql.append(" 	AND e1.CODE = '1'                                           ");
        List<String> configs = this.openGeneralDao.getBySQL(sql.toString());
        if(CollectionUtils.isEmpty(configs)) {
            throw new ServiceException("获取配置信息为空！");
        }
        WarningCondition warningCondition = null;
        if ( configs.get(0) != null) {
            warningCondition = new WarningCondition((String) configs.get(0));
        } else {
            warningCondition = new WarningCondition();
        }
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        siemens_net.setPort(10080);
        OperateResult connect=siemens_net.ConnectServer();
        if(connect.IsSuccess){
            System.out.println("connect success!");
        }else{
            System.out.println("failed:"+connect.Message);
        }
        byte glb = siemens_net.ReadByte("M17").Content;
        byte w1bb = siemens_net.ReadByte("I0").Content;
        String gls = CommonUtils.getBinaryStrFromByte(glb);
        String w1bs = CommonUtils.getBinaryStrFromByte(w1bb);
        if (warningCondition.getGl7gz() != null && warningCondition.getGl7gz() == 1 && gls.substring(6, 7).equals("1")) {
            sendDingTalk("7#锅炉运行故障");
        }

        if (warningCondition.getGl6gz() != null && warningCondition.getGl6gz() == 1 && gls.substring(5, 6).equals("1")) {
            sendDingTalk("6#锅炉运行故障");
        }

        if (warningCondition.getGl5gz() != null && warningCondition.getGl5gz() == 1 && gls.substring(4, 5).equals("1")) {
            sendDingTalk("5#锅炉运行故障");
        }

        if (warningCondition.getGl4gz() != null && warningCondition.getGl4gz() == 1 && gls.substring(3, 4).equals("1")) {
            sendDingTalk("4#锅炉运行故障");
        }

        if (warningCondition.getGl3gz() != null && warningCondition.getGl3gz() == 1 && gls.substring(2, 3).equals("1")) {
            sendDingTalk("3#锅炉运行故障");
        }

        if (warningCondition.getGl2gz() != null && warningCondition.getGl2gz() == 1 && gls.substring(1, 2).equals("1")) {
            sendDingTalk("2#锅炉运行故障");
        }

        if (warningCondition.getGl1gz() != null && warningCondition.getGl1gz() == 1 && gls.substring(0, 1).equals("1")) {
            sendDingTalk("1#锅炉运行故障");
        }

        if (warningCondition.getW1b1gz() != null && warningCondition.getW1b1gz() == 1 && w1bs.substring(1, 2).equals("1")) {
            sendDingTalk("一网1#泵故障");
        }

        if (warningCondition.getW1b2gz() != null && warningCondition.getW1b2gz() == 1 && w1bs.substring(3, 4).equals("1")) {
            sendDingTalk("一网2#泵故障");
        }

        if (warningCondition.getW1b3gz() != null && warningCondition.getW1b3gz() == 1 && w1bs.substring(5, 6).equals("1")) {
            sendDingTalk("一网3#泵故障");
        }

        if (warningCondition.getW1b4gz() != null && warningCondition.getW1b4gz() == 1 && w1bs.substring(7, 8).equals("1")) {
            sendDingTalk("一网4#泵故障");
        }
        siemens_net.ConnectClose();
        siemens_net.setPort(20080);
        String w2kts = CommonUtils.getBinaryStrFromByte(siemens_net.ReadByte("I0").Content);
        if (warningCondition.getW1b1gz() != null && warningCondition.getW1b1gz() == 1 && w2kts.substring(1, 2).equals("1")) {
            sendDingTalk("二网空调1#泵故障");
        }

        if (warningCondition.getW2b2gz() != null && warningCondition.getW2b2gz() == 1 && w2kts.substring(3, 4).equals("1")) {
            sendDingTalk("二网空调2#泵故障");
        }

        if (warningCondition.getW2b3gz() != null && warningCondition.getW2b3gz() == 1 && w2kts.substring(5, 6).equals("1")) {
            sendDingTalk("二网空调3#泵故障");
        }

        if (warningCondition.getW2b4gz() != null && warningCondition.getW2b4gz() == 1 && w2kts.substring(7, 8).equals("1")) {
            sendDingTalk("二网空调4#泵故障");
        }
        siemens_net.ConnectClose();
        siemens_net.setPort(30080);
        String w2dns = CommonUtils.getBinaryStrFromByte(siemens_net.ReadByte("I0").Content);
        if (warningCondition.getW2b5gz() != null && warningCondition.getW2b5gz() == 1 && w2dns.substring(1, 2).equals("1")) {
            sendDingTalk("二网空调1#泵故障");
        }

        if (warningCondition.getW2b6gz() != null && warningCondition.getW2b6gz() == 1 && w2dns.substring(3, 4).equals("1")) {
            sendDingTalk("二网空调2#泵故障");
        }
    }
}
