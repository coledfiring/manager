package com.whaty.products.controller.hbgr.check;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeCheckServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;
import java.text.ParseException;

import static com.whaty.constant.SqlRecordConstants.RECEIVED_UPDATE;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peCheckManage")
public class PeCheckManageController extends TycjGridBaseControllerAdapter<PeCheck> {

    @Resource(name = "peCheckService")
    private PeCheckServiceImpl peCheckService;

    @Override
    public GridService<PeCheck> getGridService() {
        return this.peCheckService;
    }

    /**
     * 获取添加巡检选项
     *
     * @return
     */
    @RequestMapping("/getCheckOptions")
    public ResultDataModel getCheckOptions() {
        return ResultDataModel.handleSuccessResult(peCheckService.getCheckOptions());
    }

    /**
     * 获取添加巡检选项
     *
     * @return
     */
    @RequestMapping("/checkData/{id}")
    public ResultDataModel addExperience(@PathVariable String id) {
        return ResultDataModel.handleSuccessResult(peCheckService.getCheckData(id));
    }

    /**
     * 添加修改巡检
     *
     * @param peCheck
     * @return
     */
    @PostMapping("/addOrUpdateCheck")
    @OperateRecord("巡检管理")
    @SqlRecord(namespace = "addOrUpdateCheck", sql = RECEIVED_UPDATE)
    public ResultDataModel addOrUpdateReceived(@RequestBody PeCheck peCheck) throws IOException, ParseException {
        peCheckService.addOrUpdateCheck(peCheck);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 设为有效
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/setValid")
    @OperateRecord("巡检管理")
    public ResultDataModel setValid(@RequestBody ParamsDataModel paramsData) throws IOException {
        peCheckService.setValid(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    /**
     * 设为无效
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/setNoValid")
    @OperateRecord("巡检管理")
    public ResultDataModel setNoValid(@RequestBody ParamsDataModel paramsData) throws IOException {
        peCheckService.setNoValid(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("设置成功");
    }
}
