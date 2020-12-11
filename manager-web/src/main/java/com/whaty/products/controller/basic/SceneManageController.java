package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CheckPointManageServiceImpl;
import com.whaty.products.service.basic.impl.SceneManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CHECK_SCENE_BASIC_SQL;

/**
 * author weipengsen  Date 2020/6/20
 *
 * 现场管理
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/sceneManage")
@BasicOperateRecord("现场")
@SqlRecord(namespace = "flagScene", sql = CHECK_SCENE_BASIC_SQL)
public class SceneManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "sceneManageService")
    private SceneManageServiceImpl sceneManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.sceneManageService;
    }
}
