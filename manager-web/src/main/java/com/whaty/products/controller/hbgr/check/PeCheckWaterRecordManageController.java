package com.whaty.products.controller.hbgr.check;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeCheckRecord;
import com.whaty.domain.bean.hbgr.yysj.PeCheckWaterRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeCheckRecordServiceImpl;
import com.whaty.products.service.hbgr.yysj.PeCheckWaterRecordServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/13
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peCheckWaterRecordManage")
public class PeCheckWaterRecordManageController extends TycjGridBaseControllerAdapter<PeCheckWaterRecord> {

    @Resource(name = "peCheckWaterRecordService")
    private PeCheckWaterRecordServiceImpl peCheckWaterRecordService;

    @Override
    public GridService<PeCheckWaterRecord> getGridService() {
        return this.peCheckWaterRecordService;
    }
}
