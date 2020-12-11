package com.whaty.products.service.hbgr.yysj;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.BoardDataModel;
import com.whaty.domain.bean.PrintFormConfig;
import com.whaty.domain.bean.hbgr.yysj.PeYysj;
import com.whaty.domain.bean.hbgr.yysj.PeYysjDetail;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.handler.SiemensS7NetHandler;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * author weipengsen  Date 2020/6/17
 */
@Lazy
@Service("peYysjDetailService")
public class PeYysjDetailServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeYysjDetail> {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    protected String getParentIdSearchParamName() {
        return "peYysj.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peYysj.id";
    }

    public void getYysjDetailJob() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        siemens_net.setPort(10080);
        OperateResult connect=siemens_net.ConnectServer();
        if(!connect.IsSuccess){
            siemens_net.ConnectClose();
            throw new ServiceException("smart S7协议连接失败");
        }
        PeYysjDetail peYysjDetail = new PeYysjDetail();
        peYysjDetail.setW1Gy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.256").Content), 2));
        peYysjDetail.setW1Hy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.260").Content), 2));
        peYysjDetail.setW1Gw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.320").Content, 2));
        peYysjDetail.setW1Hw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.324").Content, 2));
        siemens_net.ConnectClose();
        siemens_net.setPort(20080);
        siemens_net.ConnectServer();
        peYysjDetail.setW21Gy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.600").Content), 2));
        peYysjDetail.setW21Hy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.604").Content), 2));
        peYysjDetail.setW21Gw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.608").Content, 2));
        peYysjDetail.setW21Hw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.612").Content, 2));
        peYysjDetail.setSwWd(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.616").Content, 2));
        peYysjDetail.setBsxYw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.620").Content, 2));
        peYysjDetail.setPzsxYw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.624").Content, 2));
        siemens_net.ConnectClose();
        siemens_net.setPort(30080);
        siemens_net.ConnectServer();
        peYysjDetail.setW22Gy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.600").Content), 2));
        peYysjDetail.setW22Hy(CommonUtils.keepDigit(Math.abs(siemens_net.ReadFloat("DB1.604").Content), 2));
        peYysjDetail.setW22Gw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.608").Content, 2));
        peYysjDetail.setW22Hw(CommonUtils.keepDigit(siemens_net.ReadFloat("DB1.612").Content, 2));
        siemens_net.ConnectClose();
        PeYysj peYysj = new PeYysj();
        peYysj.setId((String) this.openGeneralDao.getBySQL("select id from pe_yysj " +
                "WHERE yysj_date >= DATE_FORMAT(NOW(),'%Y-%m-%d') AND flag_scene = ?",
                openGeneralDao.getEnumConstByNamespaceCode("flagScene", "1").getId()).get(0));
        peYysjDetail.setPeYysj(peYysj);
        peYysjDetail.setYysjDate(new Date());
        openGeneralDao.save(peYysjDetail);
    }

    /**
     *  数据看板矩形模块数据
     *
     * @return
     */
    public List<Map<String, Object>> getBoardYysj() throws Exception {
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        siemens_net.setPort(10080);
        OperateResult connect=siemens_net.ConnectServer();
        if(!connect.IsSuccess){
            siemens_net.ConnectClose();
            throw new ServiceException("smart S7协议连接失败");
        }
        DecimalFormat df   = new DecimalFormat("######0.000");
        Map<String, Object> dataModel = new HashMap<>();
        List<Map<String, Object>> listDataModel = new ArrayList<>();
        dataModel.put("icon", "board-data-blue");
        dataModel.put("dataType", "3");
        dataModel.put("listBoardDataModel", listDataModel);
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("title", "一网供压");
        dataMap.put("count", df.format(Math.abs(siemens_net.ReadByte("DB1.256").Content * 0.001)) + "Mpa");
        Map<String, Object> dataMap1 = new HashMap<>(2);
        dataMap1.put("title", "一网回压");
        dataMap1.put("count", df.format(Math.abs(siemens_net.ReadByte("DB1.260").Content * 0.001)) + "Mpa");
        Map<String, Object> dataMap2 = new HashMap<>(2);
        dataMap2.put("title", "一网供温");
        dataMap2.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.320").Content)) + "℃");
        Map<String, Object> dataMap3 = new HashMap<>(2);
        dataMap3.put("title", "一网回温");
        dataMap3.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.324").Content)) + "℃");
        listDataModel.add(dataMap);
        listDataModel.add(dataMap1);
        listDataModel.add(dataMap2);
        listDataModel.add(dataMap3);
        siemens_net.ConnectClose();
        siemens_net.setPort(20080);
        siemens_net.ConnectServer();
        Map<String, Object> dataModel1 = new HashMap<>();
        List<Map<String, Object>> listDataModel1 = new ArrayList<>();
        dataModel1.put("icon", "board-data-yellow");
        dataModel1.put("dataType", "3");
        dataModel1.put("listBoardDataModel", listDataModel1);
        Map<String, Object> dataMap4 = new HashMap<>(2);
        dataMap4.put("title", "二网空调供压");
        dataMap4.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.600").Content))  + "Mpa");
        Map<String, Object> dataMap5 = new HashMap<>(2);
        dataMap5.put("title", "二网空调回压");
        dataMap5.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.604").Content))  + "Mpa");
        Map<String, Object> dataMap6 = new HashMap<>(2);
        dataMap6.put("title", "二网空调供温");
        dataMap6.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.608").Content)));
        Map<String, Object> dataMap7 = new HashMap<>(2);
        dataMap7.put("title", "二网空调回温");
        dataMap7.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.612").Content)));
        listDataModel1.add(dataMap4);
        listDataModel1.add(dataMap5);
        listDataModel1.add(dataMap6);
        listDataModel1.add(dataMap7);
        Map<String, Object> dataMap12 = new HashMap<>(2);
        dataMap12.put("title", "水箱液位");
        dataMap12.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.620").Content)));
        Map<String, Object> dataMap13 = new HashMap<>(2);
        dataMap13.put("title", "膨胀水箱液位");
        dataMap13.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.624").Content)));
        Map<String, Object> dataMap14 = new HashMap<>(2);
        dataMap14.put("title", "空调补水侧压力");
        dataMap14.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.644").Content)));
        siemens_net.ConnectClose();
        siemens_net.setPort(30080);
        siemens_net.ConnectServer();
        Map<String, Object> dataModel2 = new HashMap<>();
        List<Map<String, Object>> listDataModel2 = new ArrayList<>();
        dataModel2.put("icon", "board-data-red");
        dataModel2.put("dataType", "3");
        dataModel2.put("listBoardDataModel", listDataModel2);
        Map<String, Object> dataMap8 = new HashMap<>(2);
        dataMap8.put("title", "二网地暖供压");
        dataMap8.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.600").Content)) + "Mpa");
        Map<String, Object> dataMap9 = new HashMap<>(2);
        dataMap9.put("title", "二网地暖回压");
        dataMap9.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.604").Content)) + "Mpa");
        Map<String, Object> dataMap10 = new HashMap<>(2);
        dataMap10.put("title", "二网地暖供温");
        dataMap10.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.608").Content)));
        Map<String, Object> dataMap11 = new HashMap<>(2);
        dataMap11.put("title", "二网地暖回温");
        dataMap11.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.612").Content)));
        Map<String, Object> dataMap15 = new HashMap<>(2);
        dataMap15.put("title", "地暖补水侧压力");
        dataMap15.put("count", df.format(Math.abs(siemens_net.ReadFloat("DB1.644").Content)));
        listDataModel2.add(dataMap8);
        listDataModel2.add(dataMap9);
        listDataModel2.add(dataMap10);
        listDataModel2.add(dataMap11);
        siemens_net.ConnectClose();
        Map<String, Object> dataModel3 = new HashMap<>();
        List<Map<String, Object>> listDataModel3 = new ArrayList<>();
        dataModel3.put("icon", "board-data-black");
        dataModel3.put("dataType", "3");
        dataModel3.put("listBoardDataModel", listDataModel3);
        listDataModel3.add(dataMap12);
        listDataModel3.add(dataMap13);
        listDataModel3.add(dataMap14);
        listDataModel3.add(dataMap15);
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> dataMap16 = new HashMap<>(2);
        Map<String, Object> dataMap17 = new HashMap<>(2);
        Map<String, Object> dataMap18 = new HashMap<>(2);
        Map<String, Object> dataMap19 = new HashMap<>(2);
        Map<String, Object> dataModel4 = new HashMap<>();
        List<Map<String, Object>> listDataModel4 = new ArrayList<>();
        dataModel4.put("icon", "board-data-black");
        dataModel4.put("dataType", "3");
        dataModel4.put("listBoardDataModel", listDataModel4);
        listDataModel4.add(dataMap16);
        listDataModel4.add(dataMap17);
        listDataModel4.add(dataMap18);
        listDataModel4.add(dataMap19);
        new SiemensS7NetHandler("220.194.141.5", 10080, e-> {
            dataMap16.put("title", "一网1#水泵频率");
            dataMap16.put("count", df.format(Math.abs(e.ReadFloat("DB1.336").Content)));
            dataMap17.put("title", "一网2#水泵频率");
            dataMap17.put("count", df.format(Math.abs(e.ReadFloat("DB1.340").Content)));
            dataMap18.put("title", "一网3#水泵频率");
            dataMap18.put("count", df.format(Math.abs(e.ReadFloat("DB1.344").Content)));
            dataMap19.put("title", "一网4#水泵频率");
            dataMap19.put("count", df.format(Math.abs(e.ReadFloat("DB1.348").Content)));
        }).handle();
        Map<String, Object> dataMap20 = new HashMap<>(2);
        Map<String, Object> dataMap21 = new HashMap<>(2);
        Map<String, Object> dataMap22 = new HashMap<>(2);
        Map<String, Object> dataMap23 = new HashMap<>(2);
        Map<String, Object> dataModel5 = new HashMap<>();
        List<Map<String, Object>> listDataModel5 = new ArrayList<>();
        dataModel5.put("icon", "board-data-black");
        dataModel5.put("dataType", "3");
        dataModel5.put("listBoardDataModel", listDataModel5);
        listDataModel5.add(dataMap20);
        listDataModel5.add(dataMap21);
        listDataModel5.add(dataMap22);
        listDataModel5.add(dataMap23);
        new SiemensS7NetHandler("220.194.141.5", 20080, e-> {
            dataMap20.put("title", "二网1#水泵频率");
            dataMap20.put("count", df.format(e.ReadFloat("DB1.628").Content));
            dataMap21.put("title", "二网2#水泵频率");
            dataMap21.put("count", df.format(e.ReadFloat("DB1.632").Content));
            dataMap22.put("title", "二网3#水泵频率");
            dataMap22.put("count", df.format(e.ReadFloat("DB1.636").Content));
            dataMap23.put("title", "二网4#水泵频率");
            dataMap23.put("count", df.format(e.ReadFloat("DB1.640").Content));
        }).handle();
        resultList.add(dataModel);
        resultList.add(dataModel1);
        resultList.add(dataModel2);
        resultList.add(dataModel3);
        resultList.add(dataModel4);
        resultList.add(dataModel5);
        return resultList;
    }
}
