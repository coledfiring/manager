package com.whaty.products.controller.common;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.products.service.common.CascadeSearchService;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通用级联搜索controller
 * @author weipengsen
 */
@Lazy
@RestController("cascadeSearchController")
@RequestMapping("/common/cascadeSearch")
public class CascadeSearchController {

    @Resource(name = "cascadeSearchServiceImpl")
    private CascadeSearchService cascadeSearchService;

    /**
     * 列出级联班级
     * @return
     */
    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ResultDataModel listCascadeClass() {
        List<Object[]> classes = this.cascadeSearchService
                .listCascadeClass(RequestUtils.getRequestMap(CommonUtils.getRequest()));
        return ResultDataModel.handleSuccessResult(classes);
    }

}
