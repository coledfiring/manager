package com.whaty.products.controller.hbgr.tem;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.tem.PeRealTem;
import com.whaty.domain.bean.hbgr.yysj.PeWarning;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.tem.PeRealTemManageServiceImpl;
import com.whaty.products.service.hbgr.warning.PeWarningManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.REVIEW_ITEM_MANAGE_INFO;

/**
 * @program: com.whaty.products.controller.hbgr.tem
 * @author: weipengsen
 * @create: 2020-12-15
 **/
@Lazy
@RestController
@RequestMapping("/entity/energy/peRealManage")
public class PeRealManageController  extends TycjGridBaseControllerAdapter<PeRealTem> {

    @Resource(name = "peRealTemManageService")
    private PeRealTemManageServiceImpl peRealTemManageService;

    @Override
    public GridService<PeRealTem> getGridService() {
        return this.peRealTemManageService;
    }

    /**
     * 设置评审状态
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setAddress")
    public ResultDataModel setAddress(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        peRealTemManageService.setAddress(ids, paramsDataModel.getStringParameter("address"),
                paramsDataModel.getStringParameter("flagScene"));
        return ResultDataModel.handleSuccessResult("操作成功！");
    }
}
