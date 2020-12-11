package com.whaty.domain.bean;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 通用模板打印实体类，用于控制页面的布局，包括单栏两栏、横版竖版，表头等数据设置
 *
 * @author weipengsen
 */
@Data
public class PrintFormConfig extends AbstractBean {

    private static final long serialVersionUID = 6297678710314558835L;

    private String id;
    /**
     * 字体大小
     */
    private int fontSize;
    /**
     * 每页列数
     */
    private int pageNum;

    /**
     * 栏目类型，单栏，两栏
     */
    private int columnNum;
    /**
     * 主标题
     */
    private String title;
    /**
     * 定位副标题行列的二维数组
     */
    private String[][] subTitleArr;
    /**
     * 表头，可能多层
     */
    private List<List<PrintHeaderConfig>> printHeaderConfigs = new ArrayList<>();
    /**
     * 文章版型，横版竖版
     */
    private int versionType;
    /**
     * 页面底部信息
     */
    private String fixedFoot;
    /**
     * 落款
     */
    private String inscribed;
    /**
     * 时间
     */
    private String date;

    /**
     * 是否显示页码
     */
    private boolean showPageNum;
    /**
     * 是否显示序号
     */
    private boolean showOrder;
    /**
     * 是否每页显示标题
     */
    private boolean everyPageShowTitle;
    /**
     * 是否显示落款
     */
    private boolean showInscribed;
    /**
     * 是否显示日期
     */
    private boolean showDate;
    /**
     * 排列数据的方式，先排列所有左栏再排列右栏，填充每一行
     */
    private String listType;
    /**
     * 没有数据也填充满整个表格
     */
    private boolean isFullPage = true;
    /**
     * 单元格高度
     */
    private int cellHeight = NORMAL_CELL_HEIGHT;
    /**
     * 总页数
     */
    private int totalPageNum;

    /**
     * 用户传入的原始数据
     */
    private List<Object[]> originContent;

    /**
     * 传入的列表数据集合,里面的每个list是一页，每个数组是一行
     */
    private List<List<Object[]>> content = new ArrayList<>();

    /**
     * 单栏
     */
    public static final int ONE_COLUMN_NUM = 1;
    /**
     * 多栏
     */
    public static final int TWO_COLUMN_NUM = 2;
    /**
     * 内置最小高度
     */
    public static final int MIN_CELL_HEIGHT = 15;
    /**
     * 内置默认高度
     */
    public static final int NORMAL_CELL_HEIGHT = 20;
    /**
     * 内置最大高度
     */
    public static final int BIG_CELL_HEIGHT = 25;
    /**
     * 两栏情况下，先铺满左边栏，在铺满右边
     */
    public static final String LEFT_THEN_RIGHT = "1";
    /**
     * 两栏情况下，铺满每一行
     */
    public static final String FULL_ROW = "2";
    /**
     * 大字体
     */
    public static final int BIG_FONT_SIZE = 18;
    /**
     * 普通字体
     */
    public static final int NORMAL_FONT_SIZE = 14;
    /**
     * 小字体
     */
    public static final int SMALL_FONT_SIZE = 10;
    /**
     * 横版排版
     */
    public static final int HORIZONTAL_VERSION = 20;
    /**
     * 竖版排版
     */
    public static final int PORTRAIT_VERSION = 35;

    /**
     * 无参构造函数，默认字体为普通字体，排版为竖版，不显示页码，显示日期，显示序号
     */
    public PrintFormConfig() {
        this.columnNum = ONE_COLUMN_NUM;
        this.fontSize = NORMAL_FONT_SIZE;
        this.versionType = PORTRAIT_VERSION;
        this.pageNum = PORTRAIT_VERSION;
        this.everyPageShowTitle = false;
        this.showPageNum = false;
        this.showDate = true;
        this.showInscribed = true;
        this.showOrder = true;
        this.date = CommonUtils.getStringDate(new Date(),
                CommonConstant.DEFAULT_DATE_STR);
    }

    /**
     * 构造函数,配置标题、表头、落款
     * 默认设置字体为普通字体、排版为竖版，栏数为单栏，不显示页码，显示日期，显示落款，显示序号
     * @param title
     * @param printHeaderConfigs
     * @param inscribed
     */
    public PrintFormConfig(String title, List<List<PrintHeaderConfig>> printHeaderConfigs, String inscribed) {
        this();
        this.title = title;
        this.printHeaderConfigs = printHeaderConfigs;
        this.inscribed = inscribed;
    }

    /**
     * 构造函数,配置标题、表头、落款、是否显示页码
     * 默认设置字体为普通字体、排版为竖版，栏数为单栏，显示日期，显示落款，显示序号
     * @param title
     * @param printHeaderConfigs
     * @param inscribed
     * @param isShowOrder
     * @param isShowPageNum
     */
    public PrintFormConfig(String title, List<List<PrintHeaderConfig>> printHeaderConfigs, String inscribed, boolean isShowOrder,
                           boolean isShowPageNum) {
        this();
        this.title = title;
        this.printHeaderConfigs = printHeaderConfigs;
        this.inscribed = inscribed;
        this.showPageNum = isShowPageNum;
    }

    /**
     * 初始化副标题数组
     * @param rowNum
     * @param colNum
     * @author weipengsen
     */
    public void initSubTitle(int rowNum, int colNum) {
        subTitleArr = new String[rowNum][colNum];
    }

    /**
     * 将副标题存入二维数组对应的行
     * @param title
     * @author weipengsen
     */
    public void addSubTitle(PrintSubTitle title) {
        //生成副标题单元格内容
        subTitleArr[title.getRowNum()-1][title.getColumnNum()-1] =
                "<td style=\"text-align: left;padding: 0;color: black;font-size:"
                + this.getFontSize() + "px;\" " + (title.getColspan() == 0?"":"colspan=" + title.getColspan()) + "><b>"
                + title.getHeader() + "</b>:" + title.getValue() + "</td>";
    }

    /**
     * 得到副标题的html
     * @author weipengsen
     * @return
     */
    public String getSubTitle() {
        //创建副标题字符串
        StringBuilder subTitle = new StringBuilder();
        //循环副标题二维数组
        for(String[] rowArr : subTitleArr) {
            //拼接表列开始标记
            subTitle.append("<tr class=\"print-sub-title-tr\">");
            for(String content : rowArr) {
                //如果内容不为空则拼接单元格
                if(StringUtils.isNotBlank(content)) {
                    subTitle.append(content);
                }
            }
            //拼接结束标记
            subTitle.append("</tr>");
        }
        return subTitle.toString();
    }

    /**
     * 生成适合当前配置的数据集合
     * @param param
     */
    public void initContentByConfig(List<Object[]> param) {
        //数据是否能被栏数整除
        boolean hasMore = param.size() % this.columnNum != 0;
        //数据的行数
        int rowNum = hasMore?param.size()/this.columnNum+1:param.size()/this.columnNum;
        int loopNum = rowNum;
        //是否每页表格必须充满
        if(isFullPage) {
            boolean hasMorePage = rowNum % this.pageNum != 0;
            int pageNum = hasMorePage?rowNum/this.pageNum + 1:rowNum/this.pageNum;
            loopNum = pageNum*this.pageNum;
        }
        int step = 1;
        //判断排列方式
        if(FULL_ROW.equals(this.listType)) {
            //如果列表每次充满行则每次循环索引增加栏数
            step = columnNum;
        }
        //拿到标题列数
        int columnNum = 0;
        //拿到最大的标题烈数
        for(List<PrintHeaderConfig> header : this.printHeaderConfigs) {
            if(header.size() > columnNum) {
                if(this.showOrder) {
                    columnNum = header.size() + 1;
                } else {
                    columnNum = header.size();
                }
            }
        }
        List<Object[]> pageContent = null;
        //循环拼接所有数据行
        for(int i = 0; i < loopNum; i += step) {
            if(CollectionUtils.isEmpty(this.content)
                    || this.content.get(this.content.size() - 1).size() == this.pageNum) {
                //如果第一次循环或最后的页集合已满
                pageContent = new ArrayList<>();
                this.content.add(pageContent);
            }
            List<Object> row = new ArrayList<Object>();
            int columnStep = 1;
            //判断排列方式，是先左再右还是先充满每行
            if(LEFT_THEN_RIGHT.equals(this.listType)) {
                columnStep = this.pageNum;
            }
            //加入当前序列
            for(int j = 0; j < this.columnNum*columnStep; j += columnStep) {
                if(param.size() > i + j) {
                    //如果
                    //序号等于当前序列行
                    if(this.showOrder) {
                        row.add(i + j + 1);
                    }
                    Object[] arr = param.get(i + j);
                    for(Object obj : arr) {
                        row.add(obj == null ? "" : obj);
                    }
                } else {
                    Object[] blankArr = new Object[columnNum];
                    Arrays.fill(blankArr, "");
                    row.addAll(Arrays.asList(blankArr));
                }
            }
            pageContent.add(row.toArray());
        }
        this.setTotalPageNum(this.content.size());
    }

    public void setColumnNum(int columnNum) {
        if(TWO_COLUMN_NUM == columnNum) {
            this.listType = LEFT_THEN_RIGHT;
        } else if(ONE_COLUMN_NUM == columnNum) {
            this.listType = FULL_ROW;
        }
        this.columnNum = columnNum;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
        this.pageNum = versionType;
    }

    public List<List<Object[]>> getContent() {
        this.initContentByConfig(this.originContent);
        return content;
    }

    public void setListType(String listType) {
        if(LEFT_THEN_RIGHT.equals(listType)) {
            this.isFullPage = true;
        }
        this.listType = listType;
    }

}
