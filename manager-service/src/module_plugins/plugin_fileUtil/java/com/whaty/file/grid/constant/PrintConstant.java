package com.whaty.file.grid.constant;

import java.util.regex.Pattern;

/**
 * 常量
 * @author weipengsen
 */
public interface PrintConstant {

    /**
     * 模板id
     */
    String PARAM_PRINT_ID = "printId";
    /**
     * 参数命名空间
     */
    String PARAM_NAMESPACE = "namespace";
    /**
     * 主子sql的分隔符
     */
    String MAIN_SUB_SQL_SPLIT_SIGN = ";";

    /**
     * 打印模板的搜索类型，使用一个sql查询
     */
    String SEARCH_TYPE_ONE_SQL = "1";
    /**
     * 打印模板的搜索类型，使用多个sql查询
     */
    String SEARCH_TYPE_MORE_SQL = "2";

    /**
     * 模板查询sql中的参数占位符正则表达式
     */
    String REG_EXP_STR_ARG_SIGN = "\\$\\{.+\\|.+\\}";
    /**
     * 模板查询sql中的参数占位符正则模式
     */
    Pattern REG_PATTERN_ARG_SIGN = Pattern.compile(REG_EXP_STR_ARG_SIGN);
    /**
     * 模板查询sql中的参数占位符前缀
     */
    String SEARCH_SQL_ARG_SIGN_PREFIX = "${";
    /**
     * 模板查询sql中的参数占位符后缀
     */
    String SEARCH_SQL_ARG_SIGN_SUFFIX = "}";
    /**
     * 模板查询sql中的参数占位符分割
     */
    String SEARCH_SQL_ARG_SIGN_SPLIT = "\\|";
    /**
     * 默认的图片路径
     */
    String DEFAULT_PICTURE_PATH = "/images/student/default_picture.jpg";
    /**
     * 打印模板类型，word
     */
    String PRINT_TEMPLATE_TYPE_WORD = "word";
    /**
     * 打印模板类型，word
     */
    String PRINT_TEMPLATE_TYPE_PDF = "pdf";
    /**
     * 打印模板类型，word
     */
    String PRINT_TEMPLATE_TYPE_EXCEL = "excel";

    /**
     * 打印模板类型，freemarkerToPdf
     */
    String PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF = "freemarkerToPdf";

    /**
     * 打印模板类型，freemarkerToWord
     */
    String PRINT_TEMPLATE_TYPE_FREEMARKER_TO_WORD = "freemarkerToWord";
}
