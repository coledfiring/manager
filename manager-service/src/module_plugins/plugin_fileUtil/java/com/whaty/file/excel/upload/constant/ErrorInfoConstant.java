package com.whaty.file.excel.upload.constant;

/**
 * 错误信息提示常量类
 * @author weipengsen
 */
public interface ErrorInfoConstant {
    /**
     * 提示文件为空
     */
    String EXCEL_FILE_IS_EMPTY = "文件为空！";
    /**
     * 提示：表头必须存在
     */
    String EXCEL_HEADER_MUST_EXIST = "必填表头必须在文件中存在";
    /**
     * 提示：表头在模板中不存在
     */
    String EXCEL_TEMPLATE_HEADER_NOT_EXIST = "文件中部分表头在模板中不存在";
    /**
     * 提示：文件的格式跟文件的内容不对应
     */
    String EXCEL_FILE_TYPE_ERROR = "文件的类型与内容不对称，无法解析";
    /**
     * 提示前缀：表头中存在两个相同的表头
     */
    String EXCEL_HEADER_IS_REPEAT = "文件中存在两个“%s”";
    /**
     * 提示：临时表命名空间为空
     */
    String TEMPORARY_NAMESPACE_IS_EMPTY = "命名空间为空";
    /**
     * 提示：临时表中数据不得为空
     */
    String TEMPORARY_DATA_IS_NULL = "%s不得为空;";
    /**
     * 提示：错误提示信息的前缀
     */
    String TEMPORARY_ERROR_INFO_PREFIX = "有以下数据因错误无法导入：";
    /**
     * 提示：数据行数操限
     */
    String WRITE_EXCEL_OVER_LIMIT = "sheet最大行数为65536行，当前导出数据为%s行，超过限制，请增加搜索条件再下载";
    /** 文件上传提示信息 **/
    /**
     * 提示：参数错误
     */
    String UPLOAD_ERROR_PARAM_ERROR = "参数错误";

}
