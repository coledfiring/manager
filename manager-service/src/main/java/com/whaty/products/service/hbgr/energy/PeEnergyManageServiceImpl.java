package com.whaty.products.service.hbgr.energy;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.energy.PeEnergyDay;
import com.whaty.domain.bean.hbgr.energy.PeEnergyTotal;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Lazy
@Service("peEnergyManageServiceImpl")
public class PeEnergyManageServiceImpl extends TycjGridServiceAdapter<PeEnergyDay> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    public void getEnergyData() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        EnumConst flagScene = this.openGeneralDao.getEnumConstByNamespaceCode("flagScene", "1");
        String totalId = (String) this.openGeneralDao.getBySQL("select id from pe_energy_total " +
                        "WHERE flag_scene = ?",
                flagScene.getId()).get(0);
        PeEnergyTotal peEnergyTotal = this.openGeneralDao.getById(PeEnergyTotal.class, totalId);
        PeEnergyDay peEnergyDay = new PeEnergyDay();
        peEnergyTotal.setCreateTime(new Date());
        peEnergyDay.setCreateTime(new Date());
        peEnergyDay.setEnumConstByFlagScene(flagScene);
        try {
            siemens_net.setPort(20080);
            OperateResult connect=siemens_net.ConnectServer();
            if(!connect.IsSuccess){
                throw new ServiceException(connect.Message);
            }
            peEnergyTotal.setElectricity(Double.valueOf(siemens_net.ReadFloat("DB1.528").Content));
            peEnergyDay.setElectricity(Double.valueOf(siemens_net.ReadFloat("DB1.532").Content));
            siemens_net.ConnectClose();
        } catch (Exception e) {
            siemens_net.ConnectClose();
        }
        try {
            siemens_net.setPort(30080);
            OperateResult connect=siemens_net.ConnectServer();
            if(!connect.IsSuccess){
                throw new ServiceException(connect.Message);
            }
            peEnergyTotal.setWater(Double.valueOf(siemens_net.ReadFloat("DB1.532").Content));
            peEnergyDay.setDnHeat(Double.valueOf(siemens_net.ReadByte("DB1.556").Content));
            peEnergyDay.setKtHeat(Double.valueOf(siemens_net.ReadByte("DB1.548").Content));
            peEnergyDay.setWater(Double.valueOf(siemens_net.ReadFloat("DB1.564").Content));
            peEnergyTotal.setKtHeat(Double.valueOf(siemens_net.ReadFloat("DB1.500").Content));
            peEnergyTotal.setDnHeat(Double.valueOf(siemens_net.ReadFloat("DB1.516").Content));
            peEnergyTotal.setHeat(Double.valueOf(siemens_net.ReadFloat("DB1.500").Content)
                    + Double.valueOf(siemens_net.ReadFloat("DB1.516").Content));
            siemens_net.ConnectClose();
        } catch (Exception e) {
            siemens_net.ConnectClose();
        }
        this.openGeneralDao.save(peEnergyDay);
        this.openGeneralDao.save(peEnergyTotal);
    }
}
