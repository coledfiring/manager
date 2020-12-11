package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardBlock;
import com.whaty.analyse.mvc.service.impl.BoardBlockConfigManageServiceImpl;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 看板关联块管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/boardBlockConfigManage")
public class BoardBlockConfigManageController extends TycjGridBaseControllerAdapter<AnalyseBoardBlock> {

    @Resource(name = "boardBlockConfigManageService")
    private BoardBlockConfigManageServiceImpl boardBlockConfigManageService;

    @Override
    public GridService<AnalyseBoardBlock> getGridService() {
        return this.boardBlockConfigManageService;
    }
}
