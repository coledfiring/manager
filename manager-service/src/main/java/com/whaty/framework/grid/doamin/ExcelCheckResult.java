package com.whaty.framework.grid.doamin;

import lombok.Data;

/**
 * excel导入校验结果
 *
 * @author suoqiangqiang
 */
@Data
public class ExcelCheckResult {
    /**
     * 错误信息显示列下标
     */
    int errorInfoCellIndex;
    /**
     * 错误信息行数
     */
    int errorRows;
    /**
     * 是否有错误信息
     */
    boolean isError;
    /**
     * 数据起始行
     */
    byte startRowIndex;

    public ExcelCheckResult(int errorInfoCellIndex, int errorRows, boolean isError, byte startRowIndex) {
        this.errorInfoCellIndex = errorInfoCellIndex;
        this.errorRows = errorRows;
        this.isError = isError;
        this.startRowIndex = startRowIndex;
    }

    public ExcelCheckResult(boolean isError) {
        this.isError = isError;
    }
}
