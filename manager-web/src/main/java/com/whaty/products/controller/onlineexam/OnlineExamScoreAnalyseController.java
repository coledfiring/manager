package com.whaty.products.controller.onlineexam;

import com.whaty.domain.bean.StudentOnlineExamScore;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在线考试成绩统计
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/onlineExam/onlineExamScoreAnalyse")
public class OnlineExamScoreAnalyseController extends TycjGridBaseControllerAdapter<StudentOnlineExamScore> {
}
