package com.whaty.wecharts.service;

import com.whaty.wecharts.bean.PeChartColumnDef;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.exception.WeChartsServiceException;

import java.util.List;

/**
 * weCharts管理服务类
 */
public interface ChartManagerService {

    /**
     * 删除图表配置
     * @param id
     * @return
     * @throws Exception
     */
    boolean delChart(String id) throws Exception;

    /**
     * 删除图表数据列
     * @param id
     * @return
     * @throws Exception
     */
    boolean delColumn(String id) throws Exception;

    /**
     * 保存图表配置
     * @param peChartDef
     * @param id
     * @return
     * @throws WeChartsServiceException
     */
    void savePeChartDef(PeChartDef peChartDef, String id) throws Exception;

    /**
     * 保存图表配置
     * @param peChartDef
     * @param dataDirection
     * @param chartColumnType
     * @param columnDefList
     * @return
     * @throws WeChartsServiceException
     */
    PeChartDef savePeChartDef(PeChartDef peChartDef, String dataDirection, String chartColumnType, List<String> columnDefList) throws WeChartsServiceException;

    /**
     * 查询图表配置列表
     * @return
     * @throws Exception
     */
    List<PeChartDef> queryChartList(String id) throws Exception;

    /**
     * 查询图表配置对象
     * @param code
     * @return
     * @throws Exception
     */
    PeChartDef queryChartByCode(String code) throws WeChartsServiceException;

    /**
     * 获取图表的显示列
     * @param chart
     * @return
     * @throws WeChartsServiceException
     */
    List<PeChartColumnDef> queryColumnByChart(String chart) throws WeChartsServiceException;

}
