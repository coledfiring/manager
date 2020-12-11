package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.*;

/**
 * 附件类型管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/attachFileTypeManage")
@BasicOperateRecord("附件类型")
@SqlRecord(namespace = "attachFileType", sql = ATTACH_FILE_TYPE_BASIC_SQL)
public class AttachFileTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "attachFileTypeManageService")
    private GridService attachFileTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.attachFileTypeManageService;
    }
}
