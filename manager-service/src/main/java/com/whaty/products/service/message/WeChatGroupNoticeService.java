package com.whaty.products.service.message;

import java.util.List;
import java.util.Map;

/**
 * 微信群发通知服务接口类
 *
 * @author weipengsen
 */
public interface WeChatGroupNoticeService {

    /**
     * 列举模板
     * @return
     */
    List<Map<String,Object>> listTemplates();

    /**
     * 保存模板
     * @param name
     * @param content
     */
    void saveTemplate(String name, String content);

    /**
     * 修改模板
     * @param id
     * @param name
     * @param content
     */
    void updateTemplate(String id, String name, String content);

    /**
     * 删除模板
     * @param id
     */
    void deleteTemplate(String id);
}
