package com.whaty.wecharts.controller.wecharts;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.wecharts.constant.WeChartsConstants;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.helper.WeChartsHelper;
import com.whaty.wecharts.jsonbean.Option;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * weCharts controller
 * @author weipengsen
 */
@Lazy
@RestController("weChartsController")
@RequestMapping("/entity/weCharts/weCharts")
public class WeChartsController {

    /**
     * 查询图表
     * @param code
     * @param params
     * @return
     */
    @RequestMapping("/charts")
    public ResultDataModel queryCharts(@RequestParam(WeChartsConstants.PARAM_CODE) String code,
                                       @RequestParam(
                                               value = WeChartsConstants.PARAM_PARAMS,
                                               required = false
                                       ) String params,
                                       @RequestParam(
                                               value = WeChartsConstants.IS_CACHE,
                                               required = false
                                       ) String isCache) {
        WeChartsHelper helper = (WeChartsHelper) SpringUtil.getBean("weChartsHelper");
        try {
            Option option = helper.queryChart(code, params, isCache);
            return ResultDataModel.handleSuccessResult(option);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new UncheckException(e);
        } catch (WeChartsServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
