package com.whaty.products.controller.record;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.record.impl.OperateRecordSearchServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 操作日志查询controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("operateRecordSearchController")
@RequestMapping("/entity/operateRecord/operateRecordSearch")
public class OperateRecordSearchController extends TycjGridBaseControllerAdapter {

    @Resource(name = "operateRecordSearchService")
    private OperateRecordSearchServiceImpl operateRecordSearchService;

    /**
     * 列举修改操作日志
     * @param namespace
     * @param id
     * @return
     */
    @GetMapping("/changeOperateRecordStatus")
    public ResultDataModel listChangeOperateRecordStatus(@RequestParam("namespace") String namespace,
                                                   @RequestParam("id") String id) {
        return ResultDataModel.handleSuccessResult(this.operateRecordSearchService
                .listOperateRecordStatus(namespace, id));
    }

    /**
     * 列举删除操作日志
     * @param namespace
     * @return
     */
    @GetMapping("/deleteOperateRecordStatus")
    public ResultDataModel listDeleteOperateRecordStatus(@RequestParam("namespace") String namespace) {
        return ResultDataModel.handleSuccessResult(this.operateRecordSearchService
                .listDeleteOperateRecordStatus(namespace));
    }

    @Override
    public GridService getGridService() {
        return this.operateRecordSearchService;
    }
}
