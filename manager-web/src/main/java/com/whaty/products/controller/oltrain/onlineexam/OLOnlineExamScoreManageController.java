package com.whaty.products.controller.oltrain.onlineexam;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlStudentOnlineExamScore;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.onlineexam.impl.OLOnlineExamScoreManageServiceImpl;
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
@RequestMapping("/entity/olTrain/olOnlineExamScoreManage")
public class OLOnlineExamScoreManageController extends TycjGridBaseControllerAdapter<OlStudentOnlineExamScore> {

    @Resource(name = "olOnlineExamScoreManageService")
    private OLOnlineExamScoreManageServiceImpl olOnlineExamScoreManageService;

    @RequestMapping("/publishStudentScore")
    public ResultDataModel publishStudentScore(@RequestBody ParamsDataModel paramsData) {
        int count = this.olOnlineExamScoreManageService.doPublishStudentScore(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("发布成功，共成功发布" + count + "条学生成绩");
    }

    /**
     * @param paramsData
     * @return
     */
    @RequestMapping("/cancelPublishStudentScore")
    public ResultDataModel cancelPublishStudentScore(@RequestBody ParamsDataModel paramsData) {
        int count = this.olOnlineExamScoreManageService.doCancelPublishStudentScore(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("取消发布成功，共取消发布" + count + "条学生成绩");
    }

    @Override
    public GridService<OlStudentOnlineExamScore> getGridService() {
        return this.olOnlineExamScoreManageService;
    }
}
