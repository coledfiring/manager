package com.whaty.schedule.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.grid.service.impl.ScheduleJobManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 调度任务管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("scheduleJobManageController")
@RequestMapping("/superAdmin/schedule/scheduleJobManage")
public class ScheduleJobManageController extends GridBaseController<PeScheduleJob> {

    @Resource(name = "scheduleJobManageService")
    private ScheduleJobManageServiceImpl scheduleJobManageService;

    @Override
    public GridService<PeScheduleJob> getGridService() {
        return this.scheduleJobManageService;
    }
}
