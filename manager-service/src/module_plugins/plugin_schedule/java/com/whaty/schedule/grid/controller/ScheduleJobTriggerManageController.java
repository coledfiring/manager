package com.whaty.schedule.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.grid.service.impl.ScheduleJobTriggerManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务调度管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("scheduleJobTriggerManageController")
@RequestMapping("/superAdmin/schedule/scheduleJobTriggerManage")
public class ScheduleJobTriggerManageController extends GridBaseController<PeScheduleTrigger> {

    @Resource(name = "scheduleJobTriggerManageService")
    private ScheduleJobTriggerManageServiceImpl scheduleJobTriggerManageService;

    @Override
    public GridService<PeScheduleTrigger> getGridService() {
        return this.scheduleJobTriggerManageService;
    }
}
