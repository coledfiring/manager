package com.whaty.schedule.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.schedule.bean.PeScheduleJobRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度记录查询
 *
 * @author weipengsen
 */
@Lazy
@RestController("scheduleJobRecordController")
@RequestMapping("/superAdmin/schedule/scheduleJobRecord")
public class ScheduleJobRecordController extends GridBaseController<PeScheduleJobRecord> {
}
