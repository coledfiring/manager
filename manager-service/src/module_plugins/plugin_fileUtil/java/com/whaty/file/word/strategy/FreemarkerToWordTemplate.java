package com.whaty.file.word.strategy;

import com.lowagie.text.DocumentException;
import com.whaty.file.freemarker.FreemarkerConfiguration;
import com.whaty.file.freemarker.template.FreemarkerTemplateHandler;
import com.whaty.file.word.HTMLConvertor;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * freemarker转换为word的模板类
 *
 * @author weipengsen
 */
public class FreemarkerToWordTemplate {

    private final HTMLConvertor convertor;

    private final FreemarkerTemplateHandler handler;

    public FreemarkerToWordTemplate(String templateName) throws IOException {
        this.convertor = new HTMLConvertor();
        this.handler = new FreemarkerTemplateHandler(templateName);
    }

    public FreemarkerToWordTemplate(String templateDir, String templateName) throws IOException {
        this.convertor = new HTMLConvertor();
        this.handler = new FreemarkerTemplateHandler(FreemarkerConfiguration.loadConfig(templateDir), templateName);
    }

    /**
     * 模板转换
     * @param data
     * @param out
     * @throws IOException
     * @throws TemplateException
     * @throws DocumentException
     */
    public void handleTemplate(Object data, OutputStream out)
            throws IOException, TemplateException, DocumentException {
        this.convertor.convertFromHTML(this.handler.handleTemplate(data), out);
    }

}
