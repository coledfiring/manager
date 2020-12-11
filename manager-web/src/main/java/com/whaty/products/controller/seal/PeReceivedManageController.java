package com.whaty.products.controller.seal;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeReceived;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.seal.impl.PeReceivedManageServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.RECEIVED_BASIC_INFO;
import static com.whaty.constant.SqlRecordConstants.RECEIVED_UPDATE;

/**
 * 接待管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/seal/peReceivedManage")
@BasicOperateRecord(value = "接待管理")
@SqlRecord(namespace = "peReceived", sql = RECEIVED_BASIC_INFO)
public class PeReceivedManageController extends TycjGridBaseControllerAdapter<PeReceived> {

    @Resource(name = "peReceivedManageService")
    private PeReceivedManageServiceImpl peReceivedManageService;

    @Override
    public GridService<PeReceived> getGridService() {
        return this.peReceivedManageService;
    }

    /**
     * 获取系统用户
     *
     * @return
     */
    @RequestMapping("/manager")
    public ResultDataModel listManager() {
        return ResultDataModel.handleSuccessResult(peReceivedManageService.listManager());
    }

    /**
     * 添加接待信息
     *
     * @param received
     * @return
     */
    @PostMapping("/received")
    @OperateRecord("接待管理修改")
    @SqlRecord(namespace = "peReceived", sql = RECEIVED_UPDATE)
    public ResultDataModel addOrUpdateReceived(@RequestBody PeReceived received) {
        if(StringUtils.isBlank(received.getId())) {
            peReceivedManageService.addReceived(received);
        } else {
            peReceivedManageService.updateReceived(received);
        }
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 获取接待数据用于回显
     *
     * @param id
     * @return
     */
    @GetMapping("/receivedData/{id}")
    public ResultDataModel getReceivedData(@PathVariable String id) {
        return ResultDataModel.handleSuccessResult(peReceivedManageService.getReceivedData(id));
    }
}