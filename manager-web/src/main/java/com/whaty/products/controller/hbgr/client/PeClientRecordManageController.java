package com.whaty.products.controller.hbgr.client;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeClientRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeClientRecordServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/15
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peClientRecordManage")
public class PeClientRecordManageController extends TycjGridBaseControllerAdapter<PeClientRecord> {

    @Resource(name = "peClientRecordService")
    private PeClientRecordServiceImpl peClientRecordService;

    @Override
    public GridService<PeClientRecord> getGridService() {
        return this.peClientRecordService;
    }

    @RequestMapping("/setProcess")
    public ResultDataModel setProcess(@RequestBody ParamsDataModel paramsData) {
        peClientRecordService.setProcess(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    @RequestMapping("/setNoProcess")
    public ResultDataModel setNoProcess(@RequestBody ParamsDataModel paramsData) {
        peClientRecordService.setNoProcess(this.getIds(paramsData));
        return ResultDataModel.handleSuccessResult("设置成功");
    }

}
