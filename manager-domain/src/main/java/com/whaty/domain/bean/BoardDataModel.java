package com.whaty.domain.bean;

import com.whaty.util.CommonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据看板数据模型
 *
 * @author weipengsen
 */
@Data
public class BoardDataModel implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 模块图标icon的class
     */
    private String icon;

    /**
     * 模块名称
     */
    private String name;

    /**
     * dataName
     */
    private String dataName;

    /**
     *
     */
    private Map<String, String> category;

    /**
     * actionId
     */
    private String actionId;

    /**
     * 数据模块的 数量
     */
    private int count;

    /**
     * 数据模块
     */
    private String dataContent;

    /**
     * 公告模块 标题
     */
    private String title;

    /**
     * 公告模块 更新时间
     */
    private String updateDate;


    /**
     * 公告模块 公告单位
     */
    private String unitName;

    /**
     * 模块类型
     */
    private String type;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 模块SQL
     */
    private String moduleSql;

    /**
     * 模块数据SQL
     */
    private String dataSql;

    /**
     * 公告是否已读
     */
    private String isRead;

    /**
     * 更多url
     */
    private String url;

    /**
     * 数据配置
     */
    private String dataConfig;

    /**
     * 前端样式类型
     */
    private String divType;

    /**
     * 数据集合
     */
    private List<BoardDataModel> listBoardDataModel;

    /**
     * 配置名称
     */
    private String cName;

    /**
     * 配置code 、 模块code
     */
    private String code;

    /**
     * 将数据库查出的List<Map>  转化为List<BoardDataModel>
     *
     * @param mapList
     * @return
     */
    public static List<BoardDataModel> convert(List<Map<String, Object>> mapList) {
        List<BoardDataModel> boardDataModelList = new ArrayList<>();
        mapList.forEach(e->boardDataModelList.add(CommonUtils.convertMapToBean(e, BoardDataModel.class)));
        return boardDataModelList;
    }



}
