package com.whaty.products.service.vehicle.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.vehicle.Vehicle;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 车辆管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("vehicleManageService")
public class VehicleManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<Vehicle> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(Vehicle bean) throws EntityException {
        bean.setCreateDate(new Date());
        bean.setIsDelete("0");
        super.checkBeforeAdd(bean);
    }

    /***
     * 设置车辆状态
     * @param ids
     * @param flagVehicleStatus
     */
    public void updateVehicleStatus(String ids, String flagVehicleStatus) {
        TycjParameterAssert.isAllNotBlank(ids, flagVehicleStatus);
        this.generalDao.executeBySQL(" UPDATE vehicle SET update_date =NOW(),flag_vehicle_status = ?" +
                " WHERE " + CommonUtils.madeSqlIn(ids, "id"), flagVehicleStatus);

    }

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        // 删除车辆只更新标志位
        int n = this.generalDao.executeBySQL(" UPDATE vehicle SET update_date =NOW(),is_delete = '1' " +
                " WHERE " + CommonUtils.madeSqlIn(ids, "id"));
        return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap(String.format("删除成功，共删除%d条数据", n));
    }

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "motorcade.id";
    }
}