package com.whaty.products.controller.oltrain.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLAddClassStudentServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 添加班级学员
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olAddClassStudent")
@BasicOperateRecord("添加班级学员")
public class OLAddClassStudentController extends TycjGridBaseControllerAdapter<OlPeStudent> {

    @Resource(name = "olAddClassStudentService")
    private OLAddClassStudentServiceImpl olAddClassStudentService;


    /**
     * 添加补考考试学生
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public ResultDataModel addOnlineExamStudent(@RequestBody ParamsDataModel paramsDataModel) {
        String parentId = paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID);
        int count = olAddClassStudentService.doAddClassStudent(parentId, this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功，共添加" + count + "条数据");
    }


    @Override
    public GridService<OlPeStudent> getGridService() {
        return this.olAddClassStudentService;
    }
}
