package com.whaty.framework.sqlflow.constant;

/**
 * 常量类
 * @author weipengsen
 */
public interface SqlFlowConstant {

    /**
     * 流程控制左限制符
     */
    String SQL_FLOW_LEFT_LIMIT_SIGN = "<";

    /**
     * 流程控制右限制符
     */
    String SQL_FLOW_RIGHT_LIMIT_SIGN = ">";
    /**
     * 流程控制类型if
     */
    String FLOW_TYPE_IF = "if";
    /**
     * 流程控制类型for
     */
    String FLOW_TYPE_FOR = "for";
    /**
     * 流程控制类型elseif
     */
    String FLOW_TYPE_ELSE_IF = "elseif";
    /**
     * 流程控制类型else
     */
    String FLOW_TYPE_ELSE = "else";
    /**
     * 流程控制属性，test
     */
    String FLOW_PROPERTY_KEY_TEST = "test";
    /**
     * 流程控制属性，items
     */
    String FLOW_PROPERTY_KEY_ITEMS = "items";
    /**
     * 流程控制属性，element
     */
    String FLOW_PROPERTY_KEY_ELEMENT = "element";
    /**
     * 流程控制属性，index
     */
    String FLOW_PROPERTY_KEY_INDEX = "index";
    /**
     * 流程控制，方法调用符
     */
    String FLOW_EXPRESSION_METHOD_INVOKE_SIGN = "@";
    /**
     * 流程控制，引用符
     */
    String FLOW_EXPRESSION_REFERENCE_SIGN = "\\.";
    /**
     * 方法调用中的参数分隔符
     */
    String FLOW_METHOD_INVOKE_ARG_SPLIT_STR = ",";
    /**
     * 方法调用表达式的参数包围符的左标志
     */
    String FLOW_METHOD_INVOKE_ARG_WRAPPER_LEFT_SIGN = "(";
    /**
     * 方法调用表达式的参数包围符的右标志
     */
    String FLOW_METHOD_INVOKE_ARG_WRAPPER_RIGHT_SIGN = ")";
    /**
     * 表达式中的索引引用的左包围符
     */
    String FLOW_EXPRESSION_INDEX_WRAPPER_LEFT_SIGN = "[";
    /**
     * 表达式中的索引引用的右包围符
     */
    String FLOW_EXPRESSION_INDEX_WRAPPER_RIGHT_SIGN = "]";
    /**
     * 表达式中的属性赋值等符
     */
    char FLOW_EXPRESSION_PROPERTY_EQUAL = '=';
    /**
     * 闭标签前缀
     */
    String SQL_FLOW_TAG_CLOSE_TAG_PREFIX = "</";
    /**
     * 按位与运算符
     */
    String FLOW_EXPRESSION_BIT_AND_SIGN = "&";
    /**
     * 逻辑与运算符
     */
    String FLOW_EXPRESSION_LOGIC_AND_SIGN = "&&";
    /**
     * 按位或运算符
     */
    String FLOW_EXPRESSION_BIT_OR_SIGN = "|";
    /**
     * 逻辑或运算符
     */
    String FLOW_EXPRESSION_LOGIC_OR_SIGN = "||";
    /**
     * 逻辑等运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_EQUAL = "==";
    /**
     * 逻辑不等运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_NO_EQUAL = "!=";
    /**
     * 逻辑大于运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_GREAT = ">";
    /**
     * 逻辑大于等于运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_GREAT_EQUAL = ">=";
    /**
     * 逻辑小于运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_LESS = "<";
    /**
     * 逻辑小于等与运算符
     */
    String FLOW_EXPRESSION_LOGIC_COMPUTED_LESS_EQUAL = "<=";
    /**
     * NULL字符串
     */
    String STRING_NULL = "null";
    /**
     * 逻辑否
     */
    String FLOW_EXPRESSION_LOGIC_NO = "!";
}
