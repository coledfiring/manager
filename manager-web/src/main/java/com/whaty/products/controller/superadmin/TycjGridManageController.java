package com.whaty.products.controller.superadmin;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.superadmin.impl.TycjGridManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * grid管理成教扩展功能
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/grid/gridManage")
public class TycjGridManageController extends TycjGridBaseControllerAdapter<GridBasicConfig> {

    @Resource(name = "tycjGridManageService")
    private TycjGridManageServiceImpl gridManageService;

    /**
     * 下载grid配置
     * @param response
     */
    @RequestMapping("/downUploadGridConfig")
    public void downUploadGridConfig(HttpServletResponse response) throws ServletException, IOException {
        this.downFile(response, gridManageService::downUploadGridConfig, "gridManage.xls");
    }

    /**
     * 上传grid配置
     * @param upload
     * @return
     */
    @RequestMapping("/uploadGridConfig")
    public ResultDataModel uploadGridConfig(@RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        return ResultDataModel.handleSuccessResult(this.gridManageService
                .doUploadGridConfig(CommonUtils.convertMultipartFileToFile(upload)));
    }

    /**
     * 下载column配置
     * @param response
     */
    @RequestMapping("/downUploadGridColumnConfig")
    public void downUploadGridColumnConfig(HttpServletResponse response) throws ServletException, IOException {
        this.downFile(response, gridManageService::downUploadGridColumnConfig, "gridColumnManage.xls");
    }

    /**
     * 导入column配置
     * @param upload
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadGridColumnConfig")
    public ResultDataModel uploadGridColumnConfig(@RequestParam(CommonConstant.PARAM_UPLOAD) MultipartFile upload)
            throws Exception {
        return ResultDataModel.handleSuccessResult(this.gridManageService
                .doUploadGridColumnConfig(CommonUtils.convertMultipartFileToFile(upload)));
    }

    @Override
    public GridService<GridBasicConfig> getGridService() {
        return this.gridManageService;
    }
}
