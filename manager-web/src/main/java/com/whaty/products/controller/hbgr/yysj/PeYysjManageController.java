package com.whaty.products.controller.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PrintFormConfig;
import com.whaty.domain.bean.hbgr.yysj.PeYysj;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeYysjServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * author weipengsen  Date 2020/6/17
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/yysjManage")
public class PeYysjManageController extends TycjGridBaseControllerAdapter <PeYysj> {

    @Resource(name = "peYysjService")
    private PeYysjServiceImpl peYysjService;

    @Override
    public GridService<PeYysj> getGridService() {
        return this.peYysjService;
    }


    /**
     * 运营数据批量压缩导出
     *
     * @param ids
     * @param response
     */
    @RequestMapping("/downloadYysjZip")
    public void downloadYysjZip(@RequestParam(CommonConstant.PARAM_IDS) String ids, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.peYysjService.downloadYysjZip(ids, response);
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }

    /**
     * 打印学生成绩单
     * @return
     */
    @RequestMapping("/printYysjDetail")
    public ResultDataModel printStudentScore(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = paramsDataModel.getStringParameter(CommonConstant.PARAM_IDS);
        List<PrintFormConfig> printFormConfigs = this.peYysjService.printYysjDetail(ids);
        return ResultDataModel.handleSuccessResult(printFormConfigs);
    }

    /**
     * 获取流程图数据
     *
     * @return
     */
    @RequestMapping("/getFlowData")
    public ResultDataModel getFlowData() throws Exception {
        return ResultDataModel.handleSuccessResult(this.peYysjService.getFlowData());
    }

}
