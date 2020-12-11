package com.whaty.framework.grid.adapter.controller;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.exception.ErrorCodeEnum;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.async.Task;
import com.whaty.core.framework.async.TaskManager;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.base.controller.TycjBaseControllerOperateSupport;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 通用成教grid基础controller适配器
 * 用于加入所有与成教业务相关的controller底层方法，如错误信息页面跳转等
 *
 * @author weipengsen
 */
public class TycjGridBaseControllerAdapter<T extends AbstractBean> extends GridBaseController<T> {

    @Resource(name = "tycjBaseController")
    private TycjBaseControllerOperateSupport tycjBaseController;

    @Resource(name = "gridServiceAdapter")
    private TycjGridServiceAdapter<T> tycjGridServiceAdapter;

    @Autowired
    private TaskManager taskManager;

    @Override
    @ResponseBody
    @RequestMapping({"/abstractStartExcelImport"})
    public ResultDataModel abstractStartExcelImport(@RequestBody(
            required = false
    ) ParamsDataModel paramsData) throws ApiException {
        String filePath = "incoming//" + paramsData.getStringParameter("filePath");
        Site site = this.getSite();
        String gridId = this.getGridId();
        GridConfig gridConfig = this.initGrid(site.getCode(), gridId);
        Task task = this.taskManager.newTask("excel批量导入：" + gridConfig.getTitle());
        try {
            this.getGridService().doExcelImport(task, filePath, gridConfig, site, paramsData.getParams());
        } finally {
            return ResultDataModel.handleSuccessResult(task);
        }
    }

    /**
     * 跳转到错误页面
     * @param message
     */
    protected void toErrorPage(String message, HttpServletResponse response) throws ServletException, IOException {
        this.tycjBaseController.toErrorPage(message, response);
    }

    /**
     * 下载文件
     * @param response
     * @param downFileFunction
     * @param fileName
     * @throws ServletException
     * @throws IOException
     */
    protected void downFile(HttpServletResponse response, Consumer<OutputStream> downFileFunction, String fileName)
            throws ServletException, IOException {
        this.tycjBaseController.downFile(response, downFileFunction, fileName);
    }

    @Override
    @ResponseBody
    @RequestMapping({"/abstractDelete"})
    public ResultDataModel abstractDelete(@RequestBody(required = false) ParamsDataModel<T> paramsData) throws ApiException {
        Map map;
        try {
            if (paramsData == null || MapUtils.isEmpty(paramsData.getParams())) {
                throw new ApiException(ErrorCodeEnum.PARAM_NOT_NULL_ERROR);
            }

            this.initParams(paramsData);
            GridConfig gridConfig = this.initGrid(paramsData);
            String ids = this.getIds(gridConfig, paramsData);
            map = this.delete(gridConfig, ids);
        } catch (DataIntegrityViolationException var5) {
            return ResultDataModel.handleFailureResult(ErrorCodeEnum.SYS_COMMON_CUSTOM_MSG.getCode(),
                    "数据已被其他信息引用，不能删除。");
        }

        if (map == null) {
            return ResultDataModel.handleFailureResult(ErrorCodeEnum.SYS_COMMON_CUSTOM_MSG.getCode(),
                    "删除失败，请联系管理员！");
        } else {
            return !"false".equals(map.get("success"))
                    && Boolean.valueOf(String.valueOf(map.get("success"))).booleanValue()
                    ? ResultDataModel.handleSuccessResult(String.valueOf(map.get("info")))
                    : ResultDataModel.handleFailureResult(ErrorCodeEnum.SYS_COMMON_CUSTOM_MSG.getCode(),
                    String.valueOf(map.get("info")));
        }
    }

    /**
     * 从请求参数中获取选择全部的ids
     * @param requestParams
     * @return
     */
    protected String getIds(Map<String, String> requestParams) {
        String ids = requestParams.get(CommonConstant.PARAM_IDS);
        if (StringUtils.isNotBlank(ids) && requestParams.get(CommonConstant.PARAM_PARAMS) != null) {
            String pageJson = UriEncoder.decode(requestParams.get(CommonConstant.PARAM_PARAMS));
            ParamsDataModel paramsDataModel = (ParamsDataModel) JSONObject.toBean(JSONObject.fromObject(pageJson),
                    ParamsDataModel.class);
            ids = this.getIds(paramsDataModel);
            requestParams.put(CommonConstant.PARAM_IDS, ids);
        }
        return ids;
    }

    /**
     * 获取请求参数
     * @return
     */
    protected Map<String, String> getRequestMap() {
        Map<String, String> params = RequestUtils.getRequestMap(CommonUtils.getRequest());
        this.getIds(params);
        return params;
    }

    /**
     * 获取paramsDataModel中的数据集合，并扩充选择全部的ids
     * @param paramsDataModel
     * @return
     */
    protected Map<String, Object> getParamsDataModelMap(ParamsDataModel paramsDataModel) {
        Map<String, Object> params = paramsDataModel.getParams();
        params.put(CommonConstant.PARAM_IDS, this.getIds(paramsDataModel));
        return params;
    }

    @Override
    public String getIds(GridConfig gridConfig, ParamsDataModel<T> paramsData) {
        String ids = paramsData.getStringParameter("ids");
        Page pageParam = null;
        if (paramsData.getPage() != null) {
            pageParam = paramsData.getPage();
        }
        int selectNum = ids == null ? 0 : ids.split(",").length;
        if (pageParam != null && pageParam.getTotalCount() > selectNum) {
            StringBuffer newIds = new StringBuffer();
            // curPage必须是1，否则由于getStartIndex方法会导致无法获取数据
            pageParam.setCurPage(1);
            pageParam.setPageSize(pageParam.getTotalCount());
            Page page = this.list(pageParam, gridConfig, paramsData.getParams());
            List items = page.getItems();
            if (CollectionUtils.isNotEmpty(items)) {
                Iterator var9 = items.iterator();
                while (var9.hasNext()) {
                    Map m = (Map) var9.next();
                    newIds.append(m.get("id").toString()).append(",");
                }
            }
            ids = newIds.toString();
        }
        return ids;
    }

    @Override
    public GridService<T> getGridService() {
        return this.tycjGridServiceAdapter;
    }
}
