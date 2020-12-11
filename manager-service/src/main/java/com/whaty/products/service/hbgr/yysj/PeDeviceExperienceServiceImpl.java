package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeDevice;
import com.whaty.domain.bean.hbgr.yysj.PeDeviceExperience;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@Service("peDeviceExperienceService")
public class PeDeviceExperienceServiceImpl extends TycjGridServiceAdapter<PeDeviceExperience> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 添加维修经验
     *
     * @param ids
     * @param experience
     */
    public void addExperience(String ids, String experience, String reason) {
        TycjParameterAssert.isAllNotBlank(ids, experience);
        PeDeviceExperience peDeviceExperience = new PeDeviceExperience();
        PeDevice peDeviceInfo = new PeDevice();
        peDeviceInfo.setId(ids);
        peDeviceExperience.setExperience(experience);
        peDeviceExperience.setReason(reason);
        peDeviceExperience.setPeDevice(peDeviceInfo);
        peDeviceExperience.setCreateTime(new Date());
        this.myGeneralDao.save(peDeviceExperience);
    }
}
