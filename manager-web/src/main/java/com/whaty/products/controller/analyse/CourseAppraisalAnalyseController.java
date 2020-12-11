package com.whaty.products.controller.analyse;

import com.whaty.domain.bean.TrainingItemCourse;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程统计分析
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/courseAppraisalAnalyse")
public class CourseAppraisalAnalyseController extends TycjGridBaseControllerAdapter<TrainingItemCourse> {}
