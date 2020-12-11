package com.whaty.products.controller.classmaster;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.classmaster.ClassMasterClassService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 班主任班级信息
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/classMaster/classMasterClass")
public class ClassMasterClassController {

    @Resource(name = "classMasterClassService")
    private ClassMasterClassService classMasterClassService;

    /**
     * 获取班级信息
     * @return
     */
    @GetMapping("/class/{classId}")
    public ResultDataModel getClassInfo(@PathVariable String classId) {
        Map<String, Object> classInfo = this.classMasterClassService.getClassInfo(classId);
        return ResultDataModel.handleSuccessResult(classInfo);
    }

    /**
     * 获取班级详情
     * @param classId
     * @return
     */
    @GetMapping("/classDetail/{classId}")
    public ResultDataModel getClassDetail(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterClassService.getClassDetail(classId));
    }

    /**
     * 保存内容
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/saveContent")
    public ResultDataModel saveContent(@RequestBody ParamsDataModel paramsDataModel) {
        String classId = paramsDataModel.getStringParameter("classId");
        String code = paramsDataModel.getStringParameter("code");
        String content = paramsDataModel.getStringParameter("content");
        this.classMasterClassService.saveContent(classId, code, content);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 列举网络课程
     * @param classId
     * @return
     */
    @PostMapping("/onlineCourse/{classId}")
    public ResultDataModel listOnlineCourses(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.classMasterClassService.listOnlineCourses(classId));
    }

}
