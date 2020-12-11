package com.whaty.file.template.constant;

/**
 * 常量类
 * @author weipengsen
 */
public interface TemplateConstant {

    /**
     * 文件格式分隔符
     */
    String FILE_TYPE_SIGN = ".";
    /**
     * 排列占位符的前缀
     */
    String TEMPLATE_LIST_PREFIX = "l_";
    /**
     * 是否纵向扩展，并且支持多栏扩展，左右左右逐行填充
     */
    String TEMPLATE_LIST_MORE_COLUMN_PREFIX = "lmc_";
    /**
     * 是否纵向扩展，并且支持多栏扩展，左侧先填充然后填充右侧
     */
    String TEMPLATE_LIST_MORE_COLUMN_LEFT_FULL_PREFIX = "lmcl_";
    /**
     * 是否横向写入数据标识位
     */
    String TEMPLATE_LIST_MARK_PREFIX = "lm_";
    /**
     * 图片占位符的后缀
     */
    String TEMPLATE_PICTURE_SUFFIX = "_p";
    /**
     * 中文日期后缀
     */
    String TEMPLATE_CHINESE_DATE_SUFFIX = "_cd";
    /**
     * 中文数字后缀
     */
    String TEMPLATE_CHINESE_NUM_SUFFIX = "_cn";
    /**
     * 占位符前缀
     */
    String SIGN_PREFIX = "${";
    /**
     * 占位符后缀
     */
    String SIGN_SUFFIX = "}";
    /**
     * 图片默认宽
     */
    int PICTURE_DEFAULT_WIDTH = 70;
    /**
     * 图片默认高
     */
    int PICTURE_DEFAULT_HEIGHT = 93;

    /**
     * 横向遍历标识key
     */
    String LATERAL_MARK = "lateralMark";
    /**
     * 模板策略简单工厂类的spring bean name
     */
    String TEMPLATE_STRATEGY_SIMPLE_FACTORY_SPRING_BEAN = "templateStrategySimpleFactory";

    /**
     * 占位符中的参数分割符
     */
    String PARAM_SPLIT_SIGN = "?";
}
