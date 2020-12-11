package com.whaty.file.pdf.strategy;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.template.constant.TemplateConstant;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * pdf模板辅助类
 *
 * @author weipengsen
 */
public class PdfTemplateStrategy implements AbstractTemplateStrategy {

    protected String filePath;

    protected InputStream inputStream;

    protected PdfReader pdfReader;

    protected List<ByteArrayOutputStream> outputStreams = new ArrayList<>();

    public PdfTemplateStrategy(String filePath) throws IOException {
        this(new FileInputStream(filePath));
        this.filePath = filePath;
    }

    public PdfTemplateStrategy(File file) throws IOException {
        this.pdfReader = new PdfReader(new FileInputStream(file));
    }

    public PdfTemplateStrategy(InputStream inputStream) throws IOException {
        this.pdfReader = new PdfReader(inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public void handleTemplateFileByData(List<Map<String, Object>> data)
            throws IOException, DocumentException {
        if (StringUtils.isBlank(this.filePath)) {
            throw new IllegalArgumentException("只有构造参数为文件路径时才可以进行多选打印");
        }
        for (Map<String, Object> singleData : data) {
            this.pdfReader = new PdfReader(this.filePath);
            this.handleTemplateFileByData(singleData);
        }
    }

    @Override
    public Set<String> collectSignInTemplate() {
        Set<String> signs = this.pdfReader.getAcroFields().getFields().keySet();
        return signs.stream().map(e -> e.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, ""))
                .map(e -> e.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, ""))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getTypeSuffix() {
        return ".pdf";
    }

    @Override
    public void handleTemplateFileByData(Map<String, Object> data) throws IOException, DocumentException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        this.outputStreams.add(bao);
        this.replaceSignInTemplate(data, bao);
    }

    @Override
    public void exportDocToFile(String path) throws IOException, DocumentException {
        this.exportDocToOutputStream(new FileOutputStream(CommonUtils.mkDir(path)));
    }

    @Override
    public void exportDocToOutputStream(OutputStream outputStream) throws IOException, DocumentException {
        Document doc = new Document();
        PdfCopy pdfCopy = new PdfCopy(doc, outputStream);
        doc.open();
        for (ByteArrayOutputStream bao : this.outputStreams) {
            PdfReader p = new PdfReader(bao.toByteArray());
            PdfImportedPage impPage = pdfCopy.getImportedPage(p, 1);
            pdfCopy.addPage(impPage);
        }
        doc.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void close() throws Exception {
        this.pdfReader.close();
    }

    /**
     * 替换模板中的占位符
     * @param data
     * @param bao
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceSignInTemplate(Map<String, Object> data, ByteArrayOutputStream bao)
            throws IOException, DocumentException {
        PdfStamper stamp = new PdfStamper(this.pdfReader, bao);
        AcroFields form = stamp.getAcroFields();
        for (Object key : form.getFields().keySet()) {
            String handledKey = ((String) key).replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                    .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "");
            if (data.containsKey(handledKey)) {
                this.replaceSignWithValue(stamp, (String) key, data.get(handledKey));
            }
        }
        stamp.setFormFlattening(true);
        stamp.close();
    }

    /**
     * 使用值去替换占位符
     * @param stamp
     * @param key
     * @param value
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceSignWithValue(PdfStamper stamp, String key, Object value)
            throws IOException, DocumentException {
        if (key.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "")
                .endsWith(TemplateConstant.TEMPLATE_PICTURE_SUFFIX)) {
            this.replaceSignWithPicture(stamp, key, value);
        } else {
            this.replaceSignWithText(stamp, key, (String) value);
        }
    }

    /**
     * 使用文本替换占位符
     * @param stamp
     * @param key
     * @param value
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceSignWithText(PdfStamper stamp, String key, String value)
            throws IOException, DocumentException {
        if (StringUtils.isNotBlank(value) && key.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "")
                .endsWith(TemplateConstant.TEMPLATE_CHINESE_DATE_SUFFIX)) {
            value = CommonUtils.dateNumberToChinese(Integer.parseInt(value));
        } else if (StringUtils.isNotBlank(value) && key.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "")
                .endsWith(TemplateConstant.TEMPLATE_CHINESE_NUM_SUFFIX)) {
            value = CommonUtils.feeNumberToChinese(Double.parseDouble(value));
        }
        stamp.getAcroFields().setField(key, value == null ? "" : value);
    }

    /**
     * 使用图片替换占位符
     * @param stamp
     * @param key
     * @param value
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceSignWithPicture(PdfStamper stamp, String key, Object value)
            throws IOException, DocumentException {
        if (StringUtils.isNotBlank((String) value)) {
            float[] fieldPositions = stamp.getAcroFields().getFieldPositions(key);
            float fieldPage = fieldPositions[0];
            float fieldLlx = fieldPositions[1];
            float fieldLly = fieldPositions[2];
            float fieldUrx = fieldPositions[3];
            float fieldUry = fieldPositions[4];
            Rectangle rec = new Rectangle(fieldLlx, fieldLly, fieldUrx, fieldUry);
            File file = new File((String) value);
            if (!file.exists()) {
                return;
            }
            Image gif = Image.getInstance((String) value);
            gif.scaleToFit(rec.getWidth(), rec.getHeight());

            // 得到图片的绝对位置
            gif.setAbsolutePosition(fieldLlx
                    + (rec.getWidth() - gif.getScaledWidth()) / 2, fieldLly
                    + (rec.getHeight() - gif.getScaledHeight()) / 2);
            PdfContentByte cb = stamp.getOverContent((int) fieldPage);
            cb.addImage(gif);
        }
    }

}
