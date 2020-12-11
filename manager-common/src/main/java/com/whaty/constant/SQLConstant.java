package com.whaty.constant;

/**
 * sql语句中常用常量
 *
 * @author weipengsen
 */
public interface SQLConstant {
    /**
     * gbk编码转换语句
     */
    String HQL_GBK_CONVERT_STR = " convert(%s, 'gbk') ";

    /**
     * 变量符
     */
    String VARIABLE_SIGN = ":=";
    /**
     * 转换后的
     */
    String CONVERT_VARIABLE_SIGN = "/*'*/:=/*'*/";

}
