package com.whaty.products.service.message.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.factory.MessageNoticeFactory;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 信息通知服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("messageNoticeServiceImpl")
public class MessageNoticeServiceImpl implements MessageNoticeService {

    @Override
    @LogAndNotice("发送消息")
    public void notice(Map<String, Object> params) throws Exception {
        if (!params.containsKey(MessageConstants.PARAM_MESSAGE_TYPE)) {
            throw new ParameterIllegalException();
        }
        this.addExtraParams(params);
        AbstractNoticeStrategy strategy = MessageNoticeFactory.newInstance((String) params.get("messageType"));
        strategy.notice(params);
    }

    /**
     * 添加额外参数
     * @param params
     */
    private void addExtraParams(Map<String, Object> params) {
        params.put("basePath", CommonUtils.getBasicUrl());
    }

    @Override
    public void notice(Map<String, Object> params, String siteCode) throws Exception {
        if (!params.containsKey(MessageConstants.PARAM_MESSAGE_TYPE)) {
            throw new ParameterIllegalException();
        }
        params.put(MessageConstants.PARAM_SITE_CODE, siteCode);
        AbstractNoticeStrategy strategy = MessageNoticeFactory
                .newInstance((String) params.get(MessageConstants.PARAM_MESSAGE_TYPE));
        strategy.notice(params);
    }

    @Override
    public List<String> getFilterIds(Map<String, Object> params) {
        AbstractNoticeStrategy strategy = MessageNoticeFactory.newInstance((String) params.get("messageType"));
        return strategy.getFilterIds(params);
    }

}
