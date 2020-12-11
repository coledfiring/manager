package com.whaty.products.controller.workspace;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.workspace.WorkBenchServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工作台管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/workspace/workbench")
public class WorkBenchController {

    @Resource(name = "workBenchServiceImpl")
    private WorkBenchServiceImpl workBenchService;

    /**
     * 获取工作台信息
     *
     * @return
     */
    @RequestMapping("/getWorkBenchData")
    public ResultDataModel getWorkBenchData() {
        return ResultDataModel.handleSuccessResult(workBenchService.getWorkBenchData());
    }
}
