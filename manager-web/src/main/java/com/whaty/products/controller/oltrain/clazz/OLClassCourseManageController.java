package com.whaty.products.controller.oltrain.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlClassCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLClassCourseManageServiceImpl;
import com.whaty.products.service.oltrain.constant.OlTrainConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级课程管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olClassCourseManage")
@BasicOperateRecord("班级课程管理")
public class OLClassCourseManageController extends TycjGridBaseControllerAdapter<OlClassCourse> {

    @Resource(name = "olClassCourseManageService")
    private OLClassCourseManageServiceImpl olClassCourseManageService;

    /**
     * 添加到班级
     * @return
     */
    @RequestMapping("/addToClass")
    public ResultDataModel addToClass(@RequestBody ParamsDataModel paramsDataModel) {
        String itemId = paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID);
        int count = this.olClassCourseManageService.addToClass(this.getIds(paramsDataModel), itemId);
        return ResultDataModel.handleSuccessResult("添加成功，共添加" + count + "条数据");
    }

    /**
     * 设置考核分数
     *
     * @return
     */
    @RequestMapping("/setPassScore")
    public ResultDataModel setPassScore(@RequestBody ParamsDataModel paramsDataModel) {
        String passScore = paramsDataModel.getStringParameter(OlTrainConstant.PARAM_PASS_SCORE);
        int count = this.olClassCourseManageService.doSetPassScore(this.getIds(paramsDataModel), passScore);
        return ResultDataModel.handleSuccessResult("设置成功，共设置" + count + "条数据");
    }

    @Override
    public GridService<OlClassCourse> getGridService() {
        return this.olClassCourseManageService;
    }
}
