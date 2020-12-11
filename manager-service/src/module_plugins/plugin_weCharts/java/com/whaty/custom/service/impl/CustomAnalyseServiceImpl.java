package com.whaty.custom.service.impl;

import com.whaty.custom.bean.CustomAnalyse;
import com.whaty.custom.bean.CustomAnalyseConditionConfig;
import com.whaty.custom.bean.CustomAnalyseGroup;
import com.whaty.custom.bean.CustomAnalyseItemConfig;
import com.whaty.custom.bean.CustomAnalyseXGroupConfig;
import com.whaty.custom.handler.CustomAnalyseChartsConfigHandler;
import com.whaty.custom.handler.CustomAnalyseGridConfigHandler;
import com.whaty.custom.helper.CustomAnalyseHelper;
import com.whaty.custom.service.CustomAnalyseService;
import com.whaty.domain.bean.SsoUser;
import com.whaty.constant.SiteConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.cache.service.CacheService;
import com.whaty.core.commons.cache.util.CacheUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.util.JsonUtil;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.core.framework.bean.GridColumnConfig;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.custom.constant.AnalyseChartsTypeEnum;
import com.whaty.custom.constant.AnalyseConditionEnum;
import com.whaty.custom.constant.AnalyseConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.jsonbean.Option;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 自定义统计图表服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("customAnalyseService")
public class CustomAnalyseServiceImpl implements CustomAnalyseService {

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = "core_cacheService")
    private CacheService cacheService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public Map<String, Object> getCustomAnalyseConfig(String analyseGroupId, String analyseId) {
        if (StringUtils.isBlank(analyseGroupId) && StringUtils.isBlank(analyseId)) {
            throw new ParameterIllegalException();
        }
        CustomAnalyseGroup group;
        CustomAnalyse analyse = null;
        if (StringUtils.isNotBlank(analyseId)) {
            analyse = this.generalDao.getById(CustomAnalyse.class, analyseId);
            group = this.getCustomAnalyseGroup(analyse.getCustomAnalyseGroup().getId(), true);
        } else {
            group = this.getCustomAnalyseGroup(analyseGroupId, true);
        }
        return this.generateCustomAnalyseConfig(group, analyse);
    }

    /**
     * 生成自定义统计配置
     *
     * @param group
     * @param analyse
     * @return
     */
    private Map<String, Object> generateCustomAnalyseConfig(CustomAnalyseGroup group, CustomAnalyse analyse) {
        Map<String, Object> analyseConfig = new HashMap<>(16);
        JSONObject config = null;
        if (analyse != null) {
            config = JSONObject.fromObject(analyse.getCustomAnalyseConfig());
            if (StringUtils.isNotBlank(analyse.getCanViewRoleCode())) {
                String viewRoleCode = analyse.getCanViewRoleCode()
                        .substring(analyse.getCanViewRoleCode().indexOf(CommonConstant.SPLIT_ID_SIGN) + 1);
                List<String> viewRoleCodes = Arrays.asList(viewRoleCode
                        .substring(0, viewRoleCode.lastIndexOf(CommonConstant.SPLIT_ID_SIGN))
                        .split(CommonConstant.SPLIT_ID_SIGN));
                analyseConfig.put(AnalyseConstant.PARAM_VIEW_ROLE_CODE, viewRoleCodes);
            }
            analyseConfig.put(AnalyseConstant.PARAM_VIEW_LEVEL_CODE,
                    analyse.getEnumConstByFlagAnalyseViewLevel().getCode());
        }
        if (config != null) {
            analyseConfig.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_CHARTS_CODE,
                    config.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED_CHARTS));
            analyseConfig.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_NAME, analyse.getName());
        }
        Map<String, Object> analyseGroup = new HashMap<>(16);
        analyseConfig.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ANALYSE_GROUP, analyseGroup);
        analyseGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID, group.getId());
        analyseGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_NAME, group.getName());
        // 生成统计维度
        List<Map<String, Object>> xGroups = new ArrayList<>();
        for (CustomAnalyseXGroupConfig customAnalyseXGroupConfig : group.getCustomAnalyseXGroupConfigList()) {
            Map<String, Object> xGroup = new HashMap<>(16);
            xGroups.add(xGroup);
            xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID, customAnalyseXGroupConfig.getId());
            xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_NAME, customAnalyseXGroupConfig.getName());
            xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_VALUE, customAnalyseXGroupConfig.getValue());
            xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX, customAnalyseXGroupConfig.getDataIndex());
            if (config != null
                    && ((List) config.get(AnalyseConstant.PARAM_X_GROUP))
                    .contains(customAnalyseXGroupConfig.getId())) {
                xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED, true);
            } else {
                xGroup.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED, false);
            }
        }
        analyseGroup.put(AnalyseConstant.PARAM_X_GROUP, xGroups);
        // 生成统计项
        List<Map<String, Object>> analyses = new ArrayList<>();
        for (CustomAnalyseItemConfig itemConfig : group.getCustomAnalyseItemConfigList()) {
            Map<String, Object> item = new HashMap<>(16);
            analyses.add(item);
            item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID, itemConfig.getId());
            item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_NAME, itemConfig.getName());
            item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_CODE, itemConfig.getCode());
            item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX, itemConfig.getDataIndex());
            if (config != null
                    && config.get(AnalyseConstant.PARAM_ANALYSES).equals(itemConfig.getId())) {
                item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED, true);
            } else {
                item.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED, false);
            }
        }
        analyseGroup.put(AnalyseConstant.PARAM_ANALYSES, analyses);
        // 生成筛选条件
        List<Map<String, Object>> conditions = new ArrayList<>();
        for (CustomAnalyseConditionConfig conditionConfig : group.getCustomAnalyseConditionConfigList()) {
            Map<String, Object> condition = new HashMap<>(16);
            conditions.add(condition);
            condition.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID, conditionConfig.getId());
            condition.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_NAME, conditionConfig.getName());
            condition.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_TYPE, conditionConfig.getType());
            condition.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_KEY, conditionConfig.getKey());
            condition.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_HELPER, conditionConfig.getHelper());
            // 判断类别生成值
            switch (conditionConfig.getType()) {
                case multiSelect:
                    List<Map<String, Object>> options = this.generalDao
                            .getMapBySQL(conditionConfig.getOptionSql());
                    condition.put(AnalyseConstant.CONDITION_VALUE, options);
                    for (Map<String, Object> option : options) {
                        String id = String.valueOf(option.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID));
                        option.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID, id);
                        option.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED, false);
                        if (config != null) {
                            for (Map<String, Object> value : (List<Map<String, Object>>) config
                                    .get(AnalyseConstant.PARAM_CONDITIONS)) {
                                if (value.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID).equals(conditionConfig.getId())) {
                                    option.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED,
                                            ((List) value.get(AnalyseConstant.CONDITION_VALUE)).contains(id));
                                }
                            }
                        }
                    }
                    break;
                case input:
                    condition.put(AnalyseConstant.CONDITION_VALUE, "");
                    if (config != null) {
                        for (Map<String, Object> value : (List<Map<String, Object>>) config
                                .get(AnalyseConstant.PARAM_CONDITIONS)) {
                            if (value.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID).equals(conditionConfig.getId())) {
                                condition.put(AnalyseConstant.CONDITION_VALUE,
                                        value.get(AnalyseConstant.CONDITION_VALUE));
                            }
                        }
                    }
                    break;
                case singleSelect:
                    options = this.generalDao.getMapBySQL(conditionConfig.getOptionSql());
                    condition.put(AnalyseConstant.CONDITION_OPTION, options);
                    condition.put(AnalyseConstant.CONDITION_VALUE, "");
                    if (config != null) {
                        for (Map<String, Object> value : (List<Map<String, Object>>) config
                                .get(AnalyseConstant.PARAM_CONDITIONS)) {
                            if (value.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ID).equals(conditionConfig.getId())) {
                                condition.put(AnalyseConstant.CONDITION_VALUE,
                                        value.get(AnalyseConstant.CONDITION_VALUE));
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unknown condition type '"
                            + conditionConfig.getType().getCode() + "'");
            }
        }
        analyseConfig.put(AnalyseConstant.PARAM_CONDITIONS, conditions);
        return analyseConfig;
    }

    @Override
    public Map<String, Object> getCustomAnalyseToPreviewChartsConfig(String analyseGroupId, String name, String chartsType,
                                                         Map<String, Object> charts,
                                                         List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException {
        if (StringUtils.isBlank(analyseGroupId) || StringUtils.isBlank(name) || StringUtils.isBlank(chartsType)
                || AnalyseChartsTypeEnum.getByCode(chartsType) == null || MapUtils.isEmpty(charts)) {
            throw new ParameterIllegalException();
        }
        // 拿出自定义组配置
        CustomAnalyseGroup group = this.getCustomAnalyseGroup(analyseGroupId, false);
        if (group == null) {
            throw new ServiceException("此自定义统计配置不存在");
        }
        Map<String, Object> resultMap = new HashMap<>(4);
        conditions = this.handleConditions(conditions);
        // 把参数配置解析成charts配置对象
        CustomAnalyseChartsConfigHandler chartsHandler = new CustomAnalyseChartsConfigHandler(name, group,
                chartsType,
                charts, conditions);
        Option option = chartsHandler.handle();
        resultMap.put(AnalyseConstant.RESULT_OPTION, option);
        return resultMap;
    }

    @Override
    public Map<String, Object> getCustomAnalyseToPreviewGridConfig(String analyseGroupId, String name, String chartsType,
                                                         Map<String, Object> charts,
                                                         List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException {
        if (StringUtils.isBlank(analyseGroupId) || StringUtils.isBlank(name) || StringUtils.isBlank(chartsType)
                || AnalyseChartsTypeEnum.getByCode(chartsType) == null || MapUtils.isEmpty(charts)) {
            throw new ParameterIllegalException();
        }
        // 拿出自定义组配置
        CustomAnalyseGroup group = this.getCustomAnalyseGroup(analyseGroupId, false);
        if (group == null) {
            throw new ServiceException("此自定义统计配置不存在");
        }
        Map<String, Object> resultMap = new HashMap<>(4);
        conditions = this.handleConditions(conditions);
        // 把参数配置解析成grid配置对象
        CustomAnalyseGridConfigHandler gridHandler = new CustomAnalyseGridConfigHandler(name, group, charts,
                conditions);
        Map<String, Object> gridMap = gridHandler.handle();
        resultMap.put(AnalyseConstant.RESULT_PAGE, gridMap.get(AnalyseConstant.RESULT_PAGE));
        resultMap.put(AnalyseConstant.ARG_GRID_CONFIG, gridMap.get(AnalyseConstant.ARG_GRID_CONFIG));
        return resultMap;
    }

    /**
     * 将conditions处理为需要的格式
     *
     * @param conditions
     * @return
     */
    private List<Map<String, Object>> handleConditions(List<Map<String, Object>> conditions) {
        List<Integer> needDeleteCondition = new LinkedList<>();
        int conditionIndex = 0;
        for (Map<String, Object> condition : conditions) {
            String type = (String) condition.get(AnalyseConstant.CONDITION_ARG_TYPE);
            switch (AnalyseConditionEnum.getByCode(type)) {
                case multiSelect:
                    boolean needDelete = true;
                    List<Map<String, Object>> options = (List<Map<String, Object>>) condition
                            .get(AnalyseConstant.CONDITION_VALUE);
                    List<Object> values = new ArrayList<>();
                    for (Map<String, Object> option : options) {
                        boolean selected = (Boolean) option.get(AnalyseConstant.CONDITION_MULTI_OPTION_SELECTED);
                        if (selected) {
                            needDelete = false;
                            values.add(option.get(AnalyseConstant.CONDITION_MULTI_OPTION_ID));
                        }
                    }
                    if (needDelete) {
                        needDeleteCondition.add(conditionIndex);
                    } else {
                        condition.put(AnalyseConstant.CONDITION_VALUE, values);
                    }
                    break;
                case input:
                case singleSelect:
                    String value = (String) condition.get(AnalyseConstant.CONDITION_VALUE);
                    if (StringUtils.isBlank(value)) {
                        needDeleteCondition.add(conditionIndex);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unknown condition type '" + type + "'");
            }
            conditionIndex++;
        }
        if (CollectionUtils.isNotEmpty(needDeleteCondition)) {
            for (int i = needDeleteCondition.size() - 1; i >= 0; i--) {
                conditions.remove(needDeleteCondition.get(i).intValue());
            }
        }
        return conditions;
    }

    /**
     * 查询出自定义统计组
     *
     * @param analyseGroupId
     * @param searchAll
     * @return
     */
    public CustomAnalyseGroup getCustomAnalyseGroup(String analyseGroupId, boolean searchAll) {
        CustomAnalyseGroup group = this.generalDao.getById(CustomAnalyseGroup.class, analyseGroupId);
        if (searchAll && group != null) {
            List<CustomAnalyseXGroupConfig> customAnalyseXGroupConfigs = this.generalDao
                    .getByHQL("from CustomAnalyseXGroupConfig where customAnalyseGroup.id = '" + analyseGroupId + "'");
            group.setCustomAnalyseXGroupConfigList(customAnalyseXGroupConfigs);
            List<CustomAnalyseItemConfig> customAnalyseItemConfigs = this.generalDao
                    .getByHQL("from CustomAnalyseItemConfig where customAnalyseGroup.id = '" + analyseGroupId + "'");
            group.setCustomAnalyseItemConfigList(customAnalyseItemConfigs);
            List<CustomAnalyseConditionConfig> customAnalyseConditionConfigs = this.generalDao
                    .getByHQL("from CustomAnalyseConditionConfig where customAnalyseGroup.id = '"
                            + analyseGroupId + "'");
            group.setCustomAnalyseConditionConfigList(customAnalyseConditionConfigs);
        }
        return group;
    }

    @Override
    public String saveCustomAnalyse(String analyseId, String name, String analyseGroupId,
                                    String chartsType, Map<String, Object> viewLevel, Map<String, Object> charts,
                                    List<Map<String, Object>> conditions)
            throws NoSuchMethodException, WeChartsServiceException, IllegalAccessException, InvocationTargetException {
        if (StringUtils.isBlank(analyseId)) {
            return this.saveCustomAnalyse(analyseGroupId, name, chartsType, viewLevel, charts, conditions);
        } else {
            return this.updateCustomAnalyse(analyseId, name, chartsType, viewLevel, charts, conditions);
        }
    }

    @Override
    public Map<String, Object> getCustomAnalyse(String analyseId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append(" 	an.fk_grid_id as actionId,                                ");
        sql.append(" 	ch.code as chartsCode                                     ");
        sql.append(" FROM                                                         ");
        sql.append(" 	custom_analyse an                                         ");
        sql.append(" INNER JOIN pe_chart_def ch ON ch.id = an.fk_chart_def_id     ");
        sql.append(" where                                                        ");
        sql.append(" 	an.id = '" + analyseId + "'                               ");
        List<Map<String, Object>> analyse = this.generalDao.getMapBySQL(sql.toString());
        if (CollectionUtils.isEmpty(analyse)) {
            throw new ParameterIllegalException();
        }
        return analyse.get(0);
    }

    @Override
    public Map<String, Object> getCustomAnalyseGroup(String analyseGroupId) {
        List<Map<String, Object>> groupList = this.generalDao
                .getMapBySQL("SELECT id AS id, NAME AS name FROM custom_analyse_group WHERE code = ? AND site_code = ?",
                        analyseGroupId, SiteUtil.getSiteCode());
        if (CollectionUtils.isEmpty(groupList)) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> group = groupList.get(0);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                      ");
        sql.append(" 	an.id AS analyseId,                                      ");
        sql.append(" 	an. NAME AS name,                                        ");
        sql.append(" 	ch.type AS type                                          ");
        sql.append(" FROM                                                        ");
        sql.append(" 	custom_analyse an                                        ");
        sql.append(" INNER JOIN pe_chart_def ch ON ch.id = an.fk_chart_def_id    ");
        sql.append(" INNER JOIN enum_const ac on ac.id = an.flag_active          ");
        sql.append(" WHERE                                                       ");
        sql.append(" 	an.fk_custom_analyse_group_id = '" + group.get("id") + "'");
        sql.append(" AND ac.code = '1'                                           ");
        sql.append(" AND an.fk_create_user_id = '" + this.userService.getCurrentUser().getId() + "'");
        List<Map<String, Object>> analyses = this.generalDao.getMapBySQL(sql.toString());
        // 自己的统计
        group.put("selfAnalyses", analyses);
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                          ");
        sql.append(" 	an.id AS analyseId,                                                          ");
        sql.append(" 	an. NAME AS name,                                                            ");
        sql.append(" 	ch.type AS type,                                                             ");
        sql.append(" 	if(co.id is null, '0', '1') as collection                                    ");
        sql.append(" FROM                                                                            ");
        sql.append(" 	custom_analyse an                                                            ");
        sql.append(" INNER JOIN pe_chart_def ch ON ch.id = an.fk_chart_def_id                        ");
        sql.append(" INNER JOIN enum_const vi on vi.id = an.flag_analyse_view_level                  ");
        sql.append(" INNER JOIN enum_const ac on ac.id = an.flag_active                              ");
        sql.append(" LEFT JOIN custom_analyse_user_collection co on co.fk_custom_analyse_id = an.id  ");
        sql.append(" AND co.fk_sso_user_id = '" + this.userService.getCurrentUser().getId() + "'     ");
        sql.append(" WHERE                                                                           ");
        sql.append(" 	an.fk_custom_analyse_group_id = '" + group.get("id") + "'                    ");
        sql.append(" AND ac.code = '1'                                                               ");
        sql.append(" AND (                                                                           ");
        sql.append(" 	vi. CODE = '1'                                                               ");
        sql.append(" 	OR (                                                                         ");
        sql.append(" 		vi. CODE = '3'                                                           ");
        sql.append(" 		AND an.can_view_role_code LIKE '%"
                + this.userService.getCurrentUser().getRole().getCode() + "%'                        ");
        sql.append(" 	)                                                                            ");
        sql.append(" )                                                                               ");
        // 不是自己但是可以看的统计
        analyses = this.generalDao.getMapBySQL(sql.toString());
        // 收藏的统计
        List<Map<String, Object>> collectAnalyses = new LinkedList<>();
        group.put("collectAnalyses", collectAnalyses);
        // 未收藏的统计
        List<Map<String, Object>> noCollectAnalyses = new LinkedList<>();
        group.put("noCollectAnalyses", noCollectAnalyses);
        analyses.forEach(e -> {
            if ("0".equals(e.get("collection"))) {
                noCollectAnalyses.add(e);
            } else {
                collectAnalyses.add(e);
            }
        });

        return group;
    }

    @Override
    public void deleteCustomAnalyse(String analyseId) {
        // 拿到gridId和chartId
        List<Map<String, Object>> analyseList = this.generalDao
                .getMapBySQL("select fk_grid_id as gridId,fk_chart_def_id as chartsId from custom_analyse where id = '"
                        + analyseId + "'");
        if (CollectionUtils.isEmpty(analyseList)) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> analyse = analyseList.get(0);
        // 删除analyse相关收藏
        String sql = "delete from custom_analyse_user_collection where fk_custom_analyse_id = '" + analyseId + "'";
        this.generalDao.executeBySQL(sql);
        // 删除analyse,无外键关联所以可以先删除
        this.generalDao.executeBySQL("delete from custom_analyse where id = '" + analyseId + "'");
        // 删除charts
        this.generalDao.executeBySQL("DELETE FROM pe_chart_column_def WHERE FK_CHART_DEF_ID = '"
                + analyse.get("chartsId") + "'");
        this.generalDao.executeBySQL("DELETE FROM pe_chart_def WHERE id = '" + analyse.get("chartsId") + "'");
        // 删除grid
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.openGeneralDao.executeBySQL("delete from grid_column_config where fk_action_grid_config_id = ?",
                analyse.get("gridId"));
        this.openGeneralDao.executeBySQL("delete from grid_basic_config where id = ?", analyse.get("gridId"));
    }

    @Override
    public void doCollectCustomAnalyse(String analyseId) {
        String sql = "insert into custom_analyse_user_collection(fk_custom_analyse_id, fk_sso_user_id) values('"
            + analyseId + "', '" + this.userService.getCurrentUser().getId() + "')";
        this.generalDao.executeBySQL(sql);
    }

    @Override
    public void doCancelCollectCustomAnalyse(String analyseId) {
        String sql = "delete from custom_analyse_user_collection where fk_custom_analyse_id = '"
                + analyseId + "' and fk_sso_user_id = '" + this.userService.getCurrentUser().getId() + "'";
        this.generalDao.executeBySQL(sql);
    }

    /**
     * 保存自定义统计
     *
     * @param analyseId
     * @param name
     * @param chartsType
     * @param viewLevel
     * @param charts
     * @param conditions
     * @return
     */
    private String updateCustomAnalyse(String analyseId, String name, String chartsType, Map<String, Object> viewLevel,
                                       Map<String, Object> charts, List<Map<String, Object>> conditions)
            throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(analyseId) || StringUtils.isBlank(name) || StringUtils.isBlank(chartsType)
                || AnalyseChartsTypeEnum.getByCode(chartsType) == null || MapUtils.isEmpty(charts)) {
            throw new ParameterIllegalException();
        }
        conditions = this.handleConditions(conditions);

        // 拿出自定义配置
        CustomAnalyse analyse = this.generalDao.getById(CustomAnalyse.class, analyseId);
        this.generalDao.evict(analyse);
        CustomAnalyseGroup group = analyse.getCustomAnalyseGroup();

        // 把参数配置解析成grid配置对象
        CustomAnalyseGridConfigHandler gridHandler = new CustomAnalyseGridConfigHandler(name, group, charts, conditions);
        Map<String, Object> config = gridHandler.handleConfigToGridBasicConfig();
        GridBasicConfig basicConfig = (GridBasicConfig) config.get(AnalyseConstant.ARG_GRID_CONFIG);
        List<GridColumnConfig> columns = (List<GridColumnConfig>) config.get(AnalyseConstant.ARG_GRID_COLUMN_CONFIG);
        basicConfig.setId(analyse.getGridBasicConfigId());

        // 把参数配置解析成charts配置对象
        CustomAnalyseChartsConfigHandler chartsHandler = new CustomAnalyseChartsConfigHandler(name, group, chartsType,
                charts, conditions);
        PeChartDef chart = chartsHandler.handleConfigToChartConfig();
        /*
         * 数据库操作
         */
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        GridBasicConfig oldGridConfig = null;
        List<GridColumnConfig> oldGridColumns = null;
        try {
            // 查询处原本的grid配置用于回滚
            oldGridConfig = this.openGeneralDao.getById(GridBasicConfig.class, analyse.getGridBasicConfigId());
            oldGridColumns = this.openGeneralDao
                    .getByHQL("from GridColumnConfig where gridBasicConfig.id = ?", analyse.getGridBasicConfigId());
            // 删除旧grid配置
            if (oldGridConfig != null) {
                this.deleteGridConfig(oldGridConfig, oldGridColumns);
            }
            // 保存新grid配置
            this.saveGridConfig(basicConfig, columns);

            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            // 删除旧统计图表配置
            this.generalDao.executeBySQL("delete from pe_chart_column_def where FK_CHART_DEF_ID = '" + analyse.getPeChartDefId() + "'");
            this.generalDao.executeBySQL("delete from pe_chart_def where id = '" + analyse.getPeChartDefId() + "'");
            // 保存新统计图表配置
            this.generalDao.save(chart);
            this.generalDao.flush();
            this.generalDao.saveAll(chart.getColumnDefList());
            // 生成analyse对象
            analyse.setCustomAnalyseGroup(group);
            analyse.setEnumConstByFlagActive(this.generalDao
                    .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1"));
            analyse.setGridBasicConfigId(basicConfig.getId());
            analyse.setPeChartDefId(chart.getId());
            analyse.setName(name);
            EnumConst viewLevelEnum = this.generalDao
                    .getEnumConstByNamespaceCode(AnalyseConstant.ENUM_CONST_NAMESPACE_FLAG_ANALYSE_VIEW_LEVEL,
                            (String) viewLevel.get(AnalyseConstant.PARAM_VIEW_LEVEL_CODE));
            analyse.setEnumConstByFlagAnalyseViewLevel(viewLevelEnum);
            if (viewLevel.containsKey(AnalyseConstant.PARAM_VIEW_ROLE_CODE)) {
                analyse.setCanViewRoleCode((String) viewLevel.get(AnalyseConstant.PARAM_VIEW_ROLE_CODE));
            }
            analyse.setCreateUser(this.generalDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
            CustomAnalyseHelper helper = new CustomAnalyseHelper();
            String analyseConfig = JsonUtil.toJSONString(helper.generateAnalyseConfig(chartsType, charts, conditions));
            analyse.setCustomAnalyseConfig(analyseConfig);
            this.generalDao.save(analyse);
            // 更新之后要清除缓存
            String gridCacheKey = CacheUtil.getCacheKeyWithParams("GRID_CACHE_%s_%s",
                    new String[]{SiteUtil.getSiteCode(), basicConfig.getId()});
            this.cacheService.remove(gridCacheKey);
        } catch (Exception e) {
            if (oldGridConfig != null) {
                // 回滚grid配置
                this.saveGridConfig(oldGridConfig, oldGridColumns);
            }
            throw new UncheckException(e);
        }
        return analyse.getId();
    }

    /**
     * 删除grid配置
     * @param config
     * @param columns
     */
    private void deleteGridConfig(GridBasicConfig config, List<GridColumnConfig> columns) {
        String code = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Session session = this.generalDao.getMyHibernateTemplate().getSessionFactory().openSession();
        try {
            session.beginTransaction();
            columns.forEach(session::delete);
            session.delete(config);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            throw new UncheckException(e);
        } finally {
            session.close();
        }
        MasterSlaveRoutingDataSource.setDbType(code);
    }

    /**
     * 保存grid配置
     *
     * @param config
     * @param columns
     */
    private void saveGridConfig(GridBasicConfig config, List<GridColumnConfig> columns) {
        String code = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Session session = this.generalDao.getMyHibernateTemplate().getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(config);
            columns.forEach(session::saveOrUpdate);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            throw new UncheckException(e);
        } finally {
            session.close();
        }
        MasterSlaveRoutingDataSource.setDbType(code);
    }

    /**
     * 新建自定义统计
     *
     * @param analyseGroupId
     * @param name
     * @param chartsType
     * @param charts
     * @param viewLevel
     * @param conditions
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws WeChartsServiceException
     */
    private String saveCustomAnalyse(String analyseGroupId, String name,
                                     String chartsType, Map<String, Object> viewLevel, Map<String, Object> charts,
                                     List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException {
        if (StringUtils.isBlank(analyseGroupId) || StringUtils.isBlank(chartsType)
                || AnalyseChartsTypeEnum.getByCode(chartsType) == null || MapUtils.isEmpty(charts)) {
            throw new ParameterIllegalException();
        }
        // 拿出自定义组配置
        CustomAnalyseGroup group = this.getCustomAnalyseGroup(analyseGroupId, false);
        if (group == null) {
            throw new ServiceException("此自定义统计配置不存在");
        }
        conditions = this.handleConditions(conditions);
        // 把参数配置解析成grid配置对象
        CustomAnalyseGridConfigHandler gridHandler = new CustomAnalyseGridConfigHandler(name,
                group, charts, conditions);
        Map<String, Object> config = gridHandler.handleConfigToGridBasicConfig();
        GridBasicConfig basicConfig = (GridBasicConfig) config.get(AnalyseConstant.ARG_GRID_CONFIG);
        List<GridColumnConfig> columns = (List<GridColumnConfig>) config.get(AnalyseConstant.ARG_GRID_COLUMN_CONFIG);
        // 保存grid配置
        this.saveGridConfig(basicConfig, columns);
        CustomAnalyse analyse = new CustomAnalyse();
        try {
            // 把参数配置解析成charts配置对象
            CustomAnalyseChartsConfigHandler chartsHandler = new CustomAnalyseChartsConfigHandler(name, group, chartsType,
                    charts, conditions);
            PeChartDef chart = chartsHandler.handleConfigToChartConfig();
            // 保存统计图表配置
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            this.generalDao.save(chart);
            this.generalDao.flush();
            this.generalDao.saveAll(chart.getColumnDefList());
            this.generalDao.flush();
            // 生成analyse对象
            analyse.setCustomAnalyseGroup(group);
            analyse.setEnumConstByFlagActive(this.generalDao
                    .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1"));
            analyse.setGridBasicConfigId(basicConfig.getId());
            analyse.setPeChartDefId(chart.getId());
            analyse.setName(name);
            EnumConst viewLevelEnum = this.generalDao
                    .getEnumConstByNamespaceCode(AnalyseConstant.ENUM_CONST_NAMESPACE_FLAG_ANALYSE_VIEW_LEVEL,
                            (String) viewLevel.get(AnalyseConstant.PARAM_VIEW_LEVEL_CODE));
            analyse.setEnumConstByFlagAnalyseViewLevel(viewLevelEnum);
            if (viewLevel.containsKey(AnalyseConstant.PARAM_VIEW_ROLE_CODE)) {
                analyse.setCanViewRoleCode((String) viewLevel.get(AnalyseConstant.PARAM_VIEW_ROLE_CODE));
            }
            analyse.setCreateUser(this.generalDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
            CustomAnalyseHelper helper = new CustomAnalyseHelper();
            String analyseConfig = JsonUtil.toJSONString(helper.generateAnalyseConfig(chartsType, charts, conditions));
            analyse.setCustomAnalyseConfig(analyseConfig);
            this.generalDao.save(analyse);
        } catch (Exception e) {
            // 回滚grid配置
            this.deleteGridConfig(basicConfig, columns);
            throw new UncheckException(e);
        }
        return analyse.getId();
    }

}
