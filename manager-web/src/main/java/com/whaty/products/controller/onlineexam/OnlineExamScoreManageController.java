package com.whaty.products.controller.onlineexam;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.StudentOnlineExamScore;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.onlineexam.OnlineExamScoreManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 在线考试成绩管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/onlineExam/onlineExamScoreManage")
public class OnlineExamScoreManageController extends TycjGridBaseControllerAdapter<StudentOnlineExamScore> {

    @Resource(name = "onlineExamScoreManageService")
    private OnlineExamScoreManageServiceImpl onlineExamScoreManageService;

    @Override
    public GridService<StudentOnlineExamScore> getGridService() {
        return this.onlineExamScoreManageService;
    }

    @RequestMapping("/publishStudentScore")
    public ResultDataModel publishStudentScore(@RequestBody ParamsDataModel paramsData) {
        int count = this.onlineExamScoreManageService.doPublishStudentScore(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("发布成功，共成功发布" + count + "条学生成绩");
    }

    /**
     * @param paramsData
     * @return
     */
    @RequestMapping("/cancelPublishStudentScore")
    public ResultDataModel cancelPublishStudentScore(@RequestBody ParamsDataModel paramsData) {
        int count = this.onlineExamScoreManageService.doCancelPublishStudentScore(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("取消发布成功，共取消发布" + count + "条学生成绩");
    }
}
