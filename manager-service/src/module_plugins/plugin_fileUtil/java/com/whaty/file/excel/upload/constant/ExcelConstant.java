package com.whaty.file.excel.upload.constant;

/**
 * 常量类
 */
public interface ExcelConstant {

    /**
     * 选择框提示字段
     */
    String SELECT_TIP = "请选择";
    /**
     * 隐藏域名
     */
    String HIDDEN_SHEET = "hiddenSheet";
    /**
     * 默认选择框填充数量
     */
    int SELECT_FULL_NUM = 10000;
    /**
     * 必填符号
     */
    String MUST_SIGN = "*";
    /**
     * 如果有成功的返回错误信息，插入成功的
     */
    String INSERT_SUCCESS = "insertSuccess";
    /**
     * 如果有错误所有都不插入
     */
    String IF_FAILURE_NO_INSERT = "ifFailureNoInsert";
    /**
     * excel表格第一个sheet名
     */
    String FIRST_SHEET = "sheet1";
    /**
     * excel表格最大行数
     */
    int MAX_LIMIT_ROW = 65536;
    /**
     * 错误信息表头
     */
    String ERROR_INFO_HEADER = "错误信息";
    /** 文件上传参数 **/
    /**
     * 上传的文件
     */
    String UPLODA_FILE = "upload";
    /**
     * 模板表头
     */
    String TEMPLATE_HEADER = "headers";
    /**
     * 标题行数
     */
    String TITLE_ROW_NUM = "titleRowNum";
    /**
     * 自定义检查集合
     */
    String CHECK_LIST = "checkList";
    /**
     * sql执行集合
     */
    String SQL_LIST = "sqlList";
    /**
     * 提示信息前缀
     */
    String INFO_PREFIX = "infoPrefix";
    /**
     * 每行错误信息
     */
    String UPLOAD_ERROR_INFO = "第%s行：%s";
    /**
     * 成功导入，失败返回信息模式下提示中的计数文本
     */
    String COUNT_INFO = "共%s条，成功%s条数据，失败%s条数据";
    /**
     * 失败全部返回模式下提示中的计数文本
     */
    String SUCCESS_COUNT_INFO = "共导入%s条数据";
    /**
     * 错误信息对照表路径
     */
    String ERROR_FILE_INCOMING_PATH = "/incoming/tempFile/errorFile";
    /**
     * 加载文件到临时表返回map中workbook对象对应的key
     */
    String LOAD_FILE_PARAM_BOOK = "book";
    /**
     * 加载文件到临时表返回map中插入临时表数对应的key
     */
    String LOAD_FILE_PARAM_COUNT = "count";
    /**
     * 加载文件到临时表返回map中当前文件中的表头对应的key
     */
    String LOAD_FILE_PARAM_CURRENT_HEADER = "headers";

    /**
     * 导入文件返回的map中提示信息的参数key
     */
    String UPLOAD_RETURN_PARAM_INFO = "msg";
    /**
     * 导入文件返回的map中提示信息的成功数量
     */
    String UPLOAD_RETURN_SUCCESS_NUM = "successNum";
    /**
     * 导入文件返回的map中错误信息的参数key
     */
    String UPLOAD_RETURN_ERROR_INFO = "error";
    /**
     * 导入文件返回的map中生成文件路径的参数key
     */
    String UPLOAD_RETURN_FILE_PATH_INFO = "url";
    /**
     * 错误信息中的换行符
     */
    String NEW_LINE_SIGN = "<br/>";
    /**
     * xls文件格式后缀
     */
    String XLS_FILE_TYPE = ".xls";

    /**
     * 写入数据的Excel 高基报表路径
     */
    String HIGH_REPORT_PATH = "/incoming/tempFile/highReport/";

    /**
     * 读取excel模板文件单元格中是否是表达式标识
     */
    String XLS_PARAM_TYPE = "paramType";
    /**
     * 指定模板写入excel的参数类型 sql动态获取值
     */
    String PARAM_TYPE_sql = "3";
    /**
     * 指定模板写入excel的参数类型 计算指定列的和
     */
    String PARAM_TYPE_COLUMN_NUM = "2";
    /**
     * 指定模板写入excel的参数类型 计算指定行的和
     */
    String PARAM_TYPE_ROW_NUM = "1";
    /**
     * 跳转到路由名
     */
    String TO_ROUTER_NAME = "routerName";
}
