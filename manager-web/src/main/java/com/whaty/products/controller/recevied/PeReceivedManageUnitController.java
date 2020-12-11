package com.whaty.products.controller.recevied;

import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.products.controller.seal.PeReceivedManageController;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import static com.whaty.constant.SqlRecordConstants.RECEIVED_BASIC_INFO;

/**
 * 接待管理controller 以单位分组
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/received/peReceivedManageUnit")
@BasicOperateRecord(value = "接待管理")
@SqlRecord(namespace = "peReceived", sql = RECEIVED_BASIC_INFO)
public class PeReceivedManageUnitController extends PeReceivedManageController {

}