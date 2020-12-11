package com.whaty.products.controller.oltrain.onlineexam;

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
@RequestMapping("/entity/olTrain/olOnlineExamScoreAnalyse")
public class OLOnlineExamScoreAnalyseController extends TycjGridBaseControllerAdapter<StudentOnlineExamScore> {
}
