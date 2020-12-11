package com.whaty.analyse.mvc.service;

import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;

/**
 * 统计块显示
 *
 * @author weipengsen
 */
public interface AnalyseBlockService {

    /**
     * 获取统计块
     * @param id
     * @return
     */
    AnalyseBlockConfig getBlock(String id);
}
