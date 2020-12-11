package com.whaty.products.controller.record;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.record.impl.OperateRecordManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 操作日志管理查看controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("operateRecordManageController")
@RequestMapping("/entity/superAdmin/operateRecordManage")
public class OperateRecordManageController {

    @Resource(name = "operateRecordManageService")
    private OperateRecordManageServiceImpl operateRecordManageService;

    /**
     * 列举操作日志文件
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/listFile")
    public ResultDataModel listFile(@RequestBody ParamsDataModel paramsDataModel) {
        List<Map<String, Object>> files = this.operateRecordManageService.listFile(paramsDataModel.getParams());
        return ResultDataModel.handleSuccessResult(files);
    }

}
