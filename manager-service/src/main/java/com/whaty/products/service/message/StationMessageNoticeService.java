package com.whaty.products.service.message;

import java.util.List;
import java.util.Map;

/**
 * 站内信推送信息获取服务类接口
 *
 * @author weipengsen
 */
public interface StationMessageNoticeService {

    /**
     * 获取站内信组信息
     * @param groupId
     * @return
     */
    Map<String, Object> getMessageGroupInfoById(String groupId);

    /**
     * 列举信息组
     * @return
     */
    List<Map<String, Object>> listMessageGroup();

    /**
     * 保存信息模板
     * @param params
     */
    void saveMessageTemplate(Map<String, Object> params);

    /**
     * 删除模板
     * @param templateId
     */
    void deleteMessageTemplate(String templateId);

    /**
     * 获取站内信组信息
     * @param groupCode
     * @return
     */
    Map<String,Object> getMessageGroupInfoByCode(String groupCode);
}
