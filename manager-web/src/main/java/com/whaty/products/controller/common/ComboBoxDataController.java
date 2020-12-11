package com.whaty.products.controller.common;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.common.ComboBoxDataService;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * comboBox数据查询controller
 *
 * @author weipengsen
 */
@Lazy
@RequestMapping("/common/comboBoxData")
@RestController("comboBoxDataController")
public class ComboBoxDataController {

    @Resource(name = "comboBoxDataService")
    private ComboBoxDataService comboBoxDataService;

    /**
     * 获得id，name的数据
     * @param sqlId
     * @param beanName
     * @return
     */
    @RequestMapping("/getIdNameData")
    public ResultDataModel getIdNameData(
            @RequestParam(value = CommonConstant.PARAM_SQL_ID, required = false) String sqlId,
            @RequestParam(value = CommonConstant.PARAM_BEAN_NAME, required = false) String beanName) {
        if (StringUtils.isNotBlank(sqlId)) {
            String params = CommonUtils.getRequest().getQueryString();
            return this.getIdNameDataBySqlId(sqlId, params);
        } else if (StringUtils.isNotBlank(beanName)) {
            return this.getIdNameDataByBeanName(beanName);
        } else {
            throw new ParameterIllegalException();
        }
    }

    /**
     * 根据sqlId获得id，name数据
     *
     * @param sqlId
     * @param params
     * @return
     */
    private ResultDataModel getIdNameDataBySqlId(String sqlId, String params) {
        List<Object[]> data = this.comboBoxDataService.getIdNameListBySqlId(sqlId, params);
        return ResultDataModel.handleSuccessResult(data);
    }

    /**
     * 根据beanName获得id，name数据
     *
     * @param beanName
     * @return
     */
    private ResultDataModel getIdNameDataByBeanName(String beanName) {
        List<Object[]> data = this.comboBoxDataService.getIdNameListByBeanName(beanName);
        return ResultDataModel.handleSuccessResult(data);
    }

    /**
     * 获得有效的bean数据信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getActiveBeanData")
    public ResultDataModel getActiveBeanData(HttpServletRequest request) {
        String beanName = request.getParameter(CommonConstant.PARAM_BEAN_NAME);
        List<Object[]> data = this.comboBoxDataService.getBasicBeanDataList(beanName,
                CommonConstant.BEAN_NEED_ACTIVE);
        return ResultDataModel.handleSuccessResult(data);
    }

    /**
     * 列出所有用户可以看到的角色
     * @return
     */
    @RequestMapping("/roleUserCanView")
    public ResultDataModel listRoleUserCanView() {
        List<Object[]> roles = this.comboBoxDataService.listRoleUserCanView();
        return ResultDataModel.handleSuccessResult(roles);
    }

}
