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
import com.whaty.domain.bean.PrintHeaderConfig;
import com.whaty.domain.bean.PrintSubTitle;
import com.whaty.domain.bean.hbgr.yysj.PeYysj;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.handler.SiemensS7NetHandler;
import com.whaty.products.service.common.UtilService;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * author weipengsen  Date 2020/6/17
 */
@Lazy
@Service("peYysjService")
public class PeYysjServiceImpl extends TycjGridServiceAdapter<PeYysj> {

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    public void downloadYysjZip(String ids, HttpServletResponse response) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<String> idList = Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN));
        List<File> files = new ArrayList<>();
        idList.stream().forEach(e-> {
            Map<String, Object> dateMap = this.myGeneralDao.getOneMapBySQL("SELECT date_format(yysj_date, '%Y') as y," +
                    " date_format(yysj_date, '%m') as m, date_format(yysj_date, '%d')" +
                    " as d, date_format(yysj_date, '%Y%m%d') as time FROM pe_yysj WHERE id = ?", e);
            String filePath = String.format(YysjConstants.YYSJ_EXCEL_PATH, dateMap.get("y").toString(),
                    dateMap.get("m").toString(), dateMap.get("d").toString(), dateMap.get("time") + ".xls");
            File target = new File(CommonUtils.mkDir(CommonUtils.getRealPath(filePath)));
            files.add(target);
            try (OutputStream out = new FileOutputStream(target)){
                this.utilService.generateExcelAndWrite(out, null,
                       YysjConstants.YYSJ_EXCEL_HEADER_NAME, dateMap.get("time") + "山西国际金融中心供热系统",
                        this.myGeneralDao
                                .getBySQL("SELECT yysj_date, w1_gy,w1_hy,w1_gw,w1_hw," +
                                        "w2_1_gy,w2_1_hy,w2_1_gw,w2_1_hw,w2_2_gy,w2_2_hy,w2_2_gw,w2_2_hw," +
                                        "qt_rl,dn_rl,sw_wd,ql_sw,tl_sw,bsx_yw,pzsx_yw " +
                                " FROM pe_yysj_detail WHERE fk_yysj_id = ?", e));
            } catch (Exception ex) {
                if (target.exists()) {
                    target.delete();
                }
                throw new UncheckException(ex);
            }
        });
        try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
            CommonUtils.packageToZip(files, out);
        } catch (Exception ex) {
            throw new ServiceException("导出错误!!");
        }
    }

    public void generateYysj() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        String sql = "INSERT INTO pe_yysj(id, yysj_date, flag_scene) VALUES ('"
                + CommonUtils.generateUUIDNoSign() + "', now(), '"
                + this.openGeneralDao.getEnumConstByNamespaceCode("flagScene", "1").getId() +"')";
        this.openGeneralDao.executeBySQL(sql);
    }

    /**
     * 打印运营数据详情
     * @param ids
     * @return
     */
    public List<PrintFormConfig> printYysjDetail(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                  ");
        sql.append(" 	yysj.id AS yysjId,                                                   ");
        sql.append(" 	yysj.yysj_date AS yDate,                                             ");
        sql.append(" 	e.NAME AS sceneName,                                                 ");
        sql.append(" 	DATE_FORMAT(detail.yysj_date,'%T') AS dDate,                                           ");
        sql.append(" 	detail.w1_gy AS w1Gy,                                                ");
        sql.append(" 	detail.w1_hy AS w1Hy,                                                ");
        sql.append(" 	detail.w1_gw AS w1Gw,                                                ");
        sql.append(" 	detail.w1_hw AS w1Hw,                                                ");
        sql.append(" 	detail.w2_1_gy AS w21Gy,                                             ");
        sql.append(" 	detail.w2_1_hy AS w21Hy,                                             ");
        sql.append(" 	detail.w2_1_gw AS w21Gw,                                             ");
        sql.append(" 	detail.w2_1_hw AS w21Hw,                                             ");
        sql.append(" 	detail.w2_2_gy AS w22Gy,                                             ");
        sql.append(" 	detail.w2_2_hy AS w22Hy,                                             ");
        sql.append(" 	detail.w2_2_gw AS w22Gw,                                             ");
        sql.append(" 	detail.w2_2_hw AS w22Hw,                                             ");
        sql.append(" 	detail.qt_rl AS qtRl,                                                ");
        sql.append(" 	detail.dn_rl AS dnRl,                                                ");
        sql.append(" 	detail.sw_wd AS swWd,                                                ");
        sql.append(" 	detail.ql_sw AS qlSw,                                                ");
        sql.append(" 	detail.tl_sw AS tlSw,                                                ");
        sql.append(" 	detail.bsx_yw AS bsxYw,                                              ");
        sql.append(" 	detail.pzsx_yw AS pzsxYw                                             ");
        sql.append(" FROM                                                                    ");
        sql.append(" 	pe_yysj yysj                                                         ");
        sql.append(" 	INNER JOIN pe_yysj_detail detail ON detail.fk_yysj_id = yysj.id      ");
        sql.append(" 	INNER JOIN enum_const e ON e.id = yysj.flag_scene                    ");
        sql.append("    WHERE " + CommonUtils.madeSqlIn(ids, "yysj.id") + "");
        sql.append(" 	ORDER BY dDate DESC                                                  ");
        List<Object[]> list = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("没有可打印数据，请检查设备是否连通");
        }
        // 单位标题，判断是否切换打印单位
        String id = null;

        List<Object[]> yysjInfo = null;
        // 打印配置对象
        PrintFormConfig config = null;
        // 得到学校名称
        List<PrintFormConfig> printFormConfigs = new ArrayList<>();
        for (Object[] obj : list) {
            // 判断单位是否切换
            if (!obj[0].equals(id) || yysjInfo.size() > 20) {
                if (CollectionUtils.isNotEmpty(yysjInfo)) {
                    config.setOriginContent(yysjInfo);
                }
                // 记录当前初始化的单位标志
                id = (String) obj[0];
                // 创建新的配置信息
                config = new PrintFormConfig();
                printFormConfigs.add(config);
                // 设置字体为小字体
                config.setFontSize(PrintFormConfig.SMALL_FONT_SIZE);
                // 设置栏数为两栏
                config.setColumnNum(PrintFormConfig.ONE_COLUMN_NUM);
                // 设置标题
                config.setTitle("供热运营系统数据报表");
                // 显示落款
                config.setInscribed("山西皓邦节能环保有限公司");
                // 设置表头
                List<PrintHeaderConfig> headers = new ArrayList<>();
                config.getPrintHeaderConfigs().add(headers);
                headers.add(new PrintHeaderConfig("时间", "4.7%"));
                headers.add(new PrintHeaderConfig("一网供压", "4.7%"));
                headers.add(new PrintHeaderConfig("一网回压", "4.7%"));
                headers.add(new PrintHeaderConfig("一网供温", "4.7%"));
                headers.add(new PrintHeaderConfig("一网回温", "4.7%"));
                headers.add(new PrintHeaderConfig("空调供压", "4.7%"));
                headers.add(new PrintHeaderConfig("空调回压", "4.7%"));
                headers.add(new PrintHeaderConfig("空调供温", "4.7%"));
                headers.add(new PrintHeaderConfig("空调回温", "4.7%"));
                headers.add(new PrintHeaderConfig("地暖供压", "4.7%"));
                headers.add(new PrintHeaderConfig("地暖回压", "4.7%"));
                headers.add(new PrintHeaderConfig("地暖供温", "4.7%"));
                headers.add(new PrintHeaderConfig("地暖回温", "4.7%"));
                headers.add(new PrintHeaderConfig("空调热表", "4.7%"));
                headers.add(new PrintHeaderConfig("地暖热表", "4.7%"));
                headers.add(new PrintHeaderConfig("室外温度", "4.7%"));
                headers.add(new PrintHeaderConfig("裙楼室温", "4.7%"));
                headers.add(new PrintHeaderConfig("塔楼室温", "4.7%"));
                headers.add(new PrintHeaderConfig("水箱液位", "4.7%"));
                headers.add(new PrintHeaderConfig("膨胀水箱", "4.7%"));
                // 设置副标题
                config.initSubTitle(1, 2);
                config.addSubTitle(new PrintSubTitle("时间", 1, 1, 1, String.valueOf(obj[1])));
                config.addSubTitle(new PrintSubTitle("现场", 1, 2, 1, String.valueOf(obj[2])));
                // 单位的数据信息
                yysjInfo = new ArrayList<>();
            }
            yysjInfo.add(Arrays.copyOfRange(obj, 3, obj.length));
        }
        if (CollectionUtils.isNotEmpty(yysjInfo)) {
            config.setOriginContent(yysjInfo);
        }
        return printFormConfigs;
    }

    public Map<String, Object> getFlowData() throws Exception {
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        Map<String, Object> map = new HashMap<>(16);
        DecimalFormat df   = new DecimalFormat("######0.000");
        try {
            siemens_net.setPort(10080);
            OperateResult connect=siemens_net.ConnectServer();
            if(!connect.IsSuccess){
                throw new ServiceException(connect.Message);
            }
            //-------------------------------------锅炉--------------------------------------
            map.put("gl1Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.200").Content * 0.001)));//1#炉进压（回水）
            map.put("gl1Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.204").Content * 0.001)));//1#炉出压（供水）
            map.put("gl2Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.208").Content * 0.001)));//2#炉进压（回水）
            map.put("gl2Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.212").Content * 0.001)));///2#炉出压（供水）
            map.put("gl3Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.216").Content * 0.001)));//3#炉进压（回水）
            map.put("gl3Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.220").Content * 0.001)));///3#炉出压（供水）
            map.put("gl4Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.224").Content * 0.001)));//4#炉进压（回水）
            map.put("gl4Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.228").Content*0.001)));//4#炉出压（供水）
            map.put("gl5Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.232").Content*0.001)));//5#炉进压（回水）
            map.put("gl5Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.236").Content*0.001)));//5#炉出压（供水）
            map.put("gl6Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.240").Content*0.001)));//6#炉进压（回水）
            map.put("gl6Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.244").Content*0.001)));//6#炉出压（供水）
            map.put("gl7Gy", df.format(Math.abs(siemens_net.ReadByte("DB1.248").Content*0.001)));//7#炉进压（回水）
            map.put("gl7Hy", df.format(Math.abs(siemens_net.ReadByte("DB1.252").Content*0.001)));//7#炉出压（供水）
            map.put("gl1Gw", siemens_net.ReadFloat("DB1.268").Content);//1#锅炉出（供）水温度
            map.put("gl1Hw", siemens_net.ReadFloat("DB1.264").Content);//1#锅炉进（回）水温度
            map.put("gl2Gw", siemens_net.ReadFloat("DB1.276").Content);//2#锅炉出（供）水温度
            map.put("gl2Hw", siemens_net.ReadFloat("DB1.272").Content);//2#锅炉进（回）水温度
            map.put("gl3Gw", siemens_net.ReadFloat("DB1.284").Content);//3#锅炉出（供）水温度
            map.put("gl3Hw", siemens_net.ReadFloat("DB1.280").Content);//3#锅炉进（回）水温度
            map.put("gl4Gw", siemens_net.ReadFloat("DB1.292").Content);//4#锅炉出（供）水温度
            map.put("gl4Hw", siemens_net.ReadFloat("DB1.288").Content);//4#锅炉进（回）水温度
            map.put("gl5Gw", siemens_net.ReadFloat("DB1.300").Content);//5#锅炉出（供）水温度
            map.put("gl5Hw", siemens_net.ReadFloat("DB1.296").Content);//5#锅炉进（回）水温度
            map.put("gl6Gw", siemens_net.ReadFloat("DB1.308").Content);//6#锅炉出（供）水温度
            map.put("gl6Hw", siemens_net.ReadFloat("DB1.304").Content);//6#锅炉进（回）水温度
            map.put("gl7Gw", siemens_net.ReadFloat("DB1.316").Content);//7#锅炉出（供）水温度
            map.put("gl7Hw", siemens_net.ReadFloat("DB1.312").Content);//7#锅炉进（回）水温度
            map.put("w1Gy",df.format(Math.abs(siemens_net.ReadByte("DB1.256").Content * 0.001)));// 一网供压
            map.put("w1Hy",df.format(Math.abs(siemens_net.ReadByte("DB1.260").Content * 0.001)));// 一网回压
            map.put("w1Gw",df.format(siemens_net.ReadFloat("DB1.320").Content));// 一网供温
            map.put("w1Hw",df.format(siemens_net.ReadFloat("DB1.324").Content));// 一网回温
            siemens_net.ConnectClose();
        } catch (Exception e) {
            siemens_net.ConnectClose();//关闭连接
        }
       new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
           map.put("w2ktGy", df.format(Math.abs(e.ReadFloat("DB1.600").Content)));
           map.put("w2ktHy", df.format(Math.abs(e.ReadFloat("DB1.604").Content)));
           map.put("w2ktGw", df.format(Math.abs(e.ReadFloat("DB1.608").Content)));
           map.put("w2ktHw", df.format(Math.abs(e.ReadFloat("DB1.612").Content)));
        }).handle();
        new SiemensS7NetHandler("220.194.141.5", 30080, e -> {
            map.put("w2dnGy", df.format(Math.abs(e.ReadFloat("DB1.600").Content)));
            map.put("w2dnHy", df.format(Math.abs(e.ReadFloat("DB1.604").Content)));
            map.put("w2dnGw", df.format(Math.abs(e.ReadFloat("DB1.608").Content)));
            map.put("w2dnHw", df.format(Math.abs(e.ReadFloat("DB1.612").Content)));
        }).handle();
        return map;
    }

}
