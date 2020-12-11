package com.whaty.file.word.strategy;

import com.whaty.file.template.strategy.AbstractTemplateStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * freemarker与pdf转换模板适配器
 *
 * @author weipengsen
 */
public class FreemarkerToWordTemplateAdapter implements AbstractTemplateStrategy {

    private final FreemarkerToWordTemplate template;

    private Object data;

    public FreemarkerToWordTemplateAdapter(String filePath) throws IOException {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf("."));
        String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
        this.template = new FreemarkerToWordTemplate(dir, fileName);
    }

    public FreemarkerToWordTemplateAdapter(File file) throws IOException {
        this(file.getPath());
    }

    public FreemarkerToWordTemplateAdapter(InputStream inputStream) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleTemplateFileByData(Map<String, Object> data) throws Exception {
        this.data = Collections.singletonMap("data", Collections.singletonList(data));
    }

    @Override
    public void handleTemplateFileByData(List<Map<String, Object>> data) throws Exception {
        this.data = Collections.singletonMap("data", data);
    }

    @Override
    public void exportDocToFile(String outPath) throws Exception {
        this.template.handleTemplate(this.data, new FileOutputStream(outPath));
    }

    @Override
    public void exportDocToOutputStream(OutputStream outputStream) throws Exception {
        this.template.handleTemplate(this.data, outputStream);
    }

    @Override
    public Set<String> collectSignInTemplate() {
        return new TreeSet<>();
    }

    @Override
    public String getTypeSuffix() {
        return ".doc";
    }

    @Override
    public void close() throws Exception {}
}
