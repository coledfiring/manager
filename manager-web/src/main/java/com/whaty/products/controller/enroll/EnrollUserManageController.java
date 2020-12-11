package com.whaty.products.controller.enroll;

import com.whaty.domain.bean.enroll.EnrollUser;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报名系统用户管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/enrollUserManage")
public class EnrollUserManageController extends TycjGridBaseControllerAdapter<EnrollUser> {

}
