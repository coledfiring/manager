package com.whaty.products.controller.hbgr.check;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.domain.bean.hbgr.yysj.PeCheckRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeCheckRecordServiceImpl;
import com.whaty.products.service.hbgr.yysj.PeCheckServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/13
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peCheckRecordManage")
public class PeCheckRecordManageController extends TycjGridBaseControllerAdapter<PeCheckRecord> {

    @Resource(name = "peCheckRecordService")
    private PeCheckRecordServiceImpl peCheckRecordService;

    @Override
    public GridService<PeCheckRecord> getGridService() {
        return this.peCheckRecordService;
    }
}
