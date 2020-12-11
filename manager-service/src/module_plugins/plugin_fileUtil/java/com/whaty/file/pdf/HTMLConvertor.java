package com.whaty.file.pdf;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * html转换pdf转换器
 *
 * @author weipengsen
 */
public class HTMLConvertor {

    private static final Map<String, String> REPLACE_SIGN_MAP = new HashMap<>(16);

    private static String FONT_FILE;

    static {
        // 替换不能解析的字符
        REPLACE_SIGN_MAP.put("&nbsp;", " ");
        REPLACE_SIGN_MAP.put("&ndash;", "–");
        REPLACE_SIGN_MAP.put("&mdash;", "—");
        REPLACE_SIGN_MAP.put("&lsquo;", "‘");
        REPLACE_SIGN_MAP.put("&rsquo;", "’");
        REPLACE_SIGN_MAP.put("&sbquo;", "‚");
        REPLACE_SIGN_MAP.put("&ldquo;", "“");
        REPLACE_SIGN_MAP.put("&rdquo;", "”");
        REPLACE_SIGN_MAP.put("&bdquo;", "„");
        REPLACE_SIGN_MAP.put("&prime;", "′");
        REPLACE_SIGN_MAP.put("&Prime;", "″");
        REPLACE_SIGN_MAP.put("&lsaquo;", "‹");
        REPLACE_SIGN_MAP.put("&rsaquo;", "›");
        REPLACE_SIGN_MAP.put("&oline;", "‾");
        // 中文字体包
        FONT_FILE = "/fonts/simsun.ttf";
    }

    /**
     * 将html转换成pdf
     * @param srcFile
     * @param destFile
     * @throws IOException
     * @throws DocumentException
     */
    public void convertFromHtml(String srcFile, String destFile) throws IOException, DocumentException {
        try (OutputStream fileStream = new FileOutputStream(destFile)) {
            String srcContent = FileUtils.readFileToString(new File(srcFile));
            this.convertFromHtml(srcContent, fileStream);
        }
    }

    /**
     * 将html字符转换为pdf文件并写入流中
     * @param htmlStr
     * @param outputStream
     * @throws IOException
     * @throws DocumentException
     */
    public void convertFromHtml(String htmlStr, OutputStream outputStream)
            throws IOException, DocumentException {
        for (Map.Entry<String, String> entry : REPLACE_SIGN_MAP.entrySet()) {
            htmlStr = htmlStr.replace(entry.getKey(), entry.getValue());
        }
        ITextRenderer textRenderer = new ITextRenderer();
        ITextFontResolver fontResolver = textRenderer.getFontResolver();
        fontResolver.addFont(FONT_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        textRenderer.setDocumentFromString(htmlStr, null);
        textRenderer.layout();
        textRenderer.createPDF(outputStream, true);
    }

}
