package com.whaty.products.controller.completion;

import com.whaty.domain.bean.PeClass;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目结业管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/completion/itemCompletionManage")
public class ItemCompletionManageController extends TycjGridBaseControllerAdapter<PeClass> {
}
