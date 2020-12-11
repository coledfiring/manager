package com.whaty.products.controller.oltrain.onlineexam;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlClassOnlineExam;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.onlineexam.impl.OLClassOnlineExamManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级在线考试管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/olClassOnlineExamManage")
@BasicOperateRecord("班级在线考试管理")
public class OLClassOnlineExamManageController extends TycjGridBaseControllerAdapter<OlClassOnlineExam> {

    @Resource(name = "olClassOnlineExamManageService")
    private OLClassOnlineExamManageServiceImpl olClassOnlineExamManageService;

    @Override
    public GridService<OlClassOnlineExam> getGridService() {
        return this.olClassOnlineExamManageService;
    }
}
