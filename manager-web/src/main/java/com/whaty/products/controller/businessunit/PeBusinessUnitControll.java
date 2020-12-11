package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeBusinessUnit;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessUnitServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单位基本信息
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/peBusinessUnit")
public class PeBusinessUnitControll extends TycjGridBaseControllerAdapter<PeBusinessUnit> {

    @Resource(name = "peBusinessUnitService")
    private PeBusinessUnitServiceImpl peBusinessUnitService;

    /**
     * 重置密码
     *
     * @return
     */
    @RequestMapping(value = "/resetUserPwd")
    @OperateRecord(value = "重置密码", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel cancelEnroll(@RequestBody ParamsDataModel paramsDataModel) {
        int count = peBusinessUnitService.resetUserPwd(this.getIds(paramsDataModel), this.getSite());
        return ResultDataModel.handleSuccessResult("成功为" + count + "位管理员重置密码！");
    }


    @Override
    public GridService<PeBusinessUnit> getGridService() {
        return this.peBusinessUnitService;
    }
}
