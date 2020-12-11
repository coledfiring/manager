package com.whaty.products.controller.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PrintFormConfig;
import com.whaty.domain.bean.hbgr.yysj.PeYysjDetail;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeYysjDetailServiceImpl;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * author weipengsen  Date 2020/6/17
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/yysjDetailManage")
public class PeYysjDetailManageController  extends TycjGridBaseControllerAdapter<PeYysjDetail> {

    @Resource(name = "peYysjDetailService")
    private PeYysjDetailServiceImpl peYysjDetailService;

    @Override
    public GridService<PeYysjDetail> getGridService() {
        return this.peYysjDetailService;
    }

    /**
     *  获取数据看板矩形模块数据
     *
     * @return
     */
    @RequestMapping("/getBoardYysj")
    public ResultDataModel getBoardYysj() throws Exception {
        return ResultDataModel.handleSuccessResult(this.peYysjDetailService.getBoardYysj());
    }
}
