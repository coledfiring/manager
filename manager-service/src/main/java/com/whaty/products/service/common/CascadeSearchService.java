package com.whaty.products.service.common;

import java.util.List;
import java.util.Map;

/**
 * 通用级联搜索服务类
 * @author weipengsen
 */
public interface CascadeSearchService {

    /**
     * 级联查询班级
     * @param params
     * @return
     */
    List<Object[]> listCascadeClass(Map<String, String> params);

}
