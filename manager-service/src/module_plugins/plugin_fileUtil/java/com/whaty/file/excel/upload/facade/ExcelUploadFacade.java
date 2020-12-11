package com.whaty.file.excel.upload.facade;

import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.file.excel.upload.service.ExcelUploadService;
import com.whaty.framework.exception.UncheckException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel导入外观模式对象
 *
 * @author weipengsen
 */
@Lazy
@Component("excelUploadFacade")
public class ExcelUploadFacade {

    @Resource(name = "excelUploadService")
    private ExcelUploadService excelUploadService;

    /**
     * 一站式导入外观接口
     * @param file
     * @param headers
     * @param checkList
     * @param sqlList
     * @param titleRowNum
     * @param infoPrefix
     * @return
     */
    public Map<String, Object> doUploadFile(File file, Object[] headers, List<String[]> checkList,
                                            List<String> sqlList, int titleRowNum, String infoPrefix) {
        Map<String, Object> params = new HashMap<>(6);
        params.put(ExcelConstant.UPLODA_FILE, file);
        params.put(ExcelConstant.TEMPLATE_HEADER, headers);
        params.put(ExcelConstant.CHECK_LIST, checkList);
        params.put(ExcelConstant.SQL_LIST, sqlList);
        params.put(ExcelConstant.TITLE_ROW_NUM, titleRowNum);
        params.put(ExcelConstant.INFO_PREFIX, infoPrefix);
        Map<String, Object> resultMap;
        try {
            resultMap = this.excelUploadService.doUploadFile(params, ExcelConstant.INSERT_SUCCESS, true);
        } catch (StreamOperateException e) {
            throw new UncheckException(e);
        }
        return resultMap;
    }

    /**
     * 生成excel文件并写入流接口
     * @param out
     * @param selectMap
     * @param headers
     * @param title
     * @param data
     */
    public void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers,
                                      String title, List<Object[]> data) {
        try (Workbook book = this.excelUploadService.downBatchTemplate(headers,
                title, selectMap, data)) {
            book.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }

}
