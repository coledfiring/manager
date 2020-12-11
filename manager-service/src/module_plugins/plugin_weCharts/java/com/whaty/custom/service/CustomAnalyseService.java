package com.whaty.custom.service;

import com.whaty.wecharts.exception.WeChartsServiceException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 自定义统计图表服务类
 * @author weipengsen
 */
public interface CustomAnalyseService {

    /**
     * 获取自定义统计图表配置
     *
     * @param analyseGroupId
     * @param analyseId
     * @return
     */
    Map<String,Object> getCustomAnalyseConfig(String analyseGroupId, String analyseId);

    /**
     * 根据配置获取统计图表charts预览
     *
     * @param analyseGroupId
     * @param name
     * @param chartsType
     * @param charts
     * @param conditions
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws WeChartsServiceException
     */
    Map<String,Object> getCustomAnalyseToPreviewChartsConfig(String analyseGroupId, String name, String chartsType,
                                                 Map<String, Object> charts, List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException;

    /**
     * 根据配置获取统计图表grid预览
     *
     * @param analyseGroupId
     * @param name
     * @param chartsType
     * @param charts
     * @param conditions
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws WeChartsServiceException
     */
    Map<String,Object> getCustomAnalyseToPreviewGridConfig(String analyseGroupId, String name, String chartsType,
                                                 Map<String, Object> charts, List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException;

    /**
     * 保存自定义图表统计
     * @param analyseId
     * @param name
     * @param analyseGroupId
     * @param chartsType
     * @param viewLevel
     *@param charts
     * @param conditions   @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws WeChartsServiceException
     * @return
     */
    String saveCustomAnalyse(String analyseId, String name, String analyseGroupId, String chartsType,
                             Map<String, Object> viewLevel, Map<String, Object> charts, List<Map<String, Object>> conditions)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, WeChartsServiceException;

    /**
     * 获取自定义统计配置的actionId和chartId
     * @param analyseId
     * @return
     */
    Map<String,Object> getCustomAnalyse(String analyseId);

    /**
     * 获取自定义统计组
     * @param analyseGroupId
     * @return
     */
    Map<String,Object> getCustomAnalyseGroup(String analyseGroupId);

    /**
     * 删除自定义统计
     * @param analyseId
     */
    void deleteCustomAnalyse(String analyseId);

    /**
     * 收藏自定义统计
     * @param analyseId
     */
    void doCollectCustomAnalyse(String analyseId);

    /**
     * 取消收藏自定义统计
     * @param analyseId
     */
    void doCancelCollectCustomAnalyse(String analyseId);
}
