package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;

import java.util.Objects;

/**
 * 通用页面打印配置，副标题
 * @author
 */
@Data
public class PrintSubTitle extends AbstractBean {

    private static final long serialVersionUID = -5438224150877682853L;
    /**
     * id
     */
    private String id;
    /**
     * 副标题名
     */
    private String header;
    /**
     * 所在行数
     */
    private int rowNum;
    /**
     * 所在列数
     */
    private int columnNum;
    /**
     * 副标题占用列数
     */
    private int colspan;
    /**
     * 副标题值
     */
    private String value;

    /**
     * 设置副标题名、值和占用单元格数
     * @param header
     * @param colspan
     * @param value
     */
    public PrintSubTitle(String header, int rowNum, int columnNum, int colspan, String value) {
        this.header = header;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.colspan = colspan;
        this.value = value;
    }

}
