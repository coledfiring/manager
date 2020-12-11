package com.whaty.generator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * 文字图片生成器
 *
 * @author weipengsen
 */
public class WordImageGenerator {

    /**
     * 文字占图片宽度比率
     */
    private final double rate;
    /**
     * 图片宽
     */
    private final int width;
    /**
     * 图片高
     */
    private final int height;
    /**
     * 字体
     */
    private final String fontFamily;

    private final static double DEFAULT_RATE = 0.8;

    private final static String DEFAULT_FONT_FAMILY = "黑体";

    /**
     * 头像的背景色盘
     */
    private final static Color[] PROFILE_PICTURE_COLOR = {new Color(206, 209, 130), new Color(195, 138, 207),
            new Color(91, 194, 240), new Color(244, 207, 91), new Color(242, 160, 130)};

    private final Random random = new Random();

    public WordImageGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.rate = DEFAULT_RATE;
        this.fontFamily = DEFAULT_FONT_FAMILY;
    }

    public WordImageGenerator(int width, int height, double rate, String fontFamily) {
        this.rate = rate;
        this.width = width;
        this.height = height;
        this.fontFamily = fontFamily;
    }

    /**
     * 生成头像
     * @param origin
     * @param path
     * @return
     * @throws IOException
     */
    public File generateProfilePicture(String origin, String path) throws IOException {
        return this.generate(origin, path,
                PROFILE_PICTURE_COLOR[random.nextInt(PROFILE_PICTURE_COLOR.length)], Color.WHITE);
    }

    /**
     * 生成图片
     * @param origin
     * @param targetFilePath
     * @param color
     * @param backgroundColor
     * @return
     * @throws IOException
     */
    public File generate(String origin, String targetFilePath, Color backgroundColor, Color color) throws IOException {
        return this.generate(origin, new File(targetFilePath), backgroundColor, color);
    }

    /**
     * 生成图片
     * @param origin
     * @param targetFile
     * @param color
     * @param backgroundColor
     * @return
     * @throws IOException
     */
    public File generate(String origin, File targetFile, Color backgroundColor, Color color) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Font font = new Font(this.fontFamily, Font.BOLD, (int) (width / origin.length() * this.rate));
        Graphics g = image.getGraphics();
        g.setClip(0, 0, this.width, this.height);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, this.width, this.height);

        g.setColor(color);
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics(font);
        int y = (g.getClipBounds().height - (fm.getAscent() + fm.getDescent())) / 2 + fm.getAscent();
        int xPos = (this.width - g.getFontMetrics().stringWidth(origin)) / 2;
        IntStream.range(0, 6).forEach(i -> g.drawString(origin, xPos + i * 680, y));
        g.dispose();

        ImageIO.write(image, targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1), targetFile);
        return targetFile;
    }

    @Override
    public String toString() {
        return "WordImageGenerator{" +
                "rate=" + rate +
                ", width=" + width +
                ", height=" + height +
                ", fontFamily='" + fontFamily + '\'' +
                '}';
    }
}
