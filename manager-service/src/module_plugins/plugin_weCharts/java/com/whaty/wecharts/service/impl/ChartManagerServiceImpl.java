package com.whaty.wecharts.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.wecharts.bean.PeChartColumnDef;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.chart.ChartColumnType;
import com.whaty.wecharts.chart.essh.DataDirection;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.service.ChartManagerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("chartManagerService")
public class ChartManagerServiceImpl implements ChartManagerService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public boolean delChart(String id) throws Exception {
        if(StringUtils.isNotBlank(id)){
            String sql = "update pe_chart_def set is_del = 1 where id = '" + id + "'";
            int i = this.generalDao.executeBySQL(sql);
            return BooleanUtils.toBoolean(i);

        }
        throw new WeChartsServiceException("参数不合法");
    }

    @Override
    public boolean delColumn(String id) throws Exception {
        if(StringUtils.isNotBlank(id)){
            String sql = "delete from pe_chart_column_def where id = '" + id + "'";
            int i = this.generalDao.executeBySQL(sql);
            return BooleanUtils.toBoolean(i);

        }
        throw new WeChartsServiceException("参数不合法");
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public List<PeChartColumnDef> queryColumnByChart(String chart) throws WeChartsServiceException {
        StringBuilder sql = new StringBuilder();
        sql.append("select id as id, fk_chart_def_id as peChartDef, column_index as columnIndex, type as type,  ");
        sql.append("       series_name  as seriesName, group_name as groupName, axis_index as axisIndex, input_date as inputDate, ");
        sql.append("       column_name as columnName ");
        sql.append("from pe_chart_column_def where fk_chart_def_id = '" + chart + "' ");
        List<Object[]> list = generalDao.getBySQL(sql.toString());
        List<PeChartColumnDef> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(Object[] obj : list){
                PeChartColumnDef bean = new PeChartColumnDef();
                bean.setId(obj[0].toString());
                bean.setPeChartDef(new PeChartDef(obj[1].toString()));
                bean.setColumnIndex(Integer.parseInt(obj[2].toString()));
                bean.setType(null != obj[3] ? ChartColumnType.valueOf(obj[3].toString()) : null);
                bean.setSeriesName(this.fixObjNull(obj[4], ""));
                bean.setGroupName(this.fixObjNull(obj[5], ""));
                bean.setAxisIndex(Integer.parseInt(this.fixObjNull(obj[6], "0")));
                bean.setColumnName(this.fixObjNull(obj[8], ""));
                result.add(bean);
            }
        }
        return result;
    }

    private String fixObjNull(Object obj, String defaultValue) {
        return null != obj ? obj.toString() : defaultValue;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PeChartDef queryChartByCode(String code) throws WeChartsServiceException {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, chart, code, data_direction, has_time_line, ");
        sql.append("  has_data_zoom, data_zoom_start, data_zoom_end, zoom_lock, type, ");
        sql.append("  data_index_column, value_columns_str, input_date, chart_sql  ");
        sql.append(" from pe_chart_def ");
        sql.append(" where code = ? and site_code = ? and is_del = 0 ");
        List<Object[]> list = this.generalDao.getBySQL(sql.toString(), code, SiteUtil.getSiteCode());
        PeChartDef chartDef = null;
        try {
            if(CollectionUtils.isNotEmpty(list)){
                Object[] obj = list.get(0);
                chartDef = builePeChartDef(obj);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return chartDef;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PeChartDef> queryChartList(String id) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, chart, code, data_direction, has_time_line, ");
        sql.append(" has_data_zoom, data_zoom_start, data_zoom_end, zoom_lock, type, ");
        sql.append(" data_index_column, value_columns_str, input_date, chart_sql  ");
        sql.append(" from pe_chart_def ");
        sql.append(" where is_del = 0 ");
        sql.append(" and site_code = ? ");
        if(StringUtils.isNotBlank(id)){
            sql.append(" and id = '" + id + "' ");
        }
        List<PeChartDef> charts = null;
        List<Object[]> list = this.generalDao.getBySQL(sql.toString(), SiteUtil.getSiteCode());
        if(CollectionUtils.isNotEmpty(list)){
            charts = list.stream().map(e -> {
                try {
                    return builePeChartDef(e);
                } catch (Exception e1) {
                    throw new UncheckException(e1);
                }
            }).collect(Collectors.toList());
        }
        return charts;
    }

    /**
     * 构建PeChartDef 对象
     * @param obj
     * @return
     */
    private PeChartDef builePeChartDef(Object[] obj) throws Exception{
        PeChartDef chartDef = new PeChartDef();
        chartDef.setId(this.fixObjNull(obj[0], ""));
        chartDef.setChart(this.fixObjNull(obj[1], ""));
        chartDef.setCode(this.fixObjNull(obj[2], ""));
        chartDef.setDataDirection(null != obj[3] ? DataDirection.valueOf(obj[3].toString()) : null);
        chartDef.setHasTimeline("1".equals(this.fixObjNull(obj[4], "0")));
        chartDef.setHasDataZoom("1".equals(this.fixObjNull(obj[5], "0")));
        chartDef.setDataZoomStart(Integer.parseInt(this.fixObjNull(obj[6], "0")));
        chartDef.setDataZoomEnd(Integer.parseInt(this.fixObjNull(obj[7], "0")));
        chartDef.setZoomLock("1".equals(this.fixObjNull(obj[8], "0")));
        chartDef.setType(null != obj[9] ? ChartColumnType.valueOf(obj[9].toString()) : null);
        chartDef.setDataIndexColumn(null != obj[10] ? obj[10].toString() : null);
        String valC = this.fixObjNull(obj[11], "");
        if(StringUtils.isNotBlank(valC)){
            chartDef.setValueColumnsStr(valC);
        } else {
            chartDef.setValueColumnsStr("");
        }
        chartDef.setChartSql(this.fixObjNull(obj[13], ""));
        return chartDef;
    }

    @Override
    public void savePeChartDef(PeChartDef peChartDef, String id) throws Exception {
        if(null == peChartDef){
            throw new WeChartsServiceException("图表对象不能为空");
        }
        StringBuilder sql = new StringBuilder();
        if(StringUtils.isNotBlank(peChartDef.getId())){
            //更新
            sql.append(" update pe_chart_def set ");
            sql.append(" code= :code, ");
            sql.append(" data_direction= :dataDirection, ");
            sql.append(" has_time_line= :hasTimeline, ");
            sql.append(" has_data_zoom= :hasDataZoom, ");
            sql.append(" data_zoom_start= :dataZoomStart, ");
            sql.append(" data_zoom_end= :dataZoomEnd, ");
            sql.append(" zoom_lock= :zoomLock, ");
            sql.append(" type= :type, ");
            sql.append(" data_index_column= :dataIndexColumn , ");
            sql.append(" value_columns_str = :valueColumnsStr, ");
            sql.append(" input_date= :inputDate, ");
            sql.append(" chart_sql= :chartSql, ");
            sql.append(" chart= :chart ");
            sql.append(" where id = :id and is_del = 0 ");
        } else {
            //新加
            sql.setLength(0);
            sql.append("insert into pe_chart_def( ");
            sql.append("id, chart, code, data_direction, has_time_line, has_data_zoom, ");
            sql.append("data_zoom_start, data_zoom_end, zoom_lock, type, data_index_column,  ");
            sql.append("value_columns_str, input_date, chart_sql, site_code ) values ( ");
            sql.append(":id, :chart ,:code, :dataDirection, :hasTimeline, :hasDataZoom, ");
            sql.append(":dataZoomStart, :dataZoomEnd, :zoomLock, :type, :dataIndexColumn, ");
            sql.append(":valueColumnsStr, :inputDate, :chartSql, :siteCode)");
        }

        if(StringUtils.isBlank(peChartDef.getId()) && StringUtils.isNotBlank(id)){
            peChartDef.setId(id);
        }
        Map<String, Object> chart =  buildMap(peChartDef);
        this.generalDao.executeBySQL(sql.toString(), chart);
        saveChartColumn(peChartDef.getColumnDefList(), peChartDef.getId());
    }

    /**
     * 保存图表数据列
     * @param columnDefList
     */
    private final void saveChartColumn(List<PeChartColumnDef> columnDefList, String chartDefId){
        List<String> sqlList = new ArrayList<>();
        List<Map<String, Object>> paramList = new ArrayList<>();
        for(PeChartColumnDef columnDef : columnDefList){
            if(StringUtils.isNotBlank(columnDef.getId())){
                sqlList.add("update pe_chart_column_def set fk_chart_def_id = :peChartDef, column_index = :columnIndex, " +
                            " type = :type, series_name = :seriesName, group_name= :groupName, axis_index = :axisIndex, " +
                            " input_date = :inputDate, column_name = :columnName " +
                            " where id = :id");
            } else {
                sqlList.add("insert into pe_chart_column_def" +
                            "(id, fk_chart_def_id, column_index, type, series_name, group_name, axis_index, input_date,  column_name)  " +
                            "values(:id, :peChartDef, :columnIndex, :type, :seriesName, :groupName, :axisIndex, :inputDate, :columnName) ");
            }
            paramList.add(buileColumnMap(columnDef, chartDefId));
        }
        this.generalDao.batchExecuteSql(sqlList, paramList);

    }

    /**
     * 构建 PeChartColumnDef 对象查询参数Map
     * @param columnDef
     * @return
     */
    private final Map<String, Object> buileColumnMap(PeChartColumnDef columnDef, String chartDefId){
        Map<String, Object> params = new HashMap<>();
        params.put("id", StringUtils.isNotBlank(columnDef.getId()) ? columnDef.getId()
                : UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        params.put("peChartDef", chartDefId);
        params.put("columnIndex", columnDef.getColumnIndex());
        params.put("type", null != columnDef.getType() ? columnDef.getType().name() : null);
        params.put("seriesName", columnDef.getSeriesName());
        params.put("groupName", columnDef.getGroupName());
        params.put("axisIndex", null != columnDef.getAxisIndex() ? columnDef.getAxisIndex() : 0);
        params.put("inputDate", new Date());
        params.put("columnName", columnDef.getColumnName());
        return params;
    }

    /**
     * 构建 PeChartDef 对象查询参数Map
     * @param peChartDef
     * @return
     */
    private Map<String, Object> buildMap(PeChartDef peChartDef){
        Map<String, Object> params = new HashMap<>();
        params.put("id", peChartDef.getId());
        params.put("chart", peChartDef.getChart());
        params.put("code", peChartDef.getCode());
        params.put("dataDirection", peChartDef.getDataDirection().name());
        params.put("hasTimeline", BooleanUtils.toInteger(peChartDef.isHasTimeline()));
        params.put("hasDataZoom", BooleanUtils.toInteger(peChartDef.isHasDataZoom()));
        params.put("dataZoomStart", peChartDef.getDataZoomStart());
        params.put("dataZoomEnd", peChartDef.getDataZoomEnd());
        params.put("zoomLock", BooleanUtils.toInteger(peChartDef.isZoomLock()));
        params.put("type", null != peChartDef.getType() ? peChartDef.getType().name() : null);
        params.put("dataIndexColumn", null != peChartDef.getDataIndexColumn() ? peChartDef.getDataIndexColumn() : -1);
        params.put("valueColumnsStr", peChartDef.getValueColumnsStr());
        params.put("inputDate", new Date());
        params.put("chartSql", peChartDef.getChartSql());
        params.put("siteCode", SiteUtil.getSiteCode());
        return params;
    }


    @Override
    public PeChartDef savePeChartDef(PeChartDef peChartDef, String dataDirection, String chartColumnType,
                                     List<String> columnDefList) throws WeChartsServiceException {
        List<PeChartColumnDef> list = peChartDef.getColumnDefList();
        if (StringUtils.isNotBlank(chartColumnType)) {
            ChartColumnType type = ChartColumnType.valueOf(chartColumnType);
            peChartDef.setType(type);
        }
        if (StringUtils.isNotBlank(dataDirection)) {
            DataDirection direction = DataDirection.valueOf(dataDirection);
            peChartDef.setDataDirection(direction);
        } else {
            peChartDef.setDataDirection(DataDirection.vertical);
        }
        peChartDef.setInputDate(new Date());
        if (StringUtils.isBlank(peChartDef.getId())) {
            peChartDef.setId(null);
        }
        if (StringUtils.isBlank(peChartDef.getSiteCode())) {
            peChartDef.setSiteCode(SiteUtil.getSiteCode());
        }
        peChartDef = this.generalDao.save(peChartDef);

        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                PeChartColumnDef peChartColumnDef = list.get(i);
                if (peChartColumnDef == null) {
                    continue;
                }
                peChartColumnDef.setPeChartDef(peChartDef);
                String columnType = columnDefList.get(i);
                if (StringUtils.isNotBlank(columnType)) {
                    ChartColumnType type = ChartColumnType.valueOf(columnType);
                    peChartColumnDef.setType(type);
                } else {
                    throw new WeChartsServiceException("第 " + peChartColumnDef.getColumnIndex() + " 列的数据类型为空");
                }
                peChartColumnDef.setInputDate(new Date());
                if (StringUtils.isBlank(peChartColumnDef.getId())) {
                    peChartColumnDef.setId(null);
                }
                this.generalDao.save(peChartColumnDef);
            }
        }
        return peChartDef;
    }


}
