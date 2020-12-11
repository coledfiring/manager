package com.whaty.framework.sqlflow.constant;

import java.util.regex.Pattern;

/**
 * 流程控制校验常量类
 * @author weipengsen
 */
public interface SqlFlowValidateConstant {

    /**
     * 流程控制开或闭标签部分正则
     */
    String SQL_FLOW_OPEN_OR_CLOSE_TAG_FORMAT_STR = "<(/)?\\s?[if|for][\\s\\S]*?>";
    /**
     * 流程控制开或闭标签正则匹配模式对象
     */
    Pattern SQL_FLOW_OPEN_OR_CLOSE_TAG_FORMAT_PATTERN = Pattern.compile(SQL_FLOW_OPEN_OR_CLOSE_TAG_FORMAT_STR);
    /**
     * 流程控制开或闭标签部分正则
     */
    String SQL_FLOW_IF_TAG_FORMAT_STR = "<(/)?\\s?[if|for|elseif|else][\\s\\S]*?>";
    /**
     * 流程控制开或闭标签正则匹配模式对象
     */
    Pattern SQL_FLOW_IF_TAG_FORMAT_PATTERN = Pattern.compile(SQL_FLOW_IF_TAG_FORMAT_STR);

    /**
     * 变量占位符正则匹配字符串(非贪婪)
     */
    String SQL_ARG_SIGN_FORMAT_STR = "\\$\\{[\\s\\S]*?\\}";
    /**
     * 变量占位符正则匹配模式对象
     */
    Pattern SQL_ARG_SIGN_FORMAT_PATTERN = Pattern.compile(SQL_ARG_SIGN_FORMAT_STR);
    /**
     * 匹配标签中的属性表达式的正则字符串
     */
    String SQL_FLOW_PROPERTY_FORMAT_STR = "\\w+\\s*=\\s*(('[\\s\\S]+?')|(\"[\\s\\S]+?\"))";
    /**
     * 匹配标签中的属性表达式的正则模式对象
     */
    Pattern SQL_FLOW_PROPERTY_FORMAT_PATTERN = Pattern.compile(SQL_FLOW_PROPERTY_FORMAT_STR);

    /**
     * 表达式为字符串的正则校验
     */
    String FLOW_EXPRESSION_STRING_FORMAT = "['\"][\\s\\S]*['\"]";
    /**
     * 整数的正则校验
     */
    String FLOW_EXPRESSION_INTEGER_FORMAT = "[0-9]+";
    /**
     * 小数的正则校验
     */
    String FLOW_EXPRESSION_FLOAT_FORMAT = "[0-9]+\\.[0-9]+";
    /**
     * 逻辑运算符正则校验
     */
    String FLOW_EXPRESSION_LOGIC_SIGN_FORMAT = "\\s*(&&|\\|\\||&|\\|)\\s*";
    /**
     * 逻辑计算符正则校验
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTE_FORMAT = "\\s*(!=|==|>|>=|<|<=)\\s*";
    /**
     * 包含逻辑计算符的正则校验
     */
    String FLOW_EXPRESSION_HAVE_LOGIC_COMPUTE_FORMAT = "[\\s\\S]*(!=|==|>|>=|<|<=)[\\s\\S]*";
}
