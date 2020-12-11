package com.whaty.products.controller.onlineexam;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassOnlineExam;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.onlineexam.ClassOnlineExamManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级在线考试管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/onlineExam/classOnlineExamManage")
public class ClassOnlineExamManageController extends TycjGridBaseControllerAdapter<ClassOnlineExam> {

    @Resource(name = "classOnlineExamManageService")
    private ClassOnlineExamManageServiceImpl classOnlineExamManageService;

    @Override
    public GridService<ClassOnlineExam> getGridService() {
        return this.classOnlineExamManageService;
    }
}
