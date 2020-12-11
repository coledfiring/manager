package com.whaty.core.framework.service.guide;

import java.util.List;
import java.util.Map;

/**
 * 操作指导服务类接口
 * @author weipengsen
 */
public interface OperateGuideService {

    /**
     * 获取所有的可以查看的操作指导描述
     * @return
     */
    List<Map<String,Object>> listOperateGuideDescriptionCanShow();

    /**
     * 修改描述图标
     * @param ids
     * @param icon
     */
    void doSetOperateGuideIcon(String ids, String icon);
}
