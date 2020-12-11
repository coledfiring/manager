package com.whaty.products.controller.hbgr.device;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeDevice;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeDeviceInfoManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipOutputStream;

/**
 * author weipengsen  Date 2020/6/19
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peDeviceInfoManage")
public class PeDeviceInfoManageController extends TycjGridBaseControllerAdapter<PeDevice> {


    @Resource(name = "peDeviceInfoManageService")
    private PeDeviceInfoManageServiceImpl peDeviceInfoManageService;

    @Override
    public GridService<PeDevice> getGridService() {
        return this.peDeviceInfoManageService;
    }


    @RequestMapping("/downLoadDeviceInfoQrcode")
    public void downLoadDeviceInfoQrcode(@RequestParam(CommonConstant.PARAM_IDS) String ids, HttpServletResponse response) throws IOException, ServletException {
        CommonUtils.setContentToDownload(response, "设备二维码.zip");
        try {
            this.peDeviceInfoManageService.downLoadDeviceInfoQrcode(ids, response.getOutputStream());
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }
}
