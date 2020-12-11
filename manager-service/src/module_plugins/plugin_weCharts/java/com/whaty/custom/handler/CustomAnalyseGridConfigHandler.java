package com.whaty.custom.handler;

import com.whaty.custom.bean.CustomAnalyseGroup;
import com.whaty.custom.constant.AnalyseConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.core.framework.bean.GridColumnConfig;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.sqlflow.SqlFlowHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 自定义统计grid配置处理类
 *
 * @author weipengsen
 */
public class CustomAnalyseGridConfigHandler {
    /**
     * 自定义统计组
     */
    private CustomAnalyseGroup group;
    /**
     * 统计名称
     */
    private String name;
    /**
     * 自定义统计的维度和统计项等配置
     */
    private Map<String, Object> charts;
    /**
     * 条件配置
     */
    private List<Map<String, Object>> conditions;
    /**
     * grid服务类
     */
    private GridService gridService;

    private GeneralDao generalDao;

    /**
     * 根据配置生成grid的构造器
     *
     * @param name
     * @param group
     * @param charts
     * @param conditions
     */
    public CustomAnalyseGridConfigHandler(String name, CustomAnalyseGroup group, Map<String, Object> charts,
                                          List<Map<String, Object>> conditions) {
        this.name = name;
        this.group = group;
        this.charts = charts;
        this.conditions = conditions;
    }

    /**
     * 解析为gridConfig用于预览
     *
     * @return
     */
    public Map<String, Object> handle() throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> config = this.handleConfigToGridBasicConfig();
        GridConfig gridConfig = new GridConfig((GridBasicConfig) config.get(AnalyseConstant.ARG_GRID_CONFIG),
                (List<GridColumnConfig>) config.get(AnalyseConstant.ARG_GRID_COLUMN_CONFIG),
                new ArrayList<>(), new HashMap<>(2));
        Page page = this.initPage();
        page = this.getGridService().list(page, gridConfig, null);
        Map<String, Object> gridMap = new HashMap<>(4);
        gridMap.put(AnalyseConstant.RESULT_PAGE, page);
        gridMap.put(AnalyseConstant.ARG_GRID_CONFIG, gridConfig);
        return gridMap;
    }

    /**
     * 初始化
     *
     * @return
     */
    private Page initPage() {
        Page page = new Page();
        page.setCurPage(1);
        page.setPageSize(20);
        return page;
    }

    /**
     * 将配置转化为grid基础配置
     *
     * @return
     */
    public Map<String, Object> handleConfigToGridBasicConfig() throws InvocationTargetException,
            IllegalAccessException {
        // 拼接sql
        String sql = this.group.getGridSql();
        Map<String, Object> params = new HashMap<>(4);
        params.put(AnalyseConstant.PARAM_CHARTS, this.charts);
        params.put(AnalyseConstant.PARAM_CONDITIONS, this.conditions);
        SqlFlowHandler handler = new SqlFlowHandler(sql, params);
        handler.handle();
        sql = handler.getSql();
        GridBasicConfig config = this.generateGridBasicConfig(sql);
        // grid的行拼接
        List<GridColumnConfig> columns = this.generateColumn(config);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put(AnalyseConstant.ARG_GRID_CONFIG, config);
        resultMap.put(AnalyseConstant.ARG_GRID_COLUMN_CONFIG, columns);
        return resultMap;
    }

    /**
     * 生成grid列配置
     *
     * @param grid
     * @return
     */
    private List<GridColumnConfig> generateColumn(GridBasicConfig grid) {
        List<GridColumnConfig> columns = new ArrayList<>();
        // 维度
        List<Map<String, String>> xGroupList = (List<Map<String, String>>) this.charts
                .get(AnalyseConstant.PARAM_X_GROUP);
        long index = 1;
        for (Map<String, String> xGroup : xGroupList) {
            columns.add(this.generateColumn(xGroup.get(AnalyseConstant.PARAM_NAME),
                    xGroup.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX), grid, "1",
                    xGroup.containsKey(AnalyseConstant.PARAM_TYPE) ? xGroup.get(AnalyseConstant.PARAM_TYPE) :
                            "TextField", index));
            index++;
        }
        // 统计项
        Map<String, Object> analyses = (Map<String, Object>) this.charts.get(AnalyseConstant.PARAM_ANALYSES);
        columns.add(this.generateColumn((String) analyses.get(AnalyseConstant.PARAM_NAME),
                (String) analyses.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX), grid, "1",
                analyses.containsKey(AnalyseConstant.PARAM_TYPE) ? (String) analyses.get(AnalyseConstant.PARAM_TYPE) :
                        "TextField", index ++));
        // 统计数量
        columns.add(this.generateColumn("统计数量", "number", grid, "0", "TextField", index));
        return columns;
    }

    /**
     * 生成grid列配置
     *
     * @param name
     * @param dataIndex
     * @param grid
     * @return
     */
    private GridColumnConfig generateColumn(String name, String dataIndex,
                                            GridBasicConfig grid, String canSearch,
                                            String type, long serialNumber) {
        GridColumnConfig config = new GridColumnConfig();
        config.setGridBasicConfig(grid);
        config.setName(name);
        config.setDataIndex(dataIndex);
        config.setSearch(canSearch);
        config.setList("1");
        config.setEnumConstByFlagActive(this.getGeneralDao()
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1"));
        config.setMaxLength(25L);
        config.setType(type);
        config.setSerialNumber(serialNumber);
        return config;
    }

    /**
     * 生成grid列表配置
     *
     * @param sql
     * @return
     */
    private GridBasicConfig generateGridBasicConfig(String sql) {
        GridBasicConfig config = new GridBasicConfig();
        config.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        config.setSql(sql);
        config.setCanUpdate("1");
        config.setCanSearch("1");
        config.setCanDelete("1");
        config.setCanBatchAdd("1");
        config.setCanAdd("1");
        config.setPeWebSite(SiteUtil.getSite());
        config.setDateFormat("yyyy-MM-dd");
        config.setEnumConstByFlagActive(this.getGeneralDao()
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1"));
        config.setTitle(this.name);
        config.setListType("1");
        return config;
    }

    public GeneralDao getGeneralDao() {
        if (this.generalDao == null) {
            this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
        }
        return generalDao;
    }

    public GridService getGridService() {
        if (this.gridService == null) {
            this.gridService = (GridService) SpringUtil.getBean("gridServiceImpl");
        }
        return this.gridService;
    }

    public CustomAnalyseGroup getGroup() {
        return group;
    }

    public void setGroup(CustomAnalyseGroup group) {
        this.group = group;
    }

    public Map<String, Object> getCharts() {
        return charts;
    }

    public void setCharts(Map<String, Object> charts) {
        this.charts = charts;
    }

    public List<Map<String, Object>> getConditions() {
        return conditions;
    }

    public void setConditions(List<Map<String, Object>> conditions) {
        this.conditions = conditions;
    }
}
