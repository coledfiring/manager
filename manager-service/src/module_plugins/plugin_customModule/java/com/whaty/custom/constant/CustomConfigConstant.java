package com.whaty.custom.constant;

/**
 * 定制配置常量池
 *
 * @author weipengsen
 */
public interface CustomConfigConstant {

    /**
     * 定制配置存储的缓存key，第一个参为站点code，第二个参为url
     */
    String CUSTOM_CACHE_KEY = "custom_%s_%s";
    /**
     * 正考及格分数线
     */
    String CUSTOM_CONFIG_PASS_SCORE_LINE = "passScoreLine";
    /**
     * 毕业证号中缀生成规则
     */
    String CUSTOM_CONFIG_GRADUATE_CODE_INFLIX_RULE = "graduateInflixCodeRule";
}
