package com.whaty.file.template.constant;

import java.util.regex.Pattern;

/**
 * 校验常量类
 * @author weipengsen
 */
public interface TemplateValidateConstant {

    /**
     * 占位符正则
     */
    String REG_EXP_STR_SIGN = "\\$\\{.+?\\}";

    /**
     * 占位符正则模式对象
     */
    Pattern REG_PATTERN_SIGN = Pattern.compile(REG_EXP_STR_SIGN);

}
