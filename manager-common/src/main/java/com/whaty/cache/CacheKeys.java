package com.whaty.cache;

/**
 * 定义平台中所有缓存key
 * @author hanshichao
 */
public interface CacheKeys {

    /** 通用成教缓存key统一前缀，实现缓存分片 **/
    String CACHE_NAME = "redis_tycj";

    /** 当前缓存中已经存入的缓存keys的key **/
    CacheKey CURRENT_CACHED_KEYS = new CacheKey("cache_key");

    /** 登陆保存用户信息的key **/
    CacheKey LOGIN_CACHE_KEYS = new CacheKey("%s@@<%s>@@");

    /** 生成毕业证号流水号时使用的学生排序规则的key **/
    CacheKey GRADUATE_NO_SEARIAL_RULE_KEY = new CacheKey("%s::graduate_no_serial_rule_key");

    /** 生成学位证号流水号时使用的学生排序规则的key **/
    CacheKey DEGREE_NO_SEARIAL_RULE_KEY = new CacheKey("%s::degree_no_serial_rule_key");

    /** 号码生成规则缓存key **/
    CacheKey NUMBER_GENERATE_CACHE_KEY = new CacheKey("%s::%s");

    /** 新框架中已经存入的缓存keys的key **/
    CacheKey CORE_FRAME_CACHED_KEYS = new CacheKey("com_whaty_store_defaultCache");

    /** 统计图表的缓存key **/
    CacheKey STATISTICS_CHART_CACHE_KEY = new CacheKey("statistics_chart_cache_key_s%_s%");

    /** 系统常量的缓存key **/
    CacheKey SYSTEM_VARIABLES_CACHE_KEY = new CacheKey("system_variables_cache_key_%s_%s");
}
