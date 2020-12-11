package com.whaty.products.service.databoard;

import com.whaty.domain.bean.BoardDataModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据看板查看服务类
 * @author weipengsen
 */
public interface DataBoardShowService {

    /**
     * 获取数据看板的菜单节点
     * @return
     */
    Map<String,Object> getDataBoardCategory();

    /**
     * 获取数据看板的所有图表信息
     * @return
     */
    List<Map<String, Object>> getChartInfo();

    /**
     * 获取数据看板的列表数据
     * @param isUpdate
     * @return
     */
    List<BoardDataModel> getDataBoardListData(String isUpdate, String dataConfig);
    /**
     * 获取图表数据
     * @param code
     * @return
     */
    List<Map<String, Object>> getOneChartDataInfo(String code);
}
