package com.whaty.products.service.databoard.impl;

import com.alibaba.fastjson.JSONObject;
import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.User;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.BoardDataModel;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.common.UtilService;
import com.whaty.products.service.databoard.DataBoardShowService;
import com.whaty.products.service.databoard.constant.DataBoardConstant;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import com.whaty.wecharts.constant.WeChartsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据看板查看服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("dataBoardShowService")
public class DataBoardShowServiceImpl implements DataBoardShowService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    @Override
    public Map<String, Object> getDataBoardCategory() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                   ");
        sql.append(" 	cate.id AS id,                                                        ");
        sql.append(" 	cate.`NAME` AS name                                                   ");
        sql.append(" FROM                                                                     ");
        sql.append(" 	pe_pri_category cate                                                  ");
        sql.append(" INNER JOIN pe_base_category base ON base.id = cate.fk_base_category_id   ");
        sql.append(" WHERE                                                                    ");
        sql.append(" 	base. CODE = '" + DataBoardConstant.DATA_BOARD_SHOW_CODE + "'         ");
        sql.append(" AND cate.fk_web_site_id = '" + SiteUtil.getSiteId() + "'                 ");
        return this.myGeneralDao.getOneMapBySQL(sql.toString());
    }

    @Override
    public List<Map<String, Object>> getChartInfo() {
        this.redisCacheService.putToCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(),
                WeChartsConstants.SYSTEM_VARIABLES_DATABOARD_CHART_CODE,
                SiteUtil.getSiteCode()), "");
        String chartCodeStr = this.redisCacheService.getFromCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(),
                WeChartsConstants.SYSTEM_VARIABLES_DATABOARD_CHART_CODE,
                SiteUtil.getSiteCode()));
        if (StringUtils.isBlank(chartCodeStr)) {
            chartCodeStr = this.myGeneralDao.getOneBySQL(" select value from system_variables where" +
                    " name = '" + WeChartsConstants.SYSTEM_VARIABLES_DATABOARD_CHART_CODE + "' " +
                    "and site_code = '" + SiteUtil.getSiteCode() + "'");
            if (StringUtils.isBlank(chartCodeStr)) {
                throw new ServiceException("没有可用的图表");
            }
            this.redisCacheService.putToCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(),
                    WeChartsConstants.SYSTEM_VARIABLES_DATABOARD_CHART_CODE,
                    SiteUtil.getSiteCode()), chartCodeStr);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT                            ");
        sql.append(" 	chartDef.code chartCode,                ");
        sql.append(" 	chartDef.type type,                     ");
        sql.append(" 	ifnull(chartDef.is_cache,'0') isCache,  ");
        sql.append(" 	chartDef.CHART chartName                ");
        sql.append(" FROM                                       ");
        sql.append(" 	pe_chart_def chartDef                   ");
        sql.append(" WHERE                                      ");
        sql.append(CommonUtils.madeSqlIn(chartCodeStr.split(CommonConstant.SPLIT_ID_SIGN), "chartDef.code"));
        sql.append(" AND site_code = ?                          ");
        sql.append(" order by  chartDef.code                    ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode());
    }

    @Override
    public List<BoardDataModel> getDataBoardListData(String isUpdate, String configName) {
        //获取看板数据的redis key
        String redisKey = String.format(DataBoardConstant.DATA_BOARD_STATISTICS_DATA_CACHE_KEY, SiteUtil.getSiteCode(),
                UserUtils.getCurrentUser().getId(), configName);
        //选择更新看板信息和redis中没有数据时做查询
        if (!DataBoardConstant.DO_UPDATE.equals(isUpdate) && redisCacheService.getFromCache(redisKey) != null) {
            return redisCacheService.getFromCache(redisKey);
        }
        StringBuilder sql = new StringBuilder();
        List<BoardDataModel> resultList = new ArrayList<>();
        User user = userService.getCurrentUser();
        List<Map<String, Object>> baseCategoryIds = utilService
                .getBaseCategoryIdsBySiteRole(user.getRole().getId(), SiteUtil.getSiteCode());
        Map<String, Map> baseCategoryMap = new HashMap<>();
        baseCategoryIds.forEach(map -> {
            Map<String, String> cateMap = new HashMap<>();
            cateMap.put("categoryId", (String) map.get("categoryId"));
            cateMap.put("categoryName", (String) map.get("categoryName"));
            baseCategoryMap.put(String.valueOf(map.get("baseId")), cateMap);
        });
        //获取所有类型的数据看板查询信息
        sql.append("   select                                                 ");
        sql.append("     module_type as type,                                 ");
        sql.append("     module_data_name as dataName,                        ");
        sql.append("     icon as icon,                                        ");
        sql.append("     module_data_type as dataType,                        ");
        sql.append("     pe_priority_action_Id as actionId,                   ");
        sql.append("     module_name as name,                                 ");
        sql.append("     module_data_sql as dataSql,                          ");
        sql.append("     module_sql as moduleSql,                             ");
        sql.append("     module_data_content as dataContent,                  ");
        sql.append("     module_data_config as dataConfig,                    ");
        sql.append("     div_type as divType,                                 ");
        sql.append("     code as code,                                        ");
        sql.append("     url as url                                           ");
        sql.append("   from data_board                                        ");
        sql.append("   where                                                  ");
        sql.append("      site_code = ?                                       ");
        sql.append("   order by module_type,module_data_type                  ");
        List<BoardDataModel> dataBoardList =
                BoardDataModel.convert(this.myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode()));
        for (BoardDataModel dataBoard : dataBoardList) {
            //看板类型为“01”、“03”时，为模块条目型数据（待办事项、教学基本信息统计）
            //使用module_data_sql查询数据
            if ("01".equals(dataBoard.getType()) || "03".equals(dataBoard.getType())) {
                if (StringUtils.isNotBlank(dataBoard.getActionId()) &&
                        !baseCategoryMap.keySet().contains(dataBoard.getActionId())) {
                    continue;
                }
                dataBoard.setCategory(baseCategoryMap.get(dataBoard.getActionId()));
                sql.delete(0, sql.length());
                sql.append(dataBoard.getDataSql()
                        .replace("[peManager.id]", UserUtils.getCurrentManager().getId())
                        .replace("[pePriRole.id]", UserUtils.getCurrentUser().getPePriRole().getId()));
                //根据类型放入不同的模版数据之中
                if ("01".equals(dataBoard.getType())) {
                    List<BoardDataModel> searchList = BoardDataModel.convert(this.myGeneralDao
                            .getMapBySQL(ScopeHandleUtils.handleScopeSignOfSql(sql.toString(), user.getId())));
                    dataBoard.setCount(CollectionUtils.isEmpty(searchList) ? 0 : searchList.size());
                    dataBoard.setListBoardDataModel(searchList);
                    resultList.add(dataBoard);
                } else {
                    List<BoardDataModel> searchList =
                            BoardDataModel.convert(this.myGeneralDao.
                                    getMapBySQL(ScopeHandleUtils.handleScopeSignOfSql(sql.toString(), user.getId())));
                    dataBoard.setListBoardDataModel(searchList);
                    resultList.add(dataBoard);
                }
            } else if ("02".equals(dataBoard.getType())) {
                if (StringUtils.isNotBlank(dataBoard.getActionId()) &&
                        baseCategoryMap.keySet().contains(dataBoard.getActionId())) {
                    dataBoard.setCategory(baseCategoryMap.get(dataBoard.getActionId()));
                }
                String sqlCondition = "SCOPE_STRING LIKE '%" + user.getRole().getCode() + "%'";
                String bulletinSql = String.format(dataBoard.getModuleSql(), user.getId(), sqlCondition);
                List<BoardDataModel> bulletinList = BoardDataModel.convert(myGeneralDao.getMapBySQL(bulletinSql));
                dataBoard.setListBoardDataModel(bulletinList);
                resultList.add(dataBoard);
            } else if ("04".equals(dataBoard.getType())) {
                dataBoard.setListBoardDataModel(
                        JSONObject.parseArray(dataBoard.getDataConfig(), BoardDataModel.class));
                resultList.add(dataBoard);
            }

        }
        List<String> sortList = JSONObject.parseArray(myGeneralDao
                .getOneBySQL("SELECT VALUE  FROM system_variables WHERE NAME = ? AND site_code = ?", configName,
                        SiteUtil.getSiteCode()), String.class);
        List<BoardDataModel> result = resultList.stream().filter(e-> sortList.contains(e.getCode()))
                .sorted(Comparator.comparingInt(e -> sortList.indexOf(e.getCode())))
                .collect(Collectors.toList());
        redisCacheService.putToCache(redisKey, result);
        return result;
    }


    @Override
    public List<Map<String, Object>> getOneChartDataInfo(String code) {
        String chartSql = this.myGeneralDao
                .getOneBySQL(" select chart_sql from pe_chart_def where code = '" + code + "' and site_code = '"
                        + MasterSlaveRoutingDataSource.getDbType() + "'");
        if (StringUtils.isBlank(chartSql)) {
            throw new ServiceException("图表不存在");
        }
        return this.myGeneralDao.getMapBySQL(ScopeHandleUtils.handleScopeSignOfSql(chartSql, UserUtils.getCurrentUserId()));
    }

}
