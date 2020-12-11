package com.whaty.products.service.vehicle.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.vehicle.Motorcade;
import com.whaty.domain.bean.vehicle.VehicleApplication;
import com.whaty.domain.bean.vehicle.VehicleArrangement;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.whaty.constant.EnumConstConstants.*;

/**
 * 用车申请管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("vehicleApplicationManageService")
public class VehicleApplicationManageServiceImpl extends TycjGridServiceAdapter<VehicleApplication> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    GeneralDao generalDao;

    /**
     * 对bean进行添加校验
     * @param bean
     * @throws EntityException
     */
    void checkBeanBeforeAdd(VehicleApplication bean) throws EntityException {
        //参数校验

        //单位验证 没填写单位与申请人 默认为当前用户 手机号没填写默认是当前用户的手机号
        if (null == bean.getApplicantUnit() || StringUtils.isBlank(bean.getApplicantUnit().getId())) {
            bean.setApplicantUnit(Optional.ofNullable(UserUtils.getCurrentUnit())
                    .filter(e -> StringUtils.isNotBlank(e.getId()))
                    .orElseThrow(() -> new EntityException("当前用户无单位，不可进行用车申请")));
        }
        if (null == bean.getApplicantUser() || StringUtils.isBlank(bean.getApplicantUser().getId())) {
            bean.setApplicantUser(UserUtils.getCurrentUser());
        }
        if (StringUtils.isBlank(bean.getApplicantUserTel())) {
            String mobile = this.generalDao.getOneBySQL("select mobile as mobile from pe_manager " +
                    " where fk_sso_user_id = ? ", bean.getApplicantUser().getId());
            if (StringUtils.isBlank(mobile)) {
                throw new EntityException("当前用户无手机号，不可进行用车申请");
            }
            bean.setApplicantUserTel(mobile);
        }
    }

    @Override
    public void checkBeforeAdd(VehicleApplication bean) throws EntityException {
        //校验
        this.checkBeanBeforeAdd(bean);
        bean.setApplicantTime(new Date());
        //设置为申请状态为申请中 申请类型为班级 常规使用
        bean.setEnumConstByFlagVehicleApplyStatus(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_VEHICLE_APPLY_STATUS, "0"));
        bean.setEnumConstByFlagApplicantUnitType(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_APPLICANT_UNIT_TYPE, "1"));
        bean.setEnumConstByFlagVehicleApplyType(this.generalDao
                .getEnumConstByNamespaceCode(ENUM_CONST_NAMESPACE_FLAG_VEHICLE_APPLY_TYPE, "0"));
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        //已经安排及关联了开班信息的不能删除

        if (this.generalDao.checkNotEmpty(" SELECT 1 FROM vehicle_arrangement WHERE " +
                CommonUtils.madeSqlIn(idList, "fk_vehicle_application_id"))) {
            throw new ServiceException("存在已安排的，不可删除");
        }

        if (this.generalDao.checkNotEmpty("SELECT 1 FROM vehicle_application va " +
                "INNER JOIN enum_const ec ON ec.ID = va.flag_applicant_unit_type AND ec.code = '1' " +
                "INNER JOIN pe_class pc ON pc.id = va.applicant_unit_id " +
                "WHERE " + CommonUtils.madeSqlIn(idList, "va.id"))) {
            throw new ServiceException("存在已关联的班级，不可删除");
        }
    }

    /***
     * 结账
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void doSettleAccounts(String ids) throws ServiceException {
        TycjParameterAssert.isAllNotBlank(ids);

        // 权限验证 谁可以结账

        if (this.generalDao.checkNotEmpty(" SELECT 1 FROM vehicle_application va " +
                " INNER JOIN enum_const ec ON ec.ID =  va.flag_vehicle_apply_status AND ec.code = '1' " +
                " WHERE " + CommonUtils.madeSqlIn(ids, "va.id"))) {
            throw new ServiceException("存在已结账的数据，不可重复结账");
        }

        // 更新申请状态位为已结账 并更新结账时间
        this.generalDao.executeBySQL("UPDATE vehicle_application va SET va.checkout_date = NOW(), " +
                " va.flag_vehicle_apply_status = " +
                " (SELECT id FROM enum_const ec WHERE ec.NAMESPACE='flagVehicleApplyStatus' AND ec.code = '1') " +
                " WHERE " + CommonUtils.madeSqlIn(ids, "va.id"));
    }

    /**
     * 使用登记
     *
     * @param ids
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void doUseRegistration(String ids, Map<String, Object> params) throws ServiceException {
        TycjParameterAssert.isAllNotBlank(ids);
        TycjParameterAssert.isAllNotEmpty(params);
        // 未安排车辆的申请不能进行登记
        VehicleArrangement vehicleArrangement = this.generalDao.getOneByHQL(
                "from VehicleArrangement va WHERE va.vehicleApplication.id = ? ", ids);
        if (null == vehicleArrangement) {
            throw new ServiceException("该申请未安排车辆，不能进行使用登记");
        }
        //起始公里数、返回公里数、使用公里数、返回时间、用车人
        String endUser = (String) params.getOrDefault("endUser", null);
        int startKilometers = Integer.parseInt((String) params.getOrDefault("startKilometers", 0));
        int returnKilometers = Integer.parseInt((String) params.getOrDefault("returnKilometers", 0));
        Date returnDate = null;
        try {
            returnDate = com.whaty.util.CommonUtils.stringToUTCDate((String) params.getOrDefault("returnDate", null));
        } catch (ParseException e) {
            throw new ParameterIllegalException();
        }

        //验证数据
        TycjParameterAssert.isAllNotBlank(endUser);
        if (startKilometers <= 0 || returnKilometers <= 0) {
            throw new ParameterIllegalException();
        }
        TycjParameterAssert.isAllNotNull(returnDate);

        //保存数据
        vehicleArrangement.setEndUser(endUser);
        vehicleArrangement.setReturnDate(returnDate);
        vehicleArrangement.setStartKilometers(startKilometers);
        vehicleArrangement.setReturnKilometers(returnKilometers);
        this.generalDao.save(vehicleArrangement);
    }

    /**
     * 车辆安排
     *
     * @param ids
     * @param params
     * @throws ServiceException
     */
    @Transactional(rollbackFor = Exception.class)
    public void doVehicleArrange(String ids, Map<String, Object> params) throws ServiceException {
        TycjParameterAssert.isAllNotBlank(ids);
        TycjParameterAssert.isAllNotEmpty(params);
        VehicleApplication vehicleApplication = this.generalDao.getById(VehicleApplication.class, ids);
        TycjParameterAssert.isAllNotNull(vehicleApplication);

        // 验证是否已经被安排过
        VehicleArrangement vehicleArrangement = this.generalDao.getOneByHQL(
                "from VehicleArrangement va WHERE va.vehicleApplication.id = ? ", ids);
        if (null != vehicleArrangement) {
            throw new ServiceException("该车辆已被安排，不能重复安排");
        }

        // 参数验证 费用不需要校验
        String plateNum = (String) params.getOrDefault("plateNum", null);
        String motorcadeId = (String) params.getOrDefault("motorcadeId", null);
        String driverName = (String) params.getOrDefault("driverName", null);
        String driverTel = (String) params.getOrDefault("driverTel", null);
        String fareStr = (String) params.getOrDefault("fare", "0");
        BigDecimal fare = new BigDecimal(StringUtils.isNotBlank(fareStr) ? fareStr : "0");
        TycjParameterAssert.isAllNotNull(plateNum, motorcadeId, driverName, driverTel);

        // 获取车队
        Motorcade motorcade = this.generalDao.getById(Motorcade.class, motorcadeId);
        TycjParameterAssert.isAllNotNull(motorcade);

        vehicleArrangement = new VehicleArrangement();
        // “用车车型”为小型车，则，车号必须在“安排车队”相对应的“车辆管理”中选择
        if ("1".equals(vehicleApplication.getEnumConstByFlagVehicleType().getCode())) {
            if (!this.generalDao.checkNotEmpty("select 1 from vehicle " +
                    " where fk_motorcade_id = ? and plate_num = ? and is_delete = '0' ", motorcadeId, plateNum)) {
                throw new ServiceException("请在所选车队中选择车号");
            }
        }

        // 设置安排信息
        vehicleArrangement.setVehicleApplication(vehicleApplication);
        vehicleArrangement.setPlateNum(plateNum);
        vehicleArrangement.setDriverName(driverName);
        vehicleArrangement.setDriverTel(driverTel);
        vehicleArrangement.setUpdateDate(new Date());
        vehicleArrangement.setFare(fare);
        this.generalDao.save(vehicleArrangement);

        //更新安排人 安排时间 以及安排车队
        vehicleApplication.setArrangeDate(new Date());
        vehicleApplication.setArranger(UserUtils.getCurrentUser());
        vehicleApplication.setArrangeMotorcade(motorcade);
        this.generalDao.save(vehicleApplication);
    }

    /**
     * 获取联级车牌号
     *
     * @param params
     * @return
     */
    public List<Object[]> getCascadePlateNums(Map<String, Object> params) {
        TycjParameterAssert.isAllNotEmpty(params);
        // 获取联级参数
        Object dependColumnValues = params.get("dependColumnValues");
        String sqlFragment = "";
        if (dependColumnValues instanceof Map) {
            sqlFragment = (String) ((Map) dependColumnValues).get("motorcadeId");
        }
        String sql = "select plate_num as id, plate_num as name from vehicle where is_delete = '0' ";
        if (StringUtils.isNotBlank(sqlFragment)) {
            sql = sql + " and fk_motorcade_id = ? ";
            return this.generalDao.getBySQL(sql, sqlFragment);
        }
        return this.generalDao.getBySQL(sql);
    }
}