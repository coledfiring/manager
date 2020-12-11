package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ExamSite;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.ExamSiteServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 考点管理controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/examSite")
public class ExamSiteController extends TycjGridBaseControllerAdapter<ExamSite> {

    @Resource(name = "examSiteService")
    private ExamSiteServiceImpl examSiteService;

    @Override
    public GridService<ExamSite> getGridService() {
        return this.examSiteService;
    }
}
