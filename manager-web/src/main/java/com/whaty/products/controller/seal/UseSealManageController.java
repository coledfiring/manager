package com.whaty.products.controller.seal;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.UseSeal;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.seal.impl.UseSealManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.USE_SEAL_BASIC_INFO;

/**
 * 师资管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/seal/useSealManage")
@BasicOperateRecord(value = "用印管理")
@SqlRecord(namespace = "useSeal", sql = USE_SEAL_BASIC_INFO)
public class UseSealManageController extends TycjGridBaseControllerAdapter<UseSeal> {

    @Resource(name = "useSealManageService")
    private UseSealManageServiceImpl useSealManageService;

    @Override
    public GridService<UseSeal> getGridService() {
        return this.useSealManageService;
    }

    /**
     * 受理
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doAccept")
    public ResultDataModel doAccept(@RequestBody ParamsDataModel paramsDataModel) {
        useSealManageService.doAccept(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功");
    }
}

