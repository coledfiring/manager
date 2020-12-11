package com.whaty.file.word.helper;

import com.whaty.file.template.util.TemplateUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * word读辅助类
 *
 * @author weipengsen
 */
public class WordOperateHelper implements Closeable {

    private XWPFDocument doc;

    private InputStream input;

    public WordOperateHelper(InputStream input) throws IOException {
        this.input = input;
        this.doc = new XWPFDocument(input);
    }

    public WordOperateHelper(XWPFDocument document) {
        this.doc = document;
    }

    /**
     * 将doc文档中的内容无格式输出
     *
     * @return
     */
    public String convertDocToString() {
        XWPFWordExtractor extractor = new XWPFWordExtractor(this.doc);
        return extractor.getText();
    }

    /**
     * 获取doc文档中的所有段落
     *
     * @return
     */
    public List<XWPFParagraph> getParagraphsOfDoc() {
        return this.doc.getParagraphs();
    }

    /**
     * 获取doc文档中的所有表格
     *
     * @return
     */
    public List<XWPFTable> getTablesOfDoc() {
        return this.doc.getTables();
    }

    /**
     * 获取当前文档
     *
     * @return
     */
    public XWPFDocument getDocument() {
        return this.doc;
    }

    /**
     * 将文档输出到指定文件
     *
     * @param outPath
     * @throws IOException
     */
    public void exportDocToFile(String outPath) throws IOException {
        TemplateUtils.mkdir(outPath);
        try (OutputStream out = new FileOutputStream(outPath)) {
            this.exportDocToOutputStream(out);
        }
    }

    /**
     * 将文档输出到输出流
     *
     * @param outputStream
     * @throws
     */
    public void exportDocToOutputStream(OutputStream outputStream) throws IOException {
        this.doc.write(outputStream);
    }

    /**
     * 获取或创建row
     * @param tableIndex
     * @param rowIndex
     * @param mapRowIndex
     * @return
     */
    public XWPFTableRow getOrCreateTableRow(XWPFTable table, int tableIndex, int rowIndex, Integer mapRowIndex) {
        table = table == null ? this.getTablesOfDoc().get(tableIndex) : table;
        if (rowIndex == table.getNumberOfRows()) {
            XWPFTableRow row = table.createRow();
            if (mapRowIndex != null) {
                XWPFTableRow originRow = table.getRow(mapRowIndex);
                row.getCtRow().setTrPr(originRow.getCtRow().getTrPr());
                //复制单元格
                this.copyCellOfRow(row, originRow);
            }
            return row;
        } else {
            return table.getRow(rowIndex);
        }
    }

    /**
     * 复制行中的所有单元格
     * @param row
     * @param originRow
     */
    private void copyCellOfRow(XWPFTableRow row, XWPFTableRow originRow) {
        for (int i = 0; i < originRow.getTableCells().size(); i ++) {
            XWPFTableCell originCell = originRow.getCell(i);
            XWPFTableCell cell = this.getOrCreateTableCell(row, i, originCell);
            this.copyCell(cell, originCell);
        }
    }

    /**
     * 复制单元格格式
     * @param cell
     * @param originCell
     */
    public void copyCell(XWPFTableCell cell, XWPFTableCell originCell) {
        if (originCell.getCTTc() != null) {
            cell.getCTTc().setTcPr(originCell.getCTTc().getTcPr());
            for (int i = 0; i < originCell.getParagraphs().size(); i++) {
                XWPFParagraph para = this.getOrCreateParagraphInCell(cell, i, originCell);
                XWPFParagraph originPara = originCell.getParagraphs().get(i);
                for (int j = 0; j < originPara.getRuns().size(); j++) {
                    this.getOrCreateRun(para, j, originPara);
                }
            }
        }
    }

    /**
     * 复制段落格式
     * @param paragraph
     * @param originParagraph
     */
    private void copyParagraph(XWPFParagraph paragraph, XWPFParagraph originParagraph) {
        if (paragraph.getCTP() != null && originParagraph.getCTP() != null) {
            paragraph.getCTP().setPPr(originParagraph.getCTP().getPPr());
        }
        for (int j = 0; j < originParagraph.getRuns().size(); j++) {
            this.getOrCreateRun(paragraph, j, originParagraph);
        }
    }

    /**
     * 复制run格式
     * @param run
     * @param origin
     */
    private void copyRun(XWPFRun run, XWPFRun origin) {
        if (run.getCTR() != null && origin.getCTR() != null) {
            run.getCTR().setRPr(origin.getCTR().getRPr());
        }
    }

    /**
     * 创建或获取cell
     * @param row
     * @param cellIndex
     * @param mappedCell
     * @return
     */
    public XWPFTableCell getOrCreateTableCell(XWPFTableRow row, int cellIndex, XWPFTableCell mappedCell) {
        if (cellIndex == row.getTableCells().size()) {
            XWPFTableCell target = row.createCell();
            if (mappedCell != null) {
                this.copyCell(target, mappedCell);
            }
            return target;
        } else {
            return row.getCell(cellIndex);
        }
    }

    /**
     * 创建或获取cell中的paragraph
     * @param cell
     * @param paraIndex
     * @param mappedCell
     * @return
     */
    public XWPFParagraph getOrCreateParagraphInCell(XWPFTableCell cell, int paraIndex, XWPFTableCell mappedCell) {
        if (paraIndex >= cell.getParagraphs().size()) {
            for (int i = cell.getParagraphs().size();
                 i < (mappedCell != null ? mappedCell.getParagraphs().size() : paraIndex + 1); i++) {
                if (mappedCell != null) {
                    this.copyParagraph(cell.addParagraph(), mappedCell.getParagraphs().get(i));
                } else {
                    cell.addParagraph();
                }
            }
        } else if (cell.getParagraphs().size() == 1 && this.checkParagraphIsEmpty(cell.getParagraphs().get(0))) {
            if (mappedCell != null) {
                this.copyParagraph(cell.getParagraphs().get(0), mappedCell.getParagraphs().get(0));
            }
        }
        return cell.getParagraphs().get(paraIndex);
    }

    /**
     * 判断段落是否为空段落
     * @param paragraph
     * @return
     */
    private boolean checkParagraphIsEmpty(XWPFParagraph paragraph) {
        return paragraph.getCTP() == null || paragraph.getCTP().getPPr() == null;
    }

    /**
     * 创建或获取run
     * @param paragraph
     * @param runIndex
     * @param mappedParagraph
     * @return
     */
    public XWPFRun getOrCreateRun(XWPFParagraph paragraph, int runIndex, XWPFParagraph mappedParagraph) {
        if (runIndex >= paragraph.getRuns().size()) {
            XWPFRun target = paragraph.createRun();
            if (mappedParagraph != null) {
                XWPFRun origin = mappedParagraph.getRuns().get(paragraph.getRuns().size() - 1);
                this.copyRun(target, origin);
                target.setText(origin.getText(origin.getTextPosition()), origin.getTextPosition());
            }
            return this.getOrCreateRun(paragraph, runIndex, mappedParagraph);
        } else {
            return paragraph.getRuns().get(runIndex);
        }
    }

    /**
     * 获取指定索引的paragraph
     * @param paraIndex
     * @return
     */
    public XWPFParagraph getParagraphAt(int paraIndex) {
        return this.doc.getParagraphs().get(paraIndex);
    }

    /**
     * 合并document，将appendDocument合到doc
     * @param appendDocument
     * @throws Exception
     */
    public void mergeDocument(XWPFDocument appendDocument) throws XmlException {
        CTBody src = this.doc.getDocument().getBody();
        CTBody append = appendDocument.getDocument().getBody();
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);

        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String suffix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));

        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + suffix);
        src.set(makeBody);
    }

    /**
     * 合并document，将多个document合并到一起
     * @param documents
     * @throws Exception
     */
    public void mergeDocument(List<String> documents) throws XmlException {
        StringBuilder targetXmlString = new StringBuilder();
        CTBody src = this.getDocument().getDocument().getBody();
        String srcString = src.xmlText();
        targetXmlString.append(srcString.substring(0, srcString.indexOf(">") + 1));
        targetXmlString.append(srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<")));
        documents.forEach(e -> this.mergeDocumentBody(targetXmlString, e));
        targetXmlString.append(srcString.substring(srcString.lastIndexOf("<")));
        this.doc.getDocument().getBody().set(CTBody.Factory.parse(targetXmlString.toString()));
    }

    /**
     * 将document的body合并到string中
     * @param xmlString
     * @param document
     * @return
     */
    public StringBuilder mergeDocumentBody(StringBuilder xmlString, String document) {
        String mainPart = document.substring(document.indexOf(">") + 1, document.lastIndexOf("<"));
        return xmlString.append(mainPart);
    }

    /**
     * 分页
     */
    public void breakPage() {
        this.doc.createParagraph().setPageBreak(true);
    }

    @Override
    public void close() throws IOException {
        this.input.close();
    }

}
