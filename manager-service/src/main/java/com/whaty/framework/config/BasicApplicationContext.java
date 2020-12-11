package com.whaty.framework.config;

import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.framework.config.domain.ExamSystemConfig;
import com.whaty.framework.config.domain.JdbcConfig;
import com.whaty.framework.config.domain.LearnSpaceConfig;
import com.whaty.framework.config.domain.PlatformConfig;
import com.whaty.framework.config.domain.WorkSpaceConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础配置上下文
 *
 * @author weipengsen
 */
public class BasicApplicationContext {

    /**
     * 站点code与jdbc配置的集合
     */
    public static Map<String, JdbcConfig> JDBC_CONFIG_MAP = new HashMap<>();
    /**
     * 站点code与课程空间配置的集合
     */
    public static Map<String, LearnSpaceConfig> LEARN_SPACE_CONFIG_MAP = new HashMap<>();
    /**
     * 站点code与平台配置的集合
     */
    public static Map<String, PlatformConfig> PLATFORM_CONFIG_MAP = new HashMap<>();
    /**
     * 站点code与站点信息的配置集合
     */
    public static Map<String, PeWebSite> CODE_KEY_SITE_MAP = new HashMap<>();
    /**
     * 站点域名与站点信息的配置集合
     */
    public static Map<String, PeWebSite> DOMAIN_KEY_SITE_MAP = new HashMap<>();
    /**
     * 站点code与工作室配置集合
     */
    public static Map<String, WorkSpaceConfig> WORKSPACE_CONFIG_MAP = new HashMap<>();
    /**
     * 考试系统配置集合
     */
    public static Map<String, ExamSystemConfig> EXAM_SYSTEM_CONFIG_MAP = new HashMap<>();

}
