package com.whaty.products.controller.oltrain.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlEnrollColumn;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.enroll.impl.OlEnrollColumnManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报名项管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/olTrain/enroll/enrollColumnManage")
public class OlEnrollColumnManageController extends TycjGridBaseControllerAdapter<OlEnrollColumn> {

    @Resource(name = "olEnrollColumnManageService")
    private OlEnrollColumnManageServiceImpl olEnrollColumnManageService;

    @Override
    public GridService<OlEnrollColumn> getGridService() {
        return this.olEnrollColumnManageService;
    }
}