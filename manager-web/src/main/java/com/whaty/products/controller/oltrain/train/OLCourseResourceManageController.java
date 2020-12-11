package com.whaty.products.controller.oltrain.train;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.train.impl.OLCourseResourceManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 课程资源管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olCourseResourceManage")
@BasicOperateRecord("课程资源管理")
public class OLCourseResourceManageController extends TycjGridBaseControllerAdapter<OlPeCourse> {

    @Resource(name = "olCourseResourceManageService")
    private OLCourseResourceManageServiceImpl olCourseResourceManageService;

    /**
     * 获取课程图片
     * @return
     */
    @RequestMapping(value = "/courseImage/{courseId}", method = RequestMethod.GET)
    public ResultDataModel getCourseImage(@PathVariable String courseId) {
        return ResultDataModel.handleSuccessResult(this.olCourseResourceManageService.getCourseImage(courseId));
    }

    /**
     * 上传课程图片
     * @return
     */
    @RequestMapping("/uploadCourseImage")
    public ResultDataModel uploadCourseImage(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                             @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        this.olCourseResourceManageService
                .doUploadCourseImage(ids, CommonUtils.convertMultipartFileToFile(upload), upload.getOriginalFilename());
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    @Override
    public GridService<OlPeCourse> getGridService() {
        return this.olCourseResourceManageService;
    }
}
