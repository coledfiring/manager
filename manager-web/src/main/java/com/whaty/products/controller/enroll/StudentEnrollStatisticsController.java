package com.whaty.products.controller.enroll;

import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生报名信息统计
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/studentEnrollStatistics")
public class StudentEnrollStatisticsController extends TycjGridBaseControllerAdapter<TrainingItem> {
}
