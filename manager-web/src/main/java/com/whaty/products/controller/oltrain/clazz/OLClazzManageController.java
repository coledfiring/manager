package com.whaty.products.controller.oltrain.clazz;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.exception.ErrorCodeEnum;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeClass;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.clazz.impl.OLClazzManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * 班级管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/clazz/clazzManage")
@BasicOperateRecord("班级管理")
public class OLClazzManageController extends TycjGridBaseControllerAdapter<OlPeClass> {

    @Resource(name = "olClazzManageService")
    private OLClazzManageServiceImpl olClazzManageService;

    /**
     * 通过流程创建数据
     *
     * @param paramsData
     * @return
     * @throws EntityException
     */
    @PostMapping("/createByFlow")
    public ResultDataModel createByFlow(@RequestBody ParamsDataModel<OlPeClass> paramsData) throws ServiceException {
        this.initParams(paramsData);
        GridConfig gridConfig = this.initGrid(paramsData);
        return ResultDataModel.handleSuccessResult(Collections.singletonMap("id",
                this.olClazzManageService.createByFlow(paramsData.getBean(),
                        paramsData.getParams(), gridConfig)));
    }

    /**
     * 更新基础信息
     *
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/updateBasicInfo")
    public ResultDataModel updateBasicInfo(@RequestBody ParamsDataModel<OlPeClass> paramsDataModel) {
        this.initParams(paramsDataModel);
        GridConfig gridConfig = this.initGrid(paramsDataModel);
        Map target = this.olClazzManageService
                .updateBasicInfo(paramsDataModel.getBean(), gridConfig);
        return !"false".equals((target.get("success")))
                && Boolean.valueOf(String.valueOf((target.get("success")))).booleanValue() ?
                ResultDataModel.handleSuccessResult(String.valueOf(target.get("info"))) :
                ResultDataModel.handleFailureResult(ErrorCodeEnum.SYS_COMMON_CUSTOM_MSG.getCode(),
                        String.valueOf(target.get("info")));
    }

    /**
     * 获取班级图片
     * @return
     */
    @RequestMapping(value = "/classImage/{classId}", method = RequestMethod.GET)
    public ResultDataModel getCourseImage(@PathVariable String classId) {
        return ResultDataModel.handleSuccessResult(this.olClazzManageService.getClassImage(classId));
    }

    /**
     * 上传班级图片
     * @return
     */
    @RequestMapping("/uploadClassImage")
    public ResultDataModel uploadCourseImage(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                             @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        this.olClazzManageService
                .doUploadClassImage(ids, CommonUtils.convertMultipartFileToFile(upload), upload.getOriginalFilename());
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    @Override
    public GridService<OlPeClass> getGridService() {
        return this.olClazzManageService;
    }
}
