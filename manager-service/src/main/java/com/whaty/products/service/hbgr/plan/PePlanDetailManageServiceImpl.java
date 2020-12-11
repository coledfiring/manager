package com.whaty.products.service.hbgr.plan;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.plan.PePlanDetail;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Lazy
@Service("pePlanDetailManageService")
public class PePlanDetailManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<PePlanDetail> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PePlanDetail bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        if (!listPlanTime(bean).stream().allMatch(e -> checkBeforAddAndUpdate(bean.getStartTime(), bean.getEndTime(),
                (String) e.get("startTime"), (String) e.get("endTime")))) {
            throw new ServiceException("时间范围不正确！");
        }
    }

    @Override
    public void checkBeforeUpdate(PePlanDetail bean) throws EntityException {
        if (!listPlanTime(bean).stream().allMatch(e -> checkBeforAddAndUpdate(bean.getStartTime(), bean.getEndTime(),
                (String) e.get("startTime"), (String) e.get("endTime")))) {
            throw new ServiceException("时间范围不正确！");
        }
    }

    private List<Map<String, Object>> listPlanTime(PePlanDetail bean) {
        return this.myGeneralDao.getMapBySQL("SELECT d.start_time AS startTime, d.end_time AS endTime " +
                " FROM pe_plan_detail d WHERE d.fk_plan_id = ?", bean.getPePlan().getId());
    }

    private Boolean checkBeforAddAndUpdate(String sStartDate, String sEndDate, String begin,
                                           String end) {
        boolean b = ((sStartDate.compareTo(end) >= 0) && (sStartDate.compareTo(sEndDate) < 0))
                || ((sStartDate.compareTo(sEndDate) < 0) && (sEndDate.compareTo(begin) <= 0));
        return b;
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "pePlan.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "pePlan.id";
    }
}
