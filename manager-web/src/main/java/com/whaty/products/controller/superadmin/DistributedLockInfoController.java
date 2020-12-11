package com.whaty.products.controller.superadmin;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.superadmin.DistributedLockInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分布式锁信息
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/superAdmin/distributedLockInfo")
public class DistributedLockInfoController {

    @Resource(name = "distributedLockInfoService")
    private DistributedLockInfoService distributedLockInfoService;

    /**
     * 列举锁信息
     * @return
     */
    @GetMapping("/lockInfo")
    public ResultDataModel listLockInfo(@RequestParam("key") String key, @RequestParam("url") String url,
                                        @RequestParam("siteCode") String siteCode) {
        return ResultDataModel.handleSuccessResult(this.distributedLockInfoService.listLockInfo(key, url, siteCode));
    }

    /**
     * 移除锁信息
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/removeLock")
    public ResultDataModel removeLock(@RequestBody ParamsDataModel paramsDataModel) {
        List<String> keys = (List<String>) paramsDataModel.getParameter("keys");
        this.distributedLockInfoService.removeLock(keys);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 列举锁等待信息
     * @return
     */
    @GetMapping("/waitLockInfo")
    public ResultDataModel listWaitLockInfo(@RequestParam("key") String key, @RequestParam("url") String url,
                                        @RequestParam("siteCode") String siteCode) {
        return ResultDataModel.handleSuccessResult(this.distributedLockInfoService
                .listWaitLockInfo(key, url, siteCode));
    }

}
