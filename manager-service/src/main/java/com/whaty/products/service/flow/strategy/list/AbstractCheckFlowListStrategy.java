package com.whaty.products.service.flow.strategy.list;

import java.util.Map;

/**
 * 抽象的审核流程策略
 *
 * @author weipengsen
 */
public abstract class AbstractCheckFlowListStrategy {

    /**
     * 列举审核流程
     *
     * @param checkStatus
     * @param needCheck
     * @param flowType
     * @param search
     * @param page
     * @return
     */
    public abstract Map<String,Object> listCheckFlow(String checkStatus, String needCheck, String flowType,
                                                     String search, Map<String, Object> page);

    /**
     * 计算分页
     * @param page
     * @param count
     * @return
     */
    protected Map<String, Object> countPage(Map<String, Object> page, Integer count) {
        Integer pageSize = (Integer) page.get("pageSize");
        page.put("totalNumber", count);
        Integer totalPage = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        page.put("totalPage", totalPage);
        Integer currentPage = (Integer) page.get("currentPage");
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        currentPage = currentPage < 1 ? 1 : currentPage;
        page.put("currentPage", currentPage);
        return page;
    }

    /**
     * 生成分页limit语句
     * @param page
     * @return
     */
    protected String generatePageLimit(Map<String, Object> page) {
        Integer currentPage = (Integer) page.get("currentPage");
        currentPage = currentPage <= 0 ? 1 : currentPage;
        return " LIMIT " + ((currentPage - 1) * (Integer) page.get("pageSize")) + ", " + page.get("pageSize");
    }
}
