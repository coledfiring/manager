package com.whaty.products.controller.superadmin;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.superadmin.impl.RedisCacheManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * redis缓存管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("redisCacheManageController")
@RequestMapping("/superAdmin/redisCache")
public class RedisCacheManageController extends TycjGridBaseControllerAdapter {

    @Resource(name = "redisCacheManageService")
    private RedisCacheManageServiceImpl redisCacheManageService;

    /**
     * 搜索缓存
     * @return
     */
    @RequestMapping("/searchCache")
    public ResultDataModel searchCache(@RequestParam("cacheKey") String cacheKey) {
        return ResultDataModel.handleSuccessResult(this.redisCacheManageService.searchCache(cacheKey));
    }

    /**
     * 删除缓存
     * @param paramsData
     * @return
     */
    @RequestMapping("/removeCache")
    public ResultDataModel removeCache(@RequestBody ParamsDataModel paramsData) {
        this.redisCacheManageService.removeCache((List<String>) paramsData.getParameter("keys"));
        return ResultDataModel.handleSuccessResult("删除成功");
    }
}
