package com.whaty.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转化为英文工具类
 *
 * @author weipengsen
 */
public class ChineseToEnglishUtils {

    /**
     * 将汉字转换为全拼
     *
     * @param src
     * @return
     */
    public static String getPingYin(String src) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder target = new StringBuilder();
        for (char c : src.toCharArray()) {
            // 判断是否为汉字字符
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                target.append(PinyinHelper.toHanyuPinyinStringArray(c, format));
            } else {
                target.append(Character.toString(c));
            }
        }
        return target.toString();
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        StringBuilder phonetic = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : nameChar) {
            if (c > 128) {
                try {
                    String[] strArr = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (strArr != null) {
                        phonetic.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                phonetic.append(c);
            }
        }
        return phonetic.toString();
    }

    /**
     * 姓、名 英文第一个字母大写
     *
     * @param name
     * @return
     */
    public static String getUpperFirstName(String name) {
        char[] charArr = name.toCharArray();
        String target;
        switch (charArr.length) {
            case 2:
                target = toUpperFirstCase(converterToSpell("" + charArr[0])) + " "
                        + toUpperFirstCase(converterToSpell("" + charArr[1]));
                break;
            case 3:
                target = toUpperFirstCase(converterToSpell("" + charArr[0])) + " "
                        + toUpperFirstCase(converterToSpell("" + charArr[1] + charArr[2]));
                break;
            case 4:
                target = toUpperFirstCase(converterToSpell("" + charArr[0] + charArr[1]))
                        + " " + toUpperFirstCase(converterToSpell("" + charArr[2] + charArr[3]));
                break;
            default:
                target = toUpperFirstCase(converterToSpell(name));
                break;
        }
        return target;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    private static String toUpperFirstCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

    /**
     * 返回中文的首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (char word : str.toCharArray()) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param originStr
     * @return
     */
    public static String convertToASCII(String originStr) {
        StringBuilder asciiString = new StringBuilder();
        for (byte b : originStr.getBytes()) {
            asciiString.append(Integer.toHexString(b & 0xff));
        }
        return asciiString.toString();
    }

}
