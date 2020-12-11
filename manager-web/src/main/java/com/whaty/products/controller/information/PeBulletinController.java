package com.whaty.products.controller.information;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeBulletin;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.information.impl.PeBulletinServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 公告列表controller
 * @author weipengsen
 */
@Lazy
@Controller("peBulletinController")
@RequestMapping("/entity/information/peBulletin")
public class PeBulletinController extends TycjGridBaseControllerAdapter<PeBulletin> {

    @Resource(name = "peBulletinService")
    private PeBulletinServiceImpl peBulletinService;

    @Override
    public GridService<PeBulletin> getGridService() {
        return this.peBulletinService;
    }

}
