package com.whaty.products.service.message;

import java.util.List;
import java.util.Map;

/**
 * 信息通知服务类接口
 *
 * @author weipengsen
 */
public interface MessageNoticeService {

    /**
     * 信息通知
     * @param params
     * @throws Exception
     */
    void notice(Map<String, Object> params) throws Exception;

    /**
     * 消息通知
     * @param params
     * @param siteCode
     * @throws Exception
     */
    void notice(Map<String, Object> params, String siteCode) throws Exception;

    /**
     * 获取过滤后的id
     * @param params
     * @return
     */
    List<String> getFilterIds(Map<String, Object> params);
}
