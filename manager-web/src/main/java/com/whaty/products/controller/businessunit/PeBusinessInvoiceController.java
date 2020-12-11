package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeBusinessInvoice;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessInvoiceServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单位发票管理controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/businessInvoice")
public class PeBusinessInvoiceController extends TycjGridBaseControllerAdapter<PeBusinessInvoice> {

    @Resource(name = "peBusinessInvoiceService")
    private PeBusinessInvoiceServiceImpl peBusinessInvoiceService;

    @Override
    public GridService<PeBusinessInvoice> getGridService() {
        return this.peBusinessInvoiceService;
    }

}
