package com.whaty.framework.aop.operatelog.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置读取工具类
 * @author weipengsen
 */
public class OperateRecordConfigUtils {

    private static final String CONFIG_PATH = "operateRecord/operateRecord.properties";

    private static Properties prop;

    static {
        prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PATH));
        } catch (IOException e) {
            throw new RuntimeException("error when loaded the config that the operate collect needs", e);
        }
    }

    /**
     * 获取配置中的值
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

}
