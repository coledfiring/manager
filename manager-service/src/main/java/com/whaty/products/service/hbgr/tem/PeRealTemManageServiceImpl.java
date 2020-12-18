package com.whaty.products.service.hbgr.tem;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.tem.PeRealTem;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: com.whaty.products.service.hbgr.tem
 * @author: weipengsen
 * @create: 2020-12-15
 **/
@Lazy
@Service("peRealTemManageService")
public class PeRealTemManageServiceImpl extends TycjGridServiceAdapter<PeRealTem> {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    public void getRealAndHisTem() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HMY);
        List<Map<String, Object>> temMapList = this.openGeneralDao.getMapBySQL("SELECT NODEID, DEVICE_ID, TEMP, HUMI," +
                " SIGNALSTRENGTH, UPLOADTIME, BATTERYLEVEL FROM v_valve_th_real_shanxihaobang");
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        temMapList.forEach(e -> {
            this.openGeneralDao.executeBySQL("INSERT INTO pe_real_tem (NODEID, DEVICE_ID, TEMP, HUMI," +
                    " SIGNALSTRENGTH, UPLOADTIME, BATTERYLEVEL) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE TEMP = ?, HUMI = ?, SIGNALSTRENGTH = ?, UPLOADTIME = ?," +
                    " BATTERYLEVEL = ?", e.get("NODEID"), e.get("DEVICE_ID"), e.get("TEMP"), e.get("HUMI"),
                    e.get("SIGNALSTRENGTH"), e.get("UPLOADTIME"), e.get("BATTERYLEVEL"), e.get("TEMP"),
                    e.get("HUMI"), e.get("SIGNALSTRENGTH"), e.get("UPLOADTIME"), e.get("BATTERYLEVEL"));
        });
            this.openGeneralDao.executeBySQL("INSERT INTO `pe_his_tem`(`DEVICE_ID`, `TEMP`, `HUMI`," +
                    " `UPLOADTIME`, `flag_scene`, `address`) SELECT DEVICE_ID, TEMP, HUMI, UPLOADTIME," +
                    " flag_scene, address FROM pe_real_tem");
    }

    public void setAddress(String ids, String address, String flagScene) {
        this.openGeneralDao.executeBySQL("UPDATE pe_real_tem SET address = ?, flag_scene = ? WHERE id = ?",
                address, flagScene, ids);
    }
}
