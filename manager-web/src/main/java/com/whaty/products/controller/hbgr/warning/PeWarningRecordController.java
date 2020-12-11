package com.whaty.products.controller.hbgr.warning;

import com.whaty.domain.bean.hbgr.warning.PeWarningRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author weipengsen  Date 2020/6/18
 */
@Lazy
@RestController
@RequestMapping("/entity/warning/warningRecord")
public class PeWarningRecordController extends TycjGridBaseControllerAdapter<PeWarningRecord> {
}
