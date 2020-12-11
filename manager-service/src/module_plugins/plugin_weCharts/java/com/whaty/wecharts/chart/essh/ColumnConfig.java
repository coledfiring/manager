package com.whaty.wecharts.chart.essh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * column配置类
 */
public class ColumnConfig implements Serializable {

    private String name;

    private String dataIndex;

    private int index;

    private boolean search;

    private boolean add;

    private boolean list;
    /**
     * 是否可以导出
     */
    private boolean report;

    private String type;

    private boolean allowBlank;

    private int maxLength;

    private String textFieldParameters;

    private String comboSQL;
    /**
     * 用于日期范围查询
     */
    private String startDateName;

    private String endDateName;

    private List comboList;

    private Map comboMap;

    private String format;

    /*
     * 添加三种ComboBox（下拉框）属性供 选择  czc
     */
    /**
     * extjs原始属性
     */
    public final static String COMBOBOX = "ComboBox";
    /**
     * 下拉框宽度自适应内容宽度
     */
    public final static String WHATY_COMBOBOX = "WhatyComboBox";
    /**
     * 下拉框添加复选框，可多选
     */
    public final static String MULTI_SELECT = "MultiSelect";
    /**
     * 设置该属性的值可以选择列表页面所要的combobox类型，以上三种供选择;可以在此设置默认选项
     */
    private String comboboxType = MULTI_SELECT;

    /**
     * minimal constructor
     */
    public ColumnConfig(String name, String dataIndex, int index) {
        this.name = name;
        this.dataIndex = dataIndex;
        this.index = index;
        this.search = true;
        this.add = true;
        this.list = true;
        this.type = "TextField";
        this.allowBlank = false;
        this.maxLength = 25;
        this.textFieldParameters = null;
        this.report = false;
    }

    /**
     * minimal constructor
     */
    public ColumnConfig(String name, String dataIndex, String type, String startDateName, String endDateName, int index) {
        this.name = name;
        this.dataIndex = dataIndex;
        this.index = index;
        this.search = true;
        this.add = true;
        this.list = true;
        this.type = type;
        this.allowBlank = false;
        this.maxLength = 25;
        this.textFieldParameters = null;
        this.report = false;
        this.startDateName = startDateName;
        this.endDateName = endDateName;
    }

    /**
     * full constructor
     *
     * @param name
     * @param dataIndex
     * @param search
     * @param add
     * @param list
     * @param type
     * @param allowBlank
     * @param maxLength
     * @param textFieldParameters
     */
    public ColumnConfig(String name, String dataIndex, boolean search, boolean add, boolean list, String type, boolean allowBlank, int maxLength, String textFieldParameters, boolean report, String startDateName, String endDateName) {
        this.name = name;
        this.dataIndex = dataIndex;
        this.search = search;
        this.add = add;
        this.list = list;
        this.type = type;
        this.allowBlank = allowBlank;
        this.maxLength = maxLength;
        this.textFieldParameters = textFieldParameters;
        this.report = report;
        this.startDateName = startDateName;
        this.endDateName = endDateName;
    }

    /**
     * full constructor
     *
     * @param name
     * @param dataIndex
     * @param search
     * @param add
     * @param list
     * @param type
     * @param allowBlank
     * @param maxLength
     * @param textFieldParameters
     * @param comboboxType        下拉框格式
     */
    public ColumnConfig(String name, String dataIndex, boolean search, boolean add, boolean list, String type, boolean allowBlank, int maxLength, String textFieldParameters, boolean report, String comboboxType) {
        this.name = name;
        this.dataIndex = dataIndex;
        this.search = search;
        this.add = add;
        this.list = list;
        this.type = type;
        this.allowBlank = allowBlank;
        this.maxLength = maxLength;
        this.textFieldParameters = textFieldParameters;
        this.report = report;
        if (comboboxType != null) {
            this.comboboxType = comboboxType;
        }

    }

    /**
     * full constructor
     *
     * @param name
     * @param dataIndex
     * @param search
     * @param add
     * @param list
     * @param type
     * @param allowBlank
     * @param maxLength
     * @param textFieldParameters
     */
    public ColumnConfig(String name, String dataIndex, boolean search, boolean add, boolean list, String type, boolean allowBlank, int maxLength, String textFieldParameters, boolean report) {
        this.name = name;
        this.dataIndex = dataIndex;
        this.search = search;
        this.add = add;
        this.list = list;
        this.type = type;
        this.allowBlank = allowBlank;
        this.maxLength = maxLength;
        this.textFieldParameters = textFieldParameters;
        this.report = report;

    }

    public String getName() {
        return name;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public String getDataIndex_() {
        String temp = dataIndex.replace(".", "_");
        if (temp.lastIndexOf("_") > 0 && temp.endsWith("name")) {
            String temp2 = temp.substring(0, temp.lastIndexOf("_") - 1);
            if (temp2.contains("_")) {
                temp = temp2.substring(temp2.lastIndexOf("_") + 1) + temp.substring(temp.lastIndexOf("_"));
            }

        }
        return temp;
    }

    public boolean isSearch() {
        return search;
    }

    public boolean isAdd() {
        return add;
    }

    public boolean isList() {
        return list;
    }

    public String getType() {
        return type;
    }

    public boolean isAllowBlank() {
        return allowBlank;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getTextFieldParameters() {
        return textFieldParameters;
    }

    public void setTextFieldParameters(String textFieldParameters) {
        this.textFieldParameters = textFieldParameters;
    }

    public String getComboSQL() {
        return comboSQL;
    }

    /**
     * 部分特殊符号不支持
     * 已支持：'
     *
     * @param comboSQL
     */
    public void setComboSQL(String comboSQL) {
        this.comboSQL = comboSQL;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    /**
     * 例子：col.setComboList(new String[]{"1","有效"},new String[]{"0","禁用"});
     *
     * @param args
     */
    public void setComboList(String[]... args) {
        if (args == null) {
            this.comboList = null;
            return;
        }
        List list = new ArrayList();

        for (int i = 0; i < args.length; i++) {
            list.add(args[i]);
        }
        this.comboList = list;
    }

    public Map getComboMap() {
        if (comboList == null) {
            return null;
        }
        if (comboMap == null) {
            comboMap = new HashMap<>();
            for (int i = 0; i < comboList.size(); i++) {
                comboMap.put(((String[]) comboList.get(i))[0], ((String[]) comboList.get(i))[1]);
            }
        }
        return this.comboMap;
    }

    public List getComboList() {
        return comboList;
    }

    public void setComboList(List comboList) {
        this.comboList = comboList;
    }

    public void setComboMap(Map comboMap) {
        this.comboMap = comboMap;
    }

    public String getComboboxType() {
        return comboboxType;
    }

    public void setComboboxType(String comboboxType) {
        this.comboboxType = comboboxType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStartDateName() {
        return startDateName;
    }

    public void setStartDateName(String startDateName) {
        this.startDateName = startDateName;
    }

    public String getEndDateName() {
        return endDateName;
    }

    public void setEndDateName(String endDateName) {
        this.endDateName = endDateName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
