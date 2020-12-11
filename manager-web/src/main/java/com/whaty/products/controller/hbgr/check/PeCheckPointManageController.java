package com.whaty.products.controller.hbgr.check;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.domain.bean.hbgr.yysj.PeCheckPoint;
import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeCheckPointServiceImpl;
import com.whaty.products.service.hbgr.yysj.PeCheckServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * author weipengsen  Date 2020/7/7
 */
@Lazy
@RestController
@RequestMapping("/entity/check/peCheckPointManage")
public class PeCheckPointManageController extends TycjGridBaseControllerAdapter<PeCheckPoint> {


    @Resource(name = "peCheckPointService")
    private PeCheckPointServiceImpl peCheckPointService;

    @Override
    public GridService<PeCheckPoint> getGridService() {
        return this.peCheckPointService;
    }

    @RequestMapping("/downLoadQrcode")
    public void downLoadQrcode(@RequestParam(CommonConstant.PARAM_IDS) String ids, HttpServletResponse response) throws IOException, ServletException {
        CommonUtils.setContentToDownload(response, "巡检点二维码.zip");
        try {
            this.peCheckPointService.downLoadCheckRecordQrcode(ids, response.getOutputStream());
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }
}