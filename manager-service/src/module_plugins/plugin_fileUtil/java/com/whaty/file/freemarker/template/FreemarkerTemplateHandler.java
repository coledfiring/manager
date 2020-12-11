package com.whaty.file.freemarker.template;

import com.whaty.file.freemarker.FreemarkerConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * freemarker的模板处理器
 *
 * @author weipengsen
 */
public class FreemarkerTemplateHandler {

    private Template template;

    private final static String FREEMARKER_SUFFIX = "%s.ftl";

    public FreemarkerTemplateHandler(String templateName) throws IOException {
        this(FreemarkerConfiguration.getConfiguration(), templateName);
    }

    public FreemarkerTemplateHandler(Configuration configuration, String templateName) throws IOException {
        this.template = configuration.getTemplate(String.format(FREEMARKER_SUFFIX, templateName));
    }

    /**
     * 将数据写入freemarker模板
     * @param data
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String handleTemplate(Object data) throws IOException, TemplateException {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(bao)) {
            this.template.process(data, writer);
            return new String(bao.toByteArray());
        }
    }
}
