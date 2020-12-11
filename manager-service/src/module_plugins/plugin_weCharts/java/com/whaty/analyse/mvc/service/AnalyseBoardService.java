package com.whaty.analyse.mvc.service;

import com.whaty.analyse.framework.domain.AnalyseBoardConfigVO;

import java.util.Map;

/**
 * 统计看板
 *
 * @author weipengsen
 */
public interface AnalyseBoardService {

    /**
     * 获取看板
     * @param id
     * @param params
     * @return
     */
    AnalyseBoardConfigVO getBoard(String id, Map<String, Object> params);
}
