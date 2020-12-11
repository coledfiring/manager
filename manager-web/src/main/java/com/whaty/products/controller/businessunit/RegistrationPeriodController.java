package com.whaty.products.controller.businessunit;

import com.whaty.domain.bean.PeClass;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 培训期次管理
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/registrationPeriod")
public class RegistrationPeriodController extends TycjGridBaseControllerAdapter<PeClass> {

}
