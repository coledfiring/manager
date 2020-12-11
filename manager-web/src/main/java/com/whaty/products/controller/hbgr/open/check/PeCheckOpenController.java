package com.whaty.products.controller.hbgr.open.check;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.domain.bean.hbgr.yysj.PeCheckRecord;
import com.whaty.domain.bean.hbgr.yysj.PeCheckWaterRecord;
import com.whaty.products.service.hbgr.yysj.PeCheckRecordServiceImpl;
import com.whaty.products.service.hbgr.yysj.PeCheckWaterRecordServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * author weipengsen  Date 2020/7/13
 */
@Lazy
@RestController
@RequestMapping("/open/check/checkRecord")
public class PeCheckOpenController {

    @Resource(name = "peCheckRecordService")
    private PeCheckRecordServiceImpl peCheckRecordService;

    @Resource(name = "peCheckWaterRecordService")
    private PeCheckWaterRecordServiceImpl peCheckWaterRecordService;

    @RequestMapping("/getCheckOptions")
    public ResultDataModel getCheckOptions() {
        return ResultDataModel.handleSuccessResult(peCheckRecordService.getCheckOptions());
    }

    @RequestMapping("/addCheckRecord")
    public ResultDataModel addCheckRecord(@RequestBody PeCheckRecord peCheckRecord) {
        peCheckRecordService.addCheckRecord(peCheckRecord);
        return ResultDataModel.handleSuccessResult("记录成功");
    }

    @RequestMapping("/getWaterCheckOptions")
    public ResultDataModel getWaterCheckOptions() {
        return ResultDataModel.handleSuccessResult(peCheckWaterRecordService.getWaterCheckOptions());
    }

    @RequestMapping("/addWeterCheckRecord")
    public ResultDataModel addWeterCheckRecord(@RequestBody PeCheckWaterRecord peCheckWaterRecord) {
        peCheckWaterRecordService.addWeterCheckRecord(peCheckWaterRecord);
        return ResultDataModel.handleSuccessResult("记录成功");
    }

    @RequestMapping("/getCheckRecord/{id}")
    public ResultDataModel getCheckRecord(@PathVariable String id) {
        Map<String, Object> resultMap = new HashMap<>(16);
        resultMap.put("waterRecords", peCheckWaterRecordService.getCheckWaterRecord(id));
        resultMap.put("checkRecords", peCheckRecordService.getCheckRecord(id));
        return ResultDataModel.handleSuccessResult(resultMap);
    }
}
