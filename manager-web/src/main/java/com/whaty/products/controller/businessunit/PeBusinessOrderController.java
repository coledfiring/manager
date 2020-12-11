package com.whaty.products.controller.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeBusinessOrder;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.businessunit.PeBusinessOrderServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 单位订单管理
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/peBusinessOrder")
public class PeBusinessOrderController extends TycjGridBaseControllerAdapter<PeBusinessOrder> {

    @Resource(name = "peBusinessOrderService")
    private PeBusinessOrderServiceImpl peBusinessOrderService;

    @Resource(name = "printTemplateService")
    private PrintTemplateService printTemplateService;

    /**
     * 支付：汇款支付、现场支付(订单状态)
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/doPayOrder")
    @OperateRecord(value = "单位订单支付", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel doPayOrder(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("操作成功，共操作" +
                peBusinessOrderService.doPayOrder(this.getIds(paramsDataModel)) + "条数据");
    }

    /**
     * 在线支付(订单状态)
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/doPayOnlineOrder")
    @OperateRecord(value = "在线支付", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel doPayOnlineOrder(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("操作成功，共操作" +
                peBusinessOrderService.doPayOnlineOrder(this.getIds(paramsDataModel)) + "条数据");
    }

    /**
     * 取消缴费
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelPayOrder")
    @OperateRecord(value = "取消缴费", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel cancelPayOrder(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("操作成功，共操作" +
                peBusinessOrderService.doCancelPayOrder(this.getIds(paramsDataModel)) + "条数据");
    }

    /**
     * 确认已支付
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/doConfirmPaymentOrder")
    @OperateRecord(value = "确认已支付", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel doConfirmPaymentOrder(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("操作成功，共操作" +
                peBusinessOrderService.doConfirmPaymentOrder(this.getIds(paramsDataModel)) + "条数据");
    }

    /**
     * 订单审核不通过
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/doNoPassPaymentOrder")
    @OperateRecord(value = "订单审核不通过", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel doNoPassPaymentOrder(@RequestBody ParamsDataModel paramsDataModel) {
        String auditOpinion = paramsDataModel.getStringParameter("auditOpinion");
        return ResultDataModel.handleSuccessResult("操作成功，共操作" +
                peBusinessOrderService.doNoPassPaymentOrder(this.getIds(paramsDataModel), auditOpinion) + "条数据");
    }

    /**
     * 上传汇款单文件
     * @param upload
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadMoneyOrderFile", method = RequestMethod.POST)
    @OperateRecord(value = "上传汇款单文件",
            moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE, isImportant = true)
    public ResultDataModel uploadExamBatchExamNoteFile(@RequestParam("file") MultipartFile upload, String orderId) throws Exception {
        this.peBusinessOrderService.
                doUploadMoneyOrderFile(CommonUtils.convertMultipartFileToFile(upload), upload.getOriginalFilename(), orderId);
        return ResultDataModel.handleSuccessResult("上传汇款单成功");
    }

    /**
     * 下载导入学生缴费模板
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/downloadStudentFeeDetailTemplate")
    public void downloadStudentFeeDetailTemplate(HttpServletResponse response)
            throws IOException, ServletException {
        this.downFile(response, peBusinessOrderService::downloadStudentFeeDetailTemplate, "studentFeeDetail.xls");
    }

    /**
     * 批量导入学生缴费信息
     * @param upload
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadStudentFeeDetailInfo")
    public ResultDataModel uploadStudentFeeDetailInfo(@RequestParam(CommonConstant.PARAM_FILE_UPLOAD) MultipartFile upload)
            throws Exception {
        Map<String, Object> resultMap = this.peBusinessOrderService
                .doUploadStudentFeeDetailInfo(CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /**
     * 打印听课证
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/printAndDown")
    @OperateRecord(value = "打印听课证",
            moduleCode = OperateRecordModuleConstant.PRINT_MODULE_CODE, isImportant = true)
    public void printAndDown(HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> params = this.getRequestMap();
        try {
            peBusinessOrderService.checkIsCanPrintCertificate(this.getIds(params));
            this.printTemplateService.printAndDown(params, response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    /**
     * 获取易支付参数信息
     * @return
     */
    @GetMapping(value = "/easyPayParametersInfo")
    @OperateRecord(value = "获取易支付参数信息", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel getEasyPayParametersInfo(@RequestParam String businessOrderId) throws Exception {
        return ResultDataModel.handleSuccessResult(peBusinessOrderService.getEasyPayParametersInfo(businessOrderId));
    }

    /**
     * 更新企事业单位易智付订单号
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/updateEasyPayOrderNo")
    @OperateRecord(value = "更新企事业单位易智付订单号", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel updateEasyPayOrderNo(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult("更新操作成功，共操作" +
                peBusinessOrderService.updateEasyPayOrderNo(this.getIds(paramsDataModel)) + "条数据");
    }

    /**
     * 获取易支付订单信息
     * @param businessOrderId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/easyPayOrderInfo")
    @OperateRecord(value = "获取易支付订单信息", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel getEasyPayOrderInfo(@RequestParam String businessOrderId) throws Exception {
        return ResultDataModel.handleSuccessResult(peBusinessOrderService.getEasyPayOrderInfo(businessOrderId));
    }

    @Override
    public GridService<PeBusinessOrder> getGridService() {
        return this.peBusinessOrderService;
    }
}
