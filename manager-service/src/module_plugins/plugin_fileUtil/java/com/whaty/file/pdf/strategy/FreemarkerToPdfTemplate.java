package com.whaty.file.pdf.strategy;

import com.lowagie.text.DocumentException;
import com.whaty.file.freemarker.FreemarkerConfiguration;
import com.whaty.file.freemarker.template.FreemarkerTemplateHandler;
import com.whaty.file.pdf.HTMLConvertor;
import freemarker.template.TemplateException;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * freemarker转换为pdf的模板转换器
 *
 * @author weipengsen
 */
@Getter
public class FreemarkerToPdfTemplate {

    private final HTMLConvertor convertor;

    private final FreemarkerTemplateHandler handler;

    public FreemarkerToPdfTemplate(String templateName) throws IOException {
        this.convertor = new HTMLConvertor();
        this.handler = new FreemarkerTemplateHandler(templateName);
    }

    public FreemarkerToPdfTemplate(String templateDir, String templateName) throws IOException {
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
        this.convertor.convertFromHtml(this.handler.handleTemplate(data), out);
    }
}
