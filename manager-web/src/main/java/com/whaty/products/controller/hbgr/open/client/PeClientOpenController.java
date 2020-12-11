package com.whaty.products.controller.hbgr.open.client;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.domain.bean.hbgr.yysj.PeClientRecord;
import com.whaty.products.service.hbgr.yysj.PeCheckRecordServiceImpl;
import com.whaty.products.service.hbgr.yysj.PeClientRecordServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author weipengsen  Date 2020/7/15
 */
@Lazy
@RestController
@RequestMapping("/open/client/clientRecord")
public class PeClientOpenController {

    @Resource(name = "peClientRecordService")
    private PeClientRecordServiceImpl peClientRecordService;

    @RequestMapping("/addClientRecord")
    public ResultDataModel addClientRecord(@RequestBody PeClientRecord peClientRecord) {
        peClientRecordService.addClientRecord(peClientRecord);
        return ResultDataModel.handleSuccessResult("提交成功！");
    }
}
