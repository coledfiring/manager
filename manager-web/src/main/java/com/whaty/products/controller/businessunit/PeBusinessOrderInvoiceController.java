package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeBusinessOrderInvoice;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessOrderInvoiceServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单发票管理
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/peBusinessOrderInvoice")
public class PeBusinessOrderInvoiceController extends TycjGridBaseControllerAdapter<PeBusinessOrderInvoice> {

    @Resource(name = "peBusinessOrderInvoiceService")
    private PeBusinessOrderInvoiceServiceImpl peBusinessOrderInvoiceService;

    @Override
    public GridService<PeBusinessOrderInvoice> getGridService() {
        return this.peBusinessOrderInvoiceService;
    }
}
