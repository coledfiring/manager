package com.whaty.products.controller.clazz;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ClassManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CLASS_BASIC_SQL;

/**
 * 开班管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classManage")
@BasicOperateRecord("培训班")
@SqlRecord(namespace = "class", sql = CLASS_BASIC_SQL)
public class ClassManageController extends TycjGridBaseControllerAdapter<PeClass> {

    @Resource(name = "classManageService")
    private ClassManageServiceImpl classManageService;

    /**
     * 培训地点申请
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/applyTrainingPlace")
    public ResultDataModel applyTrainingPlace(@RequestBody ParamsDataModel paramsDataModel) {
        String placeType = paramsDataModel.getStringParameter("placeType");
        String placeNote = paramsDataModel.getStringParameter("placeNote");
        int count = this.classManageService.doApplyTrainingPlace(this.getIds(paramsDataModel), placeType, placeNote);
        return ResultDataModel.handleSuccessResult("申请成功，共成功" + count + "条数据，已申请的班级不能重复申请");
    }

    /**
     * 列举详情配置
     * @return
     */
    @GetMapping("/detailConfig")
    public ResultDataModel listDetailConfig() {
        return ResultDataModel.handleSuccessResult(this.classManageService.listDetailConfig());
    }

    /**
     * 获取培训扩展内容项
     * @return
     */
    @GetMapping("/itemExtendType")
    public ResultDataModel getItemExtendType() {
        return ResultDataModel.handleSuccessResult(this.classManageService.listItemExtendType());
    }

    /**
     * 获取项目需要创建的新班级预填信息
     * @param itemId
     * @return
     */
    @GetMapping("/newClassInfo")
    public ResultDataModel getNewClassInfo(@RequestParam("itemId") String itemId) {
        return ResultDataModel.handleSuccessResult(this.classManageService.getNewClassInfo(itemId));
    }

    @Override
    public GridService<PeClass> getGridService() {
        return this.classManageService;
    }
}
