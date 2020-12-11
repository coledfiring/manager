package com.whaty.products.controller.classmaster;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.classmaster.ClassMasterWorkspaceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班主任工作台
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/classMaster/classMasterWorkspace/{classId}")
public class ClassMasterWorkspaceController {

    @Resource(name = "classMasterWorkspaceService")
    private ClassMasterWorkspaceService classMasterWorkspaceService;

    /**
     * 列举日程
     * @param classId
     * @return
     */
    @GetMapping("/schedule")
    public ResultDataModel listSchedules(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterWorkspaceService.listSchedules(classId));
    }

    /**
     * 列举推荐通知公告
     * @param classId
     * @return
     */
    @GetMapping("/notice")
    public ResultDataModel listNotices(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterWorkspaceService.listNotices(classId));
    }

    /**
     * 列举准备工作
     * @param classId
     * @return
     */
    @GetMapping("/prepareItem")
    public ResultDataModel listPrepareItems(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterWorkspaceService.listPrepareItems(classId));
    }

    /**
     * 更改准备工作状态
     * @return
     */
    @PostMapping("/prepareItem")
    public ResultDataModel updatePrepareItemStatus(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter("id");
        Boolean done = (Boolean) paramsDataModel.getParameter("done");
        String classId = paramsDataModel.getStringParameter("classId");
        this.classMasterWorkspaceService.updatePrepareItemStatus(classId, id, done);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 列举课程表
     * @param classId
     * @return
     */
    @GetMapping("/courseTimeTable")
    public ResultDataModel listCourseTimeTable(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterWorkspaceService.listCourseTimeTable(classId));
    }

    /**
     * 删除课程表
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/courseTimeTable")
    public ResultDataModel deleteCourseTimeTable(@RequestBody ParamsDataModel paramsDataModel) {
        String classId = paramsDataModel.getStringParameter("classId");
        String id = paramsDataModel.getStringParameter("id");
        this.classMasterWorkspaceService.deleteCourseTimeTable(classId, id);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 根据id获取课程表
     * @param classId
     * @param courseTimeTableId
     * @return
     */
    @GetMapping("/courseTimeTable/{id}")
    public ResultDataModel getCourseTimeTable(@PathVariable("classId") String classId,
                                              @PathVariable("id") String courseTimeTableId) {
        return ResultDataModel.handleSuccessResult(this.classMasterWorkspaceService
                .getCourseTimeTable(classId, courseTimeTableId));
    }

}
