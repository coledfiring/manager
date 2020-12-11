package com.whaty.products.controller.message.business;

import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务信息发送列表
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/businessMessage")
public class BusinessMessageController extends TycjGridBaseControllerAdapter {
}
