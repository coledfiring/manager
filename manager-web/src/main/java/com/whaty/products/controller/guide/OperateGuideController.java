package com.whaty.products.controller.guide;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.service.guide.OperateGuideService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 操作指导查看controller
 * @author weipengsen
 */
@Lazy
@RestController("operateGuideController")
@RequestMapping("/guide/operateGuide")
public class OperateGuideController {

    @Resource(name = "operateGuideService")
    private OperateGuideService operateGuideService;

    /**
     * 获得所有可以查看的操作指导描述
     * @return
     */
    @RequestMapping(value = "/operateGuideDescription", method = RequestMethod.GET)
    public ResultDataModel listOperateGuideDescription() {
        List<Map<String, Object>> guideDescriptions = this.operateGuideService.listOperateGuideDescriptionCanShow();
        return ResultDataModel.handleSuccessResult(guideDescriptions);
    }

}
