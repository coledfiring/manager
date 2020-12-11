package com.whaty.products.controller.hbgr.client;

import com.whaty.domain.bean.hbgr.yysj.PeClient;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.yysj.PeClientServiceImpl;
import com.whaty.util.CommonUtils;
import com.whaty.util.QrCodeUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author weipengsen  Date 2020/6/19
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peClientManage")
public class PeClientManageController extends TycjGridBaseControllerAdapter<PeClient> {

    @Resource(name = "peClientService")
    private PeClientServiceImpl peClientService;

    @RequestMapping("/downloadClientQrCode")
    public void downloadClientQrCode(HttpServletResponse response) throws Exception {
        peClientService.downloadClientQrCode(response.getOutputStream());
    }
}
