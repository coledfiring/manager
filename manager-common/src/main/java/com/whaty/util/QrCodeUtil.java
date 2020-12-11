package com.whaty.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.RoundRectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.xml.rpc.ServiceException;

import com.whaty.constant.CommonConstant;
import org.apache.commons.lang.StringUtils;
import sun.font.FontDesignMetrics;

/**
 * 融媒体二维码生成工具类
 *
 * @author weipensgen
 */
public class QrCodeUtil {
    private static int BLACK = -16777216;
    private static int WHITE = -1;
    private static int qHeight = 100;
    private static int qWidth = 100;
    private static int LOGO_WIDTH = 30;
    private static int LOGO_HEIGHT = 30;
    private static String imageType = "png";
    private static String encodeType = "utf-8";
    // 字体大小
    private static final int FONT_SIZE = 18;

    public QrCodeUtil() {
    }

    public static BufferedImage codeCreate(String text, String bottomDes, Integer width, Integer height, String logoPath) throws Exception {
        if (width == null || width <= 0) {
            width = qWidth;
        }

        if (height == null || height <= 0) {
            height = qHeight;
        }


        Map his = new HashMap();
        his.put(EncodeHintType.CHARACTER_SET, encodeType);
        his.put(EncodeHintType.MARGIN, 0);

        BitMatrix encode = (new MultiFormatWriter()).encode(text, BarcodeFormat.QR_CODE, width, height, his);
        BufferedImage image = new BufferedImage(width, (int)(height * 1.1), 5);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
            }
        }
        if (StringUtils.isNotEmpty(logoPath)) {
            insertImage(image, logoPath, true);
        }

        if(StringUtils.isNotBlank(bottomDes)) {
            addFontImage(image, bottomDes, width);
        }
        return image;

    }

    private static void addFontImage(BufferedImage source, String declareText, int length) {
        BufferedImage textImage = strToImage(declareText, length, (int) (length * 0.1));
        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);

        Image src = textImage;
        graph.drawImage(src, 0, length, width, height, null);
        graph.dispose();
    }

    private static BufferedImage strToImage(String str, int width, int height) {
        BufferedImage textImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)textImage.getGraphics();
        //开启文字抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);
        FontRenderContext context = g2.getFontRenderContext();
        Font font = new Font("微软雅黑", Font.BOLD, FONT_SIZE);
        g2.setFont(font);
        LineMetrics lineMetrics = font.getLineMetrics(str, context);
        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
        float offset = (width - fontMetrics.stringWidth(str)) / 2;
        float y = (height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

        g2.drawString(str, (int)offset, (int)y);

        return textImage;
    }

    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            throw new Exception("logo file not found.");
        } else {
            Image src = ImageIO.read(new File(logoPath));
            int logoWidth = ((Image) src).getWidth((ImageObserver) null);
            int logoHeight = ((Image) src).getHeight((ImageObserver) null);
            if (needCompress) {
                if (logoWidth > LOGO_WIDTH) {
                    logoWidth = LOGO_WIDTH;
                }

                if (logoHeight > LOGO_HEIGHT) {
                    logoHeight = LOGO_HEIGHT;
                }

                Image image = ((Image) src).getScaledInstance(logoWidth, logoHeight, 4);
                BufferedImage tag = new BufferedImage(logoWidth, logoHeight, 1);
                Graphics g = tag.getGraphics();
                g.drawImage(image, 0, 0, (ImageObserver) null);
                g.dispose();
                src = image;
            }

            Graphics2D graph = source.createGraphics();
            int x = (source.getWidth() - logoWidth) / 2;
            int y = (source.getHeight() - logoHeight) / 2;
            graph.drawImage((Image) src, x, y, logoWidth, logoHeight, (ImageObserver) null);
            Shape shape = new Float((float) x, (float) y, (float) logoWidth, (float) logoHeight, 6.0F, 6.0F);
            graph.setStroke(new BasicStroke(3.0F));
            graph.draw(shape);
            graph.dispose();
        }
    }

    /**
     * 生成二维码
     *
     * @param text  二维码内容
     * @param qrCodeName 二维码名称
     * @param length 二维码边长
     * @throws Exception
     */
    public static void generateQrCode(String text, String qrCodeName, Integer length, String filePath) throws Exception {
        String basePath = filePath + qrCodeName + ".png";
        String ouputPath = CommonUtils.getRealPath(basePath);
        BufferedImage bufferedImage = codeCreate(text, qrCodeName, length, length, null);
        try (FileOutputStream outputStream = new FileOutputStream(new File(ouputPath))) {
            ImageIO.write(bufferedImage, "PNG", outputStream);
            bufferedImage.flush();
            outputStream.flush();
        }
    }

    /**
     * 批量生成二维码 key 二维码文件名 value 二维码内容
     *
     * @param textMap  资源map
     * @param qrCodeLength   二维码边长
     * @param code    文件夹名字
     * @throws Exception
     */
    public static void generateQrCodes(Map<String, String> textMap, Integer qrCodeLength, String code, String dirPath) throws Exception {
        CommonUtils.deleteFiles(CommonUtils.getRealPath(String.format(dirPath, code)));
        QrCodeUtil.generateQrCodes(textMap, qrCodeLength, dirPath);
    }

    /**
     * 批量生成二维码 key 二维码文件名 value 二维码内容
     *
     * @param textMap
     * @param qrCodeLength 二维码的边长
     */
    public static void generateQrCodes(Map<String, String> textMap, Integer qrCodeLength, String dirPath) throws Exception {
        CommonUtils.deleteFiles(CommonUtils.getRealPath(dirPath));
        CommonUtils.mkDir(CommonUtils.getRealPath(dirPath));
        Iterator pathIt = textMap.entrySet().iterator();
        while(pathIt.hasNext()) {
            Entry<String, String> entry = (Entry)pathIt.next();
            generateQrCode(entry.getValue(), entry.getKey(), qrCodeLength, dirPath);
        }
    }
}

