package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.StudentEnrollDetailServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学生注册信息管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/studentEnrollDetail")
public class StudentEnrollDetailController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "studentEnrollDetailService")
    private StudentEnrollDetailServiceImpl studentEnrollDetailService;

    @Override
    public GridService<PeStudent> getGridService() {
        return this.studentEnrollDetailService;
    }
}
