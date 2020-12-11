package com.whaty.products.controller.common;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.AttachFile;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.common.constant.ComConstant;
import com.whaty.products.service.common.impl.AttachFileManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.whaty.constant.SqlRecordConstants.ATTACH_FILE;

/**
 * 附件管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/common/attachFile")
@BasicOperateRecord("附件管理")
@SqlRecord(namespace = "attachFile", sql = ATTACH_FILE)
public class AttachFileManageController extends TycjGridBaseControllerAdapter<AttachFile> {

    @Resource(name = "attachFileManageService")
    private AttachFileManageServiceImpl attachFileService;

    private final static Logger logger = LoggerFactory.getLogger(AttachFileManageController.class);

    /**
     * 获取附件配置
     *
     * @param namespace
     * @return
     */
    @RequestMapping(value = "/attachConfig", method = RequestMethod.GET)
    public ResultDataModel getAttachConfig(@RequestParam("namespace") String namespace) {
        return ResultDataModel.handleSuccessResult(this.attachFileService.getAttachConfig(namespace));
    }

    /**
     * 上传附件
     * @return
     */
    @RequestMapping("/uploadAttachFile")
    @OperateRecord(value = "上传附件", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE, isImportant = true)
    public ResultDataModel uploadAttachFile(@RequestParam(CommonConstant.PARAM_PARENT_ID) String linkId,
                                            @RequestParam(ComConstant.PARAM_NAMESPACE) String namespace,
                                            @RequestParam(value=ComConstant.PARAM_SCENE , required=false,
                                                    defaultValue="") String flagScene,
                                            @RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        this.attachFileService.doUploadAttachFile(linkId, namespace, upload.getOriginalFilename(), flagScene,
                CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.selfNotice(String.format("附件[%s]上传成功", upload.getOriginalFilename()),
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        return ResultDataModel.handleSuccessResult("上传成功");
    }

    /**
     * 下载附件
     * @param ids
     * @return
     */
    @RequestMapping("/downAttachFile")
    public void downAttachFile(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                                          HttpServletResponse response) throws ServletException, IOException {
        try {
            this.attachFileService.downAttachFile(ids, response);
        } catch (AbstractBasicException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getInfo());
            }
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("系统错误,url:" + CommonUtils.getRequest().getRequestURI(), e);
            }
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    /**
     * 预览图片
     * @param ids
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/previewPicture")
    public void previewPicture(@RequestParam(CommonConstant.PARAM_IDS) String ids,
                               HttpServletResponse response) throws ServletException, IOException {
        this.attachFileService.previewPicture(ids, response);
    }

    /**
     * 获取现场
     *
     * @return
     */
    @RequestMapping("/getFlagScene")
    public ResultDataModel getFlagScene() {
        return ResultDataModel.handleSuccessResult(this.attachFileService.getFlagScene());
    }

    @Override
    public GridService<AttachFile> getGridService() {
        return this.attachFileService;
    }
}
