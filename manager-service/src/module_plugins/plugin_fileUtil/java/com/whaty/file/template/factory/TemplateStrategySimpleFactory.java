package com.whaty.file.template.factory;

import com.whaty.constant.CommonConstant;
import com.whaty.file.excel.template.strategy.ExcelTemplateOperateStrategy;
import com.whaty.file.pdf.strategy.FreemarkerToPdfTemplateAdapter;
import com.whaty.file.word.strategy.FreemarkerToWordTemplateAdapter;
import com.whaty.framework.exception.ServiceException;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.pdf.strategy.PdfTemplateStrategy;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.file.word.strategy.WordTemplateOperateStrategy;

import java.io.File;
import java.io.IOException;

/**
 * 模板策略简单工厂对象
 *
 * @author weipengsen
 */
public class TemplateStrategySimpleFactory {

    /**
     * 生产对应的模板策略
     * @param type
     * @param filePath
     * @return
     * @throws IOException
     */
    public static AbstractTemplateStrategy newInstance(String type, String filePath) throws IOException {
        switch (type) {
            case PrintConstant.PRINT_TEMPLATE_TYPE_WORD:
                return new WordTemplateOperateStrategy(filePath);
            case PrintConstant.PRINT_TEMPLATE_TYPE_PDF:
                return new PdfTemplateStrategy(filePath);
            case PrintConstant.PRINT_TEMPLATE_TYPE_EXCEL:
                return new ExcelTemplateOperateStrategy(filePath);
            case PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF:
                return new FreemarkerToPdfTemplateAdapter(filePath);
            case PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_WORD:
                return new FreemarkerToWordTemplateAdapter(filePath);
            default:
                throw new ServiceException(CommonConstant.ERROR_STR);
        }
    }

    /**
     * 生产对应的模板策略
     * @param type
     * @param file
     * @return
     * @throws IOException
     */
    public static AbstractTemplateStrategy newInstance(String type, File file) throws IOException {
        switch (type) {
            case PrintConstant.PRINT_TEMPLATE_TYPE_WORD:
                return new WordTemplateOperateStrategy(file);
            case PrintConstant.PRINT_TEMPLATE_TYPE_PDF:
                return new PdfTemplateStrategy(file);
            case PrintConstant.PRINT_TEMPLATE_TYPE_EXCEL:
                return new ExcelTemplateOperateStrategy(file);
            case PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF:
                return new FreemarkerToPdfTemplateAdapter(file);
            case PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_WORD:
                return new FreemarkerToWordTemplateAdapter(file);
            default:
                throw new ServiceException(CommonConstant.ERROR_STR);
        }
    }

}
