package com.whaty.products.controller.basic;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.basic.impl.UnitManageServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.UNIT_BASIC_SQL;

/**
 * 单位管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/unitManage")
@BasicOperateRecord("单位")
@SqlRecord(namespace = "unit", sql = UNIT_BASIC_SQL)
public class UnitManageController extends TycjGridBaseControllerAdapter<PeUnit> {

    @Resource(name = "unitManageService")
    private UnitManageServiceImpl unitManageService;

    /**
     * 列举单位树
     * @return
     */
    @GetMapping("/unitTree")
    public ResultDataModel listUnitTree() {
        return ResultDataModel.handleSuccessResult(this.unitManageService.listCurrentUnitTree());
    }

    /**
     * 下载单位导入列表
     * @return
     */
    @RequestMapping("/downUnitImportTemplate")
    public void downUnitImportTemplate(HttpServletResponse response) throws ServletException, IOException {
        this.downFile(response, this.unitManageService::downUnitImportTemplate, "unitUpload.xls");
    }

    /**
     * 单位导入
     * @param upload
     * @return
     */
    @RequestMapping("/uploadUnit")
    public ResultDataModel uploadUnit(@RequestParam("upload") MultipartFile upload) throws Exception {
        Map<String, Object> resultMap = this.unitManageService
                .doUploadUnit(CommonUtils.convertMultipartFileToFile(upload));
        NoticeServerPollUtils.noticeUploadError(resultMap);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    @Override
    public GridService<PeUnit> getGridService() {
        return this.unitManageService;
    }
}
