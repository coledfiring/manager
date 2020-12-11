package com.whaty.schedule.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.grid.constants.ScheduleConstants;
import com.whaty.schedule.job.JobStatusBus;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.schedule.util.QuartzUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 调度任务管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("scheduleJobManageService")
public class ScheduleJobManageServiceImpl extends TycjGridServiceAdapter<PeScheduleJob> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    private final JobStatusBus jobStatusBus = new JobStatusBus();

    @Override
    public void checkBeforeAdd(PeScheduleJob bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setInputDate(new Date());
        bean.setVersion(1L);
        bean.setEnumConstByFlagIsShow(this.myGeneralDao
                .getEnumConstByNamespaceCode(ScheduleConstants.ENUM_CONST_NAMESPACE_IS_SHOW, "1"));
    }

    @Override
    public void checkBeforeUpdate(PeScheduleJob bean) throws EntityException {
        bean.setEnumConstByFlagJobValid(this.myGeneralDao
                .getById(EnumConst.class, bean.getEnumConstByFlagJobValid().getId()));
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateDate(new Date());
    }

    @Override
    protected void afterUpdate(PeScheduleJob bean) throws EntityException {
        bean.setEnumConstByFlagJobValid(this.myGeneralDao.getById(EnumConst.class,
                bean.getEnumConstByFlagJobValid().getId()));
        try {
            // 更新任务版本
            bean.addJobVersion();
            jobStatusBus.setUpdateStatus();
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from pe_schedule_job job inner join enum_const ac on ac.id = job.flag_job_valid" +
                        " where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "job.id"));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("不能删除无效的数据");
        }
    }

    /**
     * 添加或更新前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(PeScheduleJob bean) throws EntityException {
        this.checkJobValid(bean);
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from pe_schedule_job where job_group = ? AND job_name = ?" + additionalSql,
                        bean.getGroup(), bean.getName());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同组和名称的任务");
        }
    }

    /**
     * 检查bean是否有效
     * @param bean
     * @throws EntityException
     */
    private void checkJobValid(PeScheduleJob bean) throws EntityException {
        Class clazz = null;
        if (StringUtils.isNotBlank(bean.getBeanClass())) {
            try {
                clazz = Class.forName(bean.getBeanClass());
            } catch (ClassNotFoundException e) {
                throw new EntityException(String.format("class '%s' 不存在", bean.getBeanClass()));
            }
        }
        if (StringUtils.isNotBlank(bean.getSpringId())) {
            Object springBean = SpringUtil.getBean(bean.getSpringId());
            if (springBean == null) {
                throw new EntityException(String.format("IOC bean '%s' 不存在", bean.getSpringId()));
            }
            clazz = springBean.getClass();
        }
        if (clazz == null) {
            throw new EntityException("IOC容器和class方式至少要选择一种");
        }
        if (QuartzUtils.getValidMethod(clazz, bean.getMethodName()) == null) {
            throw new EntityException(String.format("方法%s不存在或者参数列表不为空或Map", bean.getMethodName()));
        }
    }
}
