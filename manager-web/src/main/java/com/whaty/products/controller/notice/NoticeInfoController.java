package com.whaty.products.controller.notice;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 通知推送controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("noticeInfoController")
@RequestMapping("/notice/noticeInfo")
public class NoticeInfoController {

    /**
     * 从请求中加载需要的参数
     *
     * @return
     */
    private Map<String, Object> collectParams() {
        Map<String, Object> params = new HashMap<>(7);
        params.putAll(Optional.ofNullable(CommonUtils.getRequest())
                .map(RequestUtils::getRequestMap).orElse(new HashMap<>(2)));
        //当前用户
        params.put(NoticeServerPollConstant.NOTICE_PARAM_USER_ID, UserUtils.getCurrentUserId());
        //|当前站点用户对应的cachekey
        params.put(NoticeServerPollConstant.NOTICE_PARAM_CACHE_KEY,
                String.format(NoticeServerPollConstant.NOTICE_USER_KEY,
                        SiteUtil.getSiteCode(), UserUtils.getCurrentUserId()));
        return params;
    }

    /**
     * 获取通知配置
     * @return
     */
    @GetMapping("/noticeConfig")
    public ResultDataModel getNoticeConfig() {
        return ResultDataModel.handleSuccessResult(PlatformConfigUtil
                .getPlatformConfig().getOpenNoticePoll());
    }

    /**
     * 获取通知信息
     *
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public ResultDataModel getNoticeInfo() {
        return ResultDataModel.handleSuccessResult(NoticeServerPollUtils.startListenNotice(collectParams()));
    }

    /**
     * 停止指定注册号的进程
     *
     * @return
     */
    @RequestMapping(value = "/killCurrentNotice", method = RequestMethod.GET)
    public ResultDataModel killCurrentNotice(
            @RequestParam(NoticeServerPollConstant.NOTICE_PARAM_REGISTER_ID) String registerId) {
        NoticeServerPollUtils.killRegisterThread(registerId);
        return ResultDataModel.handleSuccessResult("ok");
    }

    /**
     * 设置通知已读
     *
     * @return
     * @author weipengsen
     */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public ResultDataModel setSingleRead(@RequestBody ParamsDataModel paramsDataModel) {
        String noticeId = paramsDataModel.getStringParameter(NoticeServerPollConstant.NOTICE_PARAM_NOTICE_ID);
        TycjParameterAssert.isAllNotBlank(noticeId);
        NoticeServerPollUtils.singleRead(noticeId, UserUtils.getCurrentUserId());
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    /**
     * 设置通知全部已读
     *
     * @return
     * @author weipengsen
     */
    @RequestMapping(value = "/allRead", method = RequestMethod.POST)
    public ResultDataModel setAllRead() {
        NoticeServerPollUtils.allRead(UserUtils.getCurrentUserId());
        return ResultDataModel.handleSuccessResult("设置全部已读成功");
    }

    /**
     * 删除所有已读未星标通知
     *
     * @return
     * @author weipengsen
     */
    @RequestMapping(value = "/readNoStarNotice", method = RequestMethod.POST)
    public ResultDataModel deleteAllReadNoStarNotice() {
        NoticeServerPollUtils.deleteAllReadNoStarNotice(UserUtils.getCurrentUserId());
        return ResultDataModel.handleSuccessResult("删除成功");
    }

    /**
     * 通知信息取消星标或设置星标
     *
     * @return
     */
    @RequestMapping(value = "/starNotice", method = RequestMethod.POST)
    public ResultDataModel setStar(@RequestBody ParamsDataModel paramsDataModel) {
        String noticeId = paramsDataModel.getStringParameter(NoticeServerPollConstant.NOTICE_PARAM_NOTICE_ID);
        TycjParameterAssert.isAllNotBlank(noticeId);
        NoticeServerPollUtils.setStar(UserUtils.getCurrentUserId(), noticeId);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 删除通知
     *
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.POST)
    public ResultDataModel deleteNotice(@RequestBody ParamsDataModel paramsDataModel) {
        String noticeId = paramsDataModel.getStringParameter(NoticeServerPollConstant.NOTICE_PARAM_NOTICE_ID);
        TycjParameterAssert.isAllNotBlank(noticeId);
        NoticeServerPollUtils.delNotice(UserUtils.getCurrentUserId(), noticeId);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

}
