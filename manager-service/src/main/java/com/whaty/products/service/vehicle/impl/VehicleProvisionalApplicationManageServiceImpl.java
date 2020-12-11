package com.whaty.products.service.vehicle.impl;

import com.whaty.core.commons.exception.EntityException;
import com.whaty.domain.bean.vehicle.VehicleApplication;
import com.whaty.domain.bean.vehicle.VehicleArrangement;
import com.whaty.framework.asserts.TycjParameterAssert;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.whaty.constant.EnumConstConstants.*;

/**
 * 临时用车申请管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("vehicleProvisionalApplicationManageService")
public class VehicleProvisionalApplicationManageServiceImpl extends VehicleApplicationManageServiceImpl {

    @Override
    public void checkBeforeAdd(VehicleApplication bean) throws EntityException {
        this.checkBeanBeforeAdd(bean);

        VehicleArrangement vehicleArrangement = bean.getVehicleArrangement();
        TycjParameterAssert.isAllNotNull(vehicleArrangement);

        Date date = new Date();
        bean.setApplicantTime(date);
        //设置为申请状态为申请中 申请类型为班级 临时使用
        bean.setEnumConstByFlagVehicleApplyStatus(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_VEHICLE_APPLY_STATUS, "0"));
        bean.setEnumConstByFlagApplicantUnitType(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_APPLICANT_UNIT_TYPE, "1"));
        bean.setEnumConstByFlagVehicleApplyType(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_VEHICLE_APPLY_TYPE, "1"));

        bean.setArrangeDate(date);
        vehicleArrangement.setUpdateDate(date);
    }


    @Override
    protected void afterAdd(VehicleApplication bean) throws EntityException {
        VehicleArrangement vehicleArrangement = bean.getVehicleArrangement();
        vehicleArrangement.setVehicleApplication(new VehicleApplication(bean.getId()));
        //保存安排
        this.generalDao.save(vehicleArrangement);
        super.afterAdd(bean);
    }

}