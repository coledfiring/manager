package com.whaty.api.workspace.controller;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作室api controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("workspaceApiController")
@RequestMapping("/api/workspace")
public class WorkspaceApiController {

    /**
     * 获取管理端绝对路径
     * @return
     */
    @RequestMapping("/common/getManageRealPath")
    public ResultDataModel getManageRealPath() {
        return ResultDataModel.handleSuccessResult(CommonUtils.getRealPath("/"));
    }

}
