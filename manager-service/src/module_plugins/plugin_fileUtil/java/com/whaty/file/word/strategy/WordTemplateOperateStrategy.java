package com.whaty.file.word.strategy;

import com.whaty.file.template.constant.TemplateConstant;
import com.whaty.file.template.constant.TemplateValidateConstant;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.file.word.helper.WordOperateHelper;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static com.whaty.file.template.constant.TemplateConstant.LATERAL_MARK;
import static com.whaty.file.template.constant.TemplateConstant.TEMPLATE_LIST_MARK_PREFIX;

/**
 * word模板辅助类
 *
 * @author weipengsen
 */
public class WordTemplateOperateStrategy implements AbstractTemplateStrategy {

    /**
     * 段落型占位符索引数组的长度
     */
    private final static int PARAGRAPH_TYPE_INDEX_LENGTH = 2;

    /**
     * 列表型占位符索引数组的长度
     */
    private final static int TABLE_TYPE_INDEX_LENGTH = 5;

    /**
     * 多栏列表型占位符索引数组长度
     */
    private final static int TABLE_MORE_COLUMN_TYPE_INDEX_LENGTH = 6;

    /**
     * 嵌套列表型占位符索引数组的长度
     */
    private final static int NEST_TABLE_TYPE_INDEX_LENGTH = 8;

    /**
     * 表格占位符索引长度数组
     */
    private final static int TABLE_SIGN_LENGTH = 2;

    /**
     * 嵌套表格占位符索引长度数组
     */
    private final static int NEST_TABLE_SIGN_LENGTH = 5;

    /**
     * word操作复制类
     */
    private WordOperateHelper wordOperateHelper;

    /**
     * 搜索的占位符映射表
     */
    private Map<String, SignIndex> signMap = new HashMap<>(16);
    /**
     * 搜索的占位符中的参数映射表
     */
    private Map<String, String> signParams = new HashMap<>(16);
    /**
     * 新老占位符映射表
     */
    private Map<String, String> oldNewSignMap = new HashMap<>(16);

    private String fileName;

    public WordTemplateOperateStrategy(String filePath) throws IOException {
        this(new File(filePath));
    }

    public WordTemplateOperateStrategy(File file) throws IOException {
        this(new FileInputStream(file));
        this.fileName = file.getName();
    }

    public WordTemplateOperateStrategy(InputStream input) throws IOException {
        this.wordOperateHelper = new WordOperateHelper(input);
        this.fileName = null;
    }

    public WordTemplateOperateStrategy(XWPFDocument document) throws IOException {
        this.wordOperateHelper = new WordOperateHelper(document);
        this.fileName = null;
    }

    /**
     * 根据data生成模板
     *
     * @param data
     */
    @Override
    public void handleTemplateFileByData(Map<String, Object> data) {
        if (this.validateTemplateHasSign()) {
            this.searchSignInTemplate();
            this.replaceSignInTemplate(data);
        }
    }

    @Override
    public void handleTemplateFileByData(List<Map<String, Object>> data) throws IOException, XmlException {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("argument 'data' is empty");
        }
        if (data.size() == 1) {
            this.handleTemplateFileByData(data.get(0));
            return;
        }
        if (this.validateTemplateHasSign()) {
            this.searchSignInTemplate();
            XWPFDocument document = new XWPFDocument(this.wordOperateHelper.getDocument().getPackage());
            List<String> documents = new LinkedList<>();
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    this.replaceSignInTemplate(data.get(i));
                    this.getWordOperateHelper().breakPage();
                } else {
                    WordTemplateOperateStrategy helper = this.generateStrategy(document);
                    helper.replaceSignInTemplate(data.get(i));
                    if (i != data.size() - 1) {
                        helper.wordOperateHelper.breakPage();
                    }
                    documents.add(helper.getDocument().getDocument().getBody().xmlText());
                }
            }
            if (CollectionUtils.isNotEmpty(documents)) {
                this.wordOperateHelper.mergeDocument(documents);
            }
        }
    }

    /**
     * 生成新的策略
     * @param document
     * @return
     * @throws IOException
     */
    public WordTemplateOperateStrategy generateStrategy(XWPFDocument document) throws IOException {
        WordTemplateOperateStrategy helper = new WordTemplateOperateStrategy(new XWPFDocument(document.getPackage()));
        helper.setSignMap(this.getSignMap());
        helper.setOldNewSignMap(this.getOldNewSignMap());
        helper.setSignParams(this.getSignParams());
        return helper;
    }

    /**
     * 获取docment对象
     * @return
     */
    public XWPFDocument getDocument() {
        return this.wordOperateHelper.getDocument();
    }

    @Override
    public void exportDocToFile(String outPath) throws Exception {
        this.wordOperateHelper.exportDocToFile(outPath);
    }

    @Override
    public void exportDocToOutputStream(OutputStream outputStream) throws Exception {
        this.wordOperateHelper.exportDocToOutputStream(outputStream);
    }

    /**
     * 寻找模板中所有的占位符
     *
     * @return
     */
    @Override
    public Set<String> collectSignInTemplate() {
        return this.searchSignInTemplate().keySet();
    }

    @Override
    public String getTypeSuffix() {
        return this.fileName != null ? this.fileName.substring(this.fileName.lastIndexOf(".")) : ".docx";
    }

    /**
     * 寻找模板中所有的占位符
     *
     * @return
     */
    public Map<String, SignIndex> searchSignInTemplate() {
        this.searchSignInParagraph();
        this.searchSignInTable();
        return this.signMap;
    }

    /**
     * 寻找模板段落中的占位符
     *
     */
    private void searchSignInTable() {
        this.searchSignInTable(this.wordOperateHelper.getTablesOfDoc(), null);
    }

    /**
     * 遍历表格table的占位符
     *
     * @param tables
     * @param nestSignArr
     */
    private void searchSignInTable(List<XWPFTable> tables, Integer[] nestSignArr) {
        //遍历所有的表
        for (int i = 0; i < tables.size(); i++) {
            List<XWPFTableRow> rows = tables.get(i).getRows();
            //遍历表中所有的行
            for (int j = 0; j < rows.size(); j++) {
                List<XWPFTableCell> cells = rows.get(j).getTableCells();
                //遍历表中所有的单元格
                for (int z = 0; z < cells.size(); z++) {
                    List<XWPFParagraph> paras = cells.get(z).getParagraphs();
                    //遍历表中所有的段落
                    for (int x = 0; x < paras.size(); x++) {
                        String text = paras.get(x).getText();
                        if (StringUtils.isNotBlank(text)) {
                            List<XWPFRun> runs = paras.get(x).getRuns();
                            //遍历段落内所有的运行
                            for (int y = 0; y < runs.size(); y++) {
                                String targetText = runs.get(y).getText(runs.get(y).getTextPosition());
                                if (StringUtils.isNotBlank(targetText) && StringUtils.isNotBlank(targetText.trim())) {
                                    Matcher m = TemplateValidateConstant.REG_PATTERN_SIGN.matcher(targetText);
                                    while (m.find()) {
                                        String sign = m.group();
                                        String column = sign.replace(TemplateConstant.SIGN_PREFIX, "")
                                                .replace(TemplateConstant.SIGN_SUFFIX, "");
                                        Integer[] signArr;
                                        column = this.extractParamsFromSign(column);
                                        if (column.startsWith(TemplateConstant
                                                .TEMPLATE_LIST_MORE_COLUMN_LEFT_FULL_PREFIX)
                                                || column.startsWith(TemplateConstant
                                                    .TEMPLATE_LIST_MORE_COLUMN_PREFIX)) {
                                            signArr = new Integer[] {i, j, z, x, y, 0};
                                            if (this.signMap.containsKey(column)) {
                                                signArr = this.signMap.get(column).peek();
                                                signArr[5] = z - signArr[2];
                                            }
                                        } else {
                                            signArr = new Integer[] {i, j, z, x, y};
                                        }
                                        this.signMap.put(column, new SignIndex(column,
                                                (Integer[]) ArrayUtils.addAll(nestSignArr, signArr)));
                                    }
                                }
                            }
                        }
                    }

                    //递归搜索占位符
                    searchSignInTable(cells.get(z).getTables(), new Integer[]{i, j, z});
                }
            }
        }
    }

    /**
     * 寻找模板表格中的占位符
     *
     */
    private void searchSignInParagraph() {
        List<XWPFParagraph> paras = this.wordOperateHelper.getParagraphsOfDoc();
        for (int i = 0; i < paras.size(); i++) {
            String text = paras.get(i).getText();
            if (StringUtils.isNotBlank(text)) {
                List<XWPFRun> runs = paras.get(i).getRuns();
                //遍历段落内所有的运行
                for (int j = 0; j < runs.size(); j++) {
                    String targetText = runs.get(j).getText(runs.get(j).getTextPosition());
                    if (StringUtils.isNotBlank(targetText) && StringUtils.isNotBlank(targetText.trim())) {
                        Matcher m = TemplateValidateConstant.REG_PATTERN_SIGN.matcher(targetText);
                        while (m.find()) {
                            String sign = m.group();
                            String column = sign.replace(TemplateConstant.SIGN_PREFIX, "")
                                    .replace(TemplateConstant.SIGN_SUFFIX, "");
                            if (column.startsWith(TemplateConstant.TEMPLATE_LIST_PREFIX)
                                    || column.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_PREFIX)
                                    || column.startsWith(TemplateConstant.TEMPLATE_LIST_MARK_PREFIX)) {
                                throw new ServiceException("列表占位符不能出现在段落里");
                            }
                            column = this.extractParamsFromSign(column);
                            if (!this.signMap.containsKey(column)) {
                                this.signMap.put(column, new SignIndex(column, new Integer[] {i, j}));
                            } else {
                                this.signMap.get(column).push(new Integer[] {i, j});
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 从标签中提取参数
     * @param column
     * @return
     */
    private String extractParamsFromSign(String column) {
        int index = column.indexOf(TemplateConstant.PARAM_SPLIT_SIGN);
        if (index >= 0) {
            String paramString = column.substring(index + 1);
            this.signParams.put(column.substring(0, index), paramString);
            this.oldNewSignMap.put(column.substring(0, index), column);
            return column.substring(0, index);
        }
        return column;
    }

    /**
     * 替换模板中的占位符
     *
     * @param data
     */
    public void replaceSignInTemplate(Map<String, Object> data) {
        Map<String, Integer> listKeyUseNum = new HashMap<>(16);
        data.forEach((k, v) -> {
            if (v instanceof String) {
                if (!this.signMap.containsKey(k)) {
                    return;
                }
                SignIndex signIndex = this.signMap.get(k);
                signIndex.forEach(signArr -> {
                    if (signArr.length == PARAGRAPH_TYPE_INDEX_LENGTH) {
                        XWPFParagraph para = this.wordOperateHelper.getParagraphAt(signArr[0]);
                        this.replaceSignInParagraph(k, (String) v, para, signArr[1]);
                    } else if (signArr.length == TABLE_TYPE_INDEX_LENGTH) {
                        this.replaceSignInTableNoList(k, (String) v, signArr);
                    } else if (signArr.length == NEST_TABLE_TYPE_INDEX_LENGTH) {
                        this.replaceSignInNestTableNoList(k, (String) v, signArr);
                    }
                });
            } else if (v instanceof Collection
                    && CollectionUtils.isNotEmpty((Collection) v)) {
                this.replaceSignsInTable((Collection<Map>) v, listKeyUseNum);
            } else if (v != null) {
                throw new IllegalArgumentException();
            }
        });
        this.clearSignNoExistInData(data);
    }

    /**
     * 清除没有用到的占位符
     *
     * @param data
     */
    private void clearSignNoExistInData(Map<String, Object> data) {
        List<String> dataSign = new ArrayList<>();
        data.forEach((k, v) -> {
            if (v instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection) v)) {
                    Object row = ((Collection) v).iterator().next();
                    ((Map<String, String>) row).forEach((ik, iv) -> dataSign.add(ik));
                }
            } else if (v instanceof String) {
                dataSign.add(k);
            }
        });
        this.signMap.forEach((k, v) -> {
            if (!dataSign.contains(k)) {
                v.forEach(signArr -> clearSign(k, signArr));
            }
        });
    }

    /**
     * 清除指定的占位符
     *
     * @param signArr
     */
    private void clearSign(String key, Integer[] signArr) {
        if (signArr.length == PARAGRAPH_TYPE_INDEX_LENGTH) {
            this.replaceSignInParagraph(key, "", this.wordOperateHelper.getParagraphAt(signArr[0]), signArr[1]);
        } else if (signArr.length == TABLE_TYPE_INDEX_LENGTH) {
            this.replaceSignInTableNoList(key, "", signArr);
        } else if (signArr.length == NEST_TABLE_TYPE_INDEX_LENGTH) {
            this.replaceSignInNestTableNoList(key, "", signArr);
        } else if (signArr.length == TABLE_MORE_COLUMN_TYPE_INDEX_LENGTH) {
            this.replaceSignInTableNoList(key, "", signArr);
            Integer[] moreArr = new Integer[signArr.length];
            System.arraycopy(signArr, 0, moreArr, 0, signArr.length);
            moreArr[2] = moreArr[2] + moreArr[5];
            this.replaceSignInTableNoList(key, "", moreArr);
        }
    }

    /**
     * 替换非列表形式的表格中的占位符
     *
     * @param key
     * @param value
     * @param signArr
     */
    private void replaceSignInTableNoList(String key, String value, Integer[] signArr) {
        XWPFRun run = this.wordOperateHelper.getTablesOfDoc().get(signArr[0]).getRow(signArr[1]).getCell(signArr[2])
                .getParagraphs().get(signArr[3]).getRuns().get(signArr[4]);
        String text = run.getText(run.getTextPosition());
        this.replacePictureOrTextInRun(run, key, value, text);
    }

    /**
     * 替换非列表形式的嵌套表格中的占位符
     *
     * @param key
     * @param value
     * @param signArr
     */
    private void replaceSignInNestTableNoList(String key, String value, Integer[] signArr) {
        XWPFTableCell cell = this.wordOperateHelper.getTablesOfDoc().get(signArr[0])
                .getRow(signArr[1]).getCell(signArr[2]);
        XWPFRun run = cell.getTables().get(signArr[3]).getRow(signArr[4])
                .getCell(signArr[5]).getParagraphs().get(signArr[6]).getRuns().get(signArr[7]);
        String text = run.getText(run.getTextPosition());
        this.replacePictureOrTextInRun(run, key, value, text);
    }

    /**
     * 替换列表中的占位符
     *
     * @param rows
     * @param listKeyUseNum
     */
    private void replaceSignsInTable(Collection<Map> rows, Map<String, Integer> listKeyUseNum) {
        Integer[] indexArrRow = new Integer[TABLE_SIGN_LENGTH];
        Integer[] indexArrNestRow = new Integer[NEST_TABLE_SIGN_LENGTH];
        //标识是否横向遍历写入数据
        Map<String, String> markMap = new HashMap<>(16);
        for (Map<String, String> row : rows) {
            row.forEach((k, v) -> {
                if (k.contains(TEMPLATE_LIST_MARK_PREFIX)) {
                    markMap.put(LATERAL_MARK, v);
                }
            });
            row.forEach((k, v) -> {
                if (!this.signMap.containsKey(k)) {
                    return;
                }
                SignIndex signIndex = this.signMap.get(k);
                int useNum = listKeyUseNum.get(k) == null ? 0 : listKeyUseNum.get(k);
                signIndex.forEach(signArr -> {
                    if (signArr.length == TABLE_TYPE_INDEX_LENGTH
                            || signArr.length == TABLE_MORE_COLUMN_TYPE_INDEX_LENGTH) {
                        System.arraycopy(signArr, 0, indexArrRow, 0, TABLE_SIGN_LENGTH);
                        this.replaceSignInTable(k, v, signArr, useNum, rows.size());
                    } else if (signArr.length == NEST_TABLE_TYPE_INDEX_LENGTH) {
                        System.arraycopy(signArr, 0, indexArrNestRow, 0, NEST_TABLE_SIGN_LENGTH);
                        this.replaceSignInNestTable(k, v, signArr, useNum, markMap);
                    }
                });
                listKeyUseNum.computeIfPresent(k, (e, o) -> o + 1);
                listKeyUseNum.putIfAbsent(k, 1);
            });
        }

        if (indexArrRow[0] != null && indexArrRow[1] != null) {
            this.wordOperateHelper.getTablesOfDoc().get(indexArrRow[0]).removeRow(indexArrRow[1]);
        }
        if (indexArrNestRow[0] != null && indexArrNestRow[1] != null
                && indexArrNestRow[2] != null && indexArrNestRow[3] != null && indexArrNestRow[4] != null) {
            List<XWPFTableCell> cells = this.wordOperateHelper.getTablesOfDoc().get(indexArrNestRow[0])
                    .getRow(indexArrNestRow[1])
                    .getTableCells();
            for (XWPFTableCell xwpfTableCell : cells) {
                xwpfTableCell.getTables().get(indexArrNestRow[3]).removeRow(indexArrNestRow[4]);
            }
        }
    }

    /**
     * 替换表格中的占位符
     *
     * @param key     占位符
     * @param value   替换值
     * @param signArr 占位符在文档中的索引
     * @param useNum  key的使用次数
     * @param dataSize  要填充数据的条数
     */
    private void replaceSignInTable(String key, String value, Integer[] signArr, int useNum, int dataSize) {
        XWPFTable table = this.wordOperateHelper.getTablesOfDoc().get(signArr[0]);
        XWPFTableCell originCell = table.getRow(signArr[1]).getCell(signArr[2]);
        XWPFParagraph originParagraph = originCell.getParagraphs().get(signArr[3]);
        XWPFRun originRun = originParagraph.getRuns().get(signArr[4]);
        String text = originRun.getText(originRun.getTextPosition());
        int rowNum = table.getNumberOfRows() - signArr[1] - 1;
        int actualUserNum = useNum;
        if (key.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_LEFT_FULL_PREFIX)) {
            //判断表格行是否够用
            if (actualUserNum == rowNum && dataSize > 2 * rowNum) {
                //写入到了最后一行且需要的行数大于实际行数 就新增一行
                this.wordOperateHelper.getOrCreateTableRow(table, signArr[0], table.getNumberOfRows(), rowNum);
                rowNum++;
            }
            actualUserNum = actualUserNum % rowNum;
        }
        XWPFTableRow row;
        int rowMoreNum = key.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_PREFIX)
                ? (actualUserNum / 2 + 1) : (actualUserNum + 1);
        row = this.wordOperateHelper.getOrCreateTableRow(null, signArr[0],
                signArr[1] + rowMoreNum, signArr[1]);
        XWPFTableCell cell;
        if (key.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_PREFIX) && actualUserNum % 2 == 1) {
            cell = this.wordOperateHelper.getOrCreateTableCell(row, signArr[2] + signArr[5], originCell);
        } else if (key.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_LEFT_FULL_PREFIX)) {
            int offset = rowNum <= useNum ? signArr[5] : 0;
            cell = this.wordOperateHelper.getOrCreateTableCell(row, signArr[2] + offset, originCell);
        } else {
            cell = this.wordOperateHelper.getOrCreateTableCell(row, signArr[2], originCell);
        }
        XWPFParagraph paragraph = this.wordOperateHelper.getOrCreateParagraphInCell(cell, signArr[3], originCell);
        XWPFRun run = this.wordOperateHelper.getOrCreateRun(paragraph, signArr[4], originParagraph);
        this.replacePictureOrTextInRun(run, key, value, text);
    }

    /**
     * 替换嵌套表格中的表格中的占位符
     *
     * @param key     占位符
     * @param value   替换值
     * @param signArr 占位符在文档中的索引
     * @param useNum  key的使用次数
     */
    private void replaceSignInNestTable(String key, String value, Integer[] signArr, int useNum,
                                        Map<String, String> markMap) {
        // 包含需要横向遍历的表格的行
        XWPFTableRow xwpfTableRow = this.wordOperateHelper.getTablesOfDoc().get(signArr[0]).getRow(signArr[1]);
        // 需要横向遍历复制的表格
        XWPFTable table = xwpfTableRow.getCell(signArr[2]).getTables().get(signArr[3]);
        // 需要替换的模板单元格
        XWPFTableCell originCell = table.getRow(signArr[4]).getCell(signArr[5]);
        // 需要替换的模板段落
        XWPFParagraph originParagraph = originCell.getParagraphs().get(signArr[6]);
        // 需要替换的模板run
        XWPFRun originRun = originParagraph.getRuns().get(signArr[7]);
        // 需要替换的模板
        String text = originRun.getText(originRun.getTextPosition());
        // 目标行的单元格数
        int rowCellLength = table.getRow(signArr[4]).getTableCells().size();
        // 最外层包装的行的单元格数
        int cellLength = xwpfTableRow.getTableICells().size();

        XWPFTableCell cell;
        if (MapUtils.isNotEmpty(markMap) && cellLength == 1) {
            // 计算实际操作行 row
            int realRow = useNum / (rowCellLength);
            // 需要替换的目标单元格索引
            int rowCellIndex = (signArr[5] + useNum) % (rowCellLength);
            // 创建或获取目标行
            XWPFTableRow row = this.wordOperateHelper.getOrCreateTableRow(table, signArr[3], signArr[4]
                    + realRow + 1, signArr[4]);
            // 创建或获取目标单元格
            cell = this.wordOperateHelper.getOrCreateTableCell(row, rowCellIndex, originCell);
        } else if (MapUtils.isNotEmpty(markMap) && cellLength > 1) {
            table = xwpfTableRow.getCell(signArr[2] + useNum % cellLength).getTables().get(signArr[3]);
            int realRow = useNum / cellLength;
            int rowCellIndex = (signArr[5]) % (rowCellLength);
            XWPFTableRow row = this.wordOperateHelper.getOrCreateTableRow(table, signArr[3], signArr[4] +
                    realRow + 1, signArr[4]);
            cell = this.wordOperateHelper.getOrCreateTableCell(row, rowCellIndex, originCell);
        } else {
            XWPFTableRow row = this.wordOperateHelper.getOrCreateTableRow(table, signArr[3], signArr[4]
                    + useNum + 1, signArr[4]);
            cell = this.wordOperateHelper.getOrCreateTableCell(row, signArr[5], originCell);
        }
        XWPFParagraph paragraph = this.wordOperateHelper.getOrCreateParagraphInCell(cell, signArr[6], originCell);
        XWPFRun run = this.wordOperateHelper.getOrCreateRun(paragraph, signArr[7], originParagraph);
        this.replacePictureOrTextInRun(run, key, value, text);
    }

    /**
     * 替换run中的占位符为图片
     *
     * @param run
     * @param key
     * @param value
     * @param text
     */
    private void replacePictureOrTextInRun(XWPFRun run, String key, String value, String text) {
        Map<String, String> params = this.getParamsForSign(key);
        String sign = TemplateConstant.SIGN_PREFIX +
                (StringUtils.isBlank(this.oldNewSignMap.get(key)) ? key : this.oldNewSignMap.get(key))
                + TemplateConstant.SIGN_SUFFIX;
        if (key.endsWith(TemplateConstant.TEMPLATE_PICTURE_SUFFIX)) {
            try {
                int width = TemplateConstant.PICTURE_DEFAULT_WIDTH;
                int height = TemplateConstant.PICTURE_DEFAULT_HEIGHT;
                if (MapUtils.isNotEmpty(params)) {
                    width = StringUtils.isBlank(params.get("w")) ? width : Integer.parseInt(params.get("w"));
                    height = StringUtils.isBlank(params.get("h")) ? height : Integer.parseInt(params.get("h"));
                }
                if (StringUtils.isNotBlank(value) && new File(value).exists()) {
                    run.addPicture(new FileInputStream(value),
                            this.getPictureTypeFromPath(value), key, Units.toEMU(width), Units.toEMU(height));
                }
            } catch (InvalidFormatException | IOException e) {
                throw new UncheckException(e);
            }
            text = text.replace(sign, "");
            run.setText(text, 0);
        } else {
            if(text != null){
                text = text.replace(sign, StringUtils.isBlank(value) ? "" : value);
                run.setText(text, 0);
            }
        }
    }

    /**
     * 根据占位符获取参数
     * @param sign
     * @return
     */
    private Map<String, String> getParamsForSign(String sign) {
        String paramString = this.signParams.get(sign);
        if (StringUtils.isBlank(paramString)) {
            return null;
        }
        Map<String, String> params = new HashMap<>(16);
        Stream.of(paramString).flatMap(e -> Arrays.stream(e.split(","))).forEach(e -> {
            String[] param = e.split(":");
            params.put(param[0], param[1]);
        });
        return params;
    }

    /**
     * 从图片路径中获取图片类型
     *
     * @param picturePath
     */
    private int getPictureTypeFromPath(String picturePath) {
        String type = picturePath.substring(picturePath.lastIndexOf(".") + 1);
        return ImageEnum.getDocumentByType(type.toLowerCase());
    }

    private enum ImageEnum {

        JPEG("jpg", Document.PICTURE_TYPE_JPEG),

        JPG("jpeg", Document.PICTURE_TYPE_JPEG) ,

        PNG("png", Document.PICTURE_TYPE_PNG),

        GIF("gif", Document.PICTURE_TYPE_GIF),

        WMF("wmf", Document.PICTURE_TYPE_WMF),

        BMP("bmp", Document.PICTURE_TYPE_BMP),
        ;

        private String imageType;

        private Integer imageDocument;

        ImageEnum(String imageType, Integer imageDocument) {
            this.imageType = imageType;
            this.imageDocument = imageDocument;
        }

        /**
         * 根据类型获取枚举
         * @param type
         * @return
         */
        public static Integer getDocumentByType(String type) {
            if (StringUtils.isNotBlank(type)) {
                for (ImageEnum imageEnum : values()) {
                    if (imageEnum.getImageType().equals(type)) {
                        return imageEnum.getImageDocument();
                    }
                }
            }
            throw new ServiceException("不支持的图片类型");
        }

        public String getImageType() {
            return imageType;
        }

        public Integer getImageDocument() {
            return imageDocument;
        }

    }

    private static class SignIndex {

        private final Stack<Integer[]> indexStack;

        private final String sign;

        private SignIndex(String sign, Integer[] index) {
            this.sign = sign;
            this.indexStack = new Stack<>();
            this.indexStack.add(index);
        }

        private SignIndex(Collection<Integer[]> index, String sign) {
            this.sign = sign;
            this.indexStack = new Stack<>();
            index.forEach(this.indexStack::push);
        }

        private boolean push(Integer[] index) {
            if (this.indexStack.contains(index)) {
                return false;
            }
            this.indexStack.push(index);
            return true;
        }

        private Integer[] peek() {
            return this.indexStack.peek();
        }

        private Integer[] pop() {
            return this.indexStack.pop();
        }

        private Stream<Integer[]> stream() {
            return this.indexStack.stream();
        }

        private void forEach(Consumer<? super Integer[]> consumer) {
            this.indexStack.forEach(consumer);
        }

        private String getSign() {
            return sign;
        }
    }

    /**
     * 替换段落中的占位符
     *
     * @param key
     * @param value
     * @param paragraph
     */
    private void replaceSignInParagraph(String key, String value, XWPFParagraph paragraph, int runIndex) {
        XWPFRun run = this.wordOperateHelper.getOrCreateRun(paragraph, runIndex, null);
        String text = run.getText(run.getTextPosition());
        this.replacePictureOrTextInRun(run, key, value, text);
    }

    /**
     * 校验模板中是否有占位符
     *
     * @return
     */
    public boolean validateTemplateHasSign() {
        String text = this.wordOperateHelper.convertDocToString();
        Matcher m = TemplateValidateConstant.REG_PATTERN_SIGN.matcher(text);
        return m.find();
    }

    @Override
    public void close() throws Exception {
        this.wordOperateHelper.close();
    }

    public WordOperateHelper getWordOperateHelper() {
        return wordOperateHelper;
    }

    public Map<String, SignIndex> getSignMap() {
        return signMap;
    }

    public void setSignMap(Map<String, SignIndex> signMap) {
        this.signMap = signMap;
    }

    public Map<String, String> getSignParams() {
        return signParams;
    }

    public void setSignParams(Map<String, String> signParams) {
        this.signParams = signParams;
    }

    public Map<String, String> getOldNewSignMap() {
        return oldNewSignMap;
    }

    public void setOldNewSignMap(Map<String, String> oldNewSignMap) {
        this.oldNewSignMap = oldNewSignMap;
    }
}
