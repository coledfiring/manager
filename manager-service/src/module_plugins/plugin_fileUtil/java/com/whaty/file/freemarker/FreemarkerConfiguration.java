package com.whaty.file.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * freemarker的配置类
 *
 * @author weipengsen
 */
@Component
public class FreemarkerConfiguration implements InitializingBean {

    private static Configuration CONFIGURATION;

    @Override
    public void afterPropertiesSet() throws Exception {
        CONFIGURATION = loadConfig(FreemarkerConfiguration.class.getResource("/").getPath() + "/freemarker");
    }

    /**
     * 加载配置
     * @return
     * @throws IOException
     */
    public static Configuration loadConfig(String path) throws IOException {
        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(new File(path));
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return config;
    }

    public static Configuration getConfiguration() {
        return CONFIGURATION;
    }
}
