package com.whaty.file.excel.template.strategy;

import com.whaty.file.excel.upload.util.ExcelStreamUtils;
import com.whaty.file.template.constant.TemplateConstant;
import com.whaty.file.template.constant.TemplateValidateConstant;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

/**
 * excel通用模板打印策略对象
 *
 * @author weipengsen
 */
public class ExcelTemplateOperateStrategy implements AbstractTemplateStrategy {

    /**
     * workbook对象
     */
    private Workbook workbook;

    private final String fileName;
    /**
     * 已经创建了的行
     */
    private Set<Integer> createdRows = new TreeSet<>();

    public ExcelTemplateOperateStrategy(String filePath) throws IOException {
        this(new File(filePath));
    }

    public ExcelTemplateOperateStrategy(File file) throws IOException {
        fileName = file.getName();
        this.workbook = ExcelStreamUtils.loadFile(new FileInputStream(file));
    }

    @Override
    public void handleTemplateFileByData(Map<String, Object> data) throws Exception {
        Map<String, Integer[]> signMap = this.searchSignInTemplate();
        boolean hasTable = false;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!signMap.containsKey(entry.getKey()) && entry.getValue() instanceof String) {
                continue;
            }
            if (entry.getValue() instanceof String) {
                if (entry.getKey().startsWith(TemplateConstant.TEMPLATE_LIST_PREFIX)
                        || entry.getKey().startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_PREFIX)) {
                    throw new IllegalArgumentException("扩展表格型占位符的值类型不为集合");
                }
                this.replaceSignInParagraph(entry.getKey(), (String) entry.getValue(), signMap.get(entry.getKey()));
            } else if (entry.getValue() instanceof Collection) {
                if (hasTable) {
                    throw new ServiceException("一个excel模板中只能有一个扩展表格");
                }
                this.replaceSignInTable((Collection<Map<String, Object>>) entry.getValue(), signMap);
                hasTable = true;
            } else {
                throw new IllegalArgumentException("不支持的类型");
            }
        }
        this.deleteSignRowForTable(signMap);
    }

    /**
     * 删除扩展型占位符的占位符行
     * @param signMap
     */
    private void deleteSignRowForTable(Map<String, Integer[]> signMap) {
        Set<Integer> needDeleteRows = new TreeSet<>(Comparator.comparingInt(i -> -i));
        signMap.forEach((k, v) -> {
            if (v.length == 3) {
                needDeleteRows.add(v[0]);
            }
        });
        needDeleteRows.forEach(e ->
            this.workbook.getSheetAt(0).shiftRows(e + 1, this.workbook.getSheetAt(0).getLastRowNum(), -1)
        );
    }

    /**
     * 替换段落中的占位符
     * @param key
     * @param value
     * @param fixedArray
     */
    private void replaceSignInParagraph(String key, String value, Integer[] fixedArray) {
        Cell cell = this.workbook.getSheetAt(0).getRow(fixedArray[0]).getCell(fixedArray[1]);
        if (key.endsWith(TemplateConstant.TEMPLATE_PICTURE_SUFFIX)) {
            this.replacePictureForCell(cell, key, value);
        } else {
            this.replaceTextForCell(cell, key, value);
        }
    }

    /**
     * 向cell中写入照片
     * @param cell
     * @param key
     * @param value
     */
    private void replacePictureForCell(Cell cell, String key, String value) {
        throw new ServiceException("当前excel未支持写入图片");
    }

    /**
     * 向cell中写入文本
     * @param cell
     * @param key
     * @param value
     */
    private void replaceTextForCell(Cell cell, String key, String value) {
        String text = cell.getStringCellValue();
        String sign = TemplateConstant.SIGN_PREFIX + key + TemplateConstant.SIGN_SUFFIX;
        text = text.replace(sign, StringUtils.isBlank(value) ? "" : value);
        cell.setCellValue(text);
    }

    /**
     * 替换表格中的sign
     * @param data
     * @param signMap
     */
    private void replaceSignInTable(Collection<Map<String, Object>> data, Map<String, Integer[]> signMap) {
        data.forEach(e -> e.forEach((k, v) -> {
            if (signMap.containsKey(k)) {
                this.replaceSignInTable(k, (String) v, signMap.get(k));
            }
        }));
    }

    /**
     * 替换表格中的占位符
     * @param key
     * @param value
     * @param fixedArray
     */
    private void replaceSignInTable(String key, String value, Integer[] fixedArray) {
        Sheet sheet = this.workbook.getSheetAt(0);
        Row row;
        if (this.createdRows.contains(fixedArray[0] + fixedArray[2])) {
            row = sheet.getRow(fixedArray[0] + fixedArray[2]);
        } else {
            sheet.shiftRows(fixedArray[0] + fixedArray[2], sheet.getLastRowNum(), 1, true, false);
            row = sheet.createRow(fixedArray[0] + fixedArray[2]);
            this.createdRows.add(fixedArray[0] + fixedArray[2]);
        }
        Cell currentCell = row.getCell(fixedArray[1]);
        if (currentCell == null) {
            currentCell = row.createCell(fixedArray[1]);
        }
        String text = currentCell.getStringCellValue();
        if (StringUtils.isBlank(text)) {
            text = sheet.getRow(fixedArray[0]).getCell(fixedArray[1]).getStringCellValue();
        }
        String sign = TemplateConstant.SIGN_PREFIX + key + TemplateConstant.SIGN_SUFFIX;
        text = text.replace(sign, StringUtils.isBlank(value) ? "" : value);
        currentCell.setCellValue(text);
        fixedArray[2] = fixedArray[2] + 1;
    }

    @Override
    public void handleTemplateFileByData(List<Map<String, Object>> data) throws Exception {
        this.handleTemplateFileByData(data.get(0));
    }

    @Override
    public void exportDocToFile(String outPath) throws Exception {
        CommonUtils.mkDir(outPath);
        this.workbook.write(new FileOutputStream(outPath));
    }

    @Override
    public void exportDocToOutputStream(OutputStream outputStream) throws Exception {
        this.workbook.write(outputStream);
    }

    /**
     * 查找模板中的占位符
     * @return
     */
    private Map<String, Integer[]> searchSignInTemplate() {
        Map<String, Integer[]> signMap = new HashMap<>(16);
        Sheet sheet = this.workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i <= rowNum; i ++) {
            Row row = sheet.getRow(i);
            int columnNum = row.getLastCellNum();
            for (int j = 0; j < columnNum; j ++) {
                String text = row.getCell(j).getStringCellValue();
                if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(text.trim())) {
                    Matcher m = TemplateValidateConstant.REG_PATTERN_SIGN.matcher(text);
                    while (m.find()) {
                        String signKey = m.group().replace(TemplateConstant.SIGN_PREFIX, "")
                                .replace(TemplateConstant.SIGN_SUFFIX, "");
                        if (signKey.startsWith(TemplateConstant.TEMPLATE_LIST_PREFIX)) {
                            signMap.put(signKey, new Integer[]{i, j, 1});
                        } else {
                            signMap.put(signKey, new Integer[]{i, j});
                        }
                    }
                }
            }
        }
        return signMap;
    }

    @Override
    public Set<String> collectSignInTemplate() {
        return this.searchSignInTemplate().keySet();
    }

    @Override
    public String getTypeSuffix() {
        return this.fileName.substring(this.fileName.lastIndexOf("."));
    }

    @Override
    public void close() throws Exception {
        this.workbook.close();
    }
}
