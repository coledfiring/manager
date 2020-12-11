package com.whaty.schedule.util;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 验证相关工具类
 * @author weipengsen
 */
public class ValidateUtils {

    private static GeneralDao generalDao;

    /**
     * 身份证号正则
     */
    public static final String PERSON_CARD_NO_REG_STR = "(^[1-9][0-9]{5}(18|19|([23][0-9]))[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}[0-9Xx]$)" +
            "|(^[1-9][0-9]{5}[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{2}[0-9Xx]$)";

    /**
     * 身份证正则对象
     */
    public static final Pattern PERSON_CARD_NO_PATTERN = Pattern.compile(PERSON_CARD_NO_REG_STR);

    /**
     * 日期校验正则字符串
     */
    public static final String DATE_REG_STR = "^(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))"
            + "(\\/|-|\\.)(?:0?2\\1(?:29))$)|(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(\\/|-|\\.)(?:(?:(?:0?[13578]|1[02])\\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\\2(?:0?[1-9]|1\\d|2[0-8]))$";

    /**
     * 日期校验正则模式对象
     */
    public static final Pattern DATE_REG_PATTERN = Pattern.compile(DATE_REG_STR);

    /**
     * 日期正则，加闰年闰月
     */
    public static final String DATE_DETAIL_REG_STR = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|" +
            "[1-9][0-9]{3})(-?)(((0[13578]|1[02])(-?)(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(-?)(0[1-9]|[12][0-9]|30))|" +
            "(02(-?)(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))(0229|-02-29))";


    /**
     * 字母正则
     */
    public static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");

    /**
     * 字母正则
     */
    public static final Pattern NUM_PATTERN = Pattern.compile("[a-zA-Z]");

    /**
     * 金额正则
     */
    public static final String MONEY_PATTERN_STR = "^\\d{1,8}(\\.\\d{1,2})?$";
    /**
     * 金额正则不满足时的提示信息
     */
    public static final String MONEY_PATTERN_STR_MESSAGE = "金额输入格式：1到8位整数 0到2位小数";
    /**
     * 金额正则模式
     */
    public static final Pattern MONEY_PATTERN = Pattern.compile(MONEY_PATTERN_STR);

    /**
     * 图片文件名正则
     */
    public static final String IMAGE_NAME_PATTERN = "^.+((\\.bmp)|(\\.jpg)|(\\.gif)|(\\.png))$";

    /**
     * 成绩分数正则
     */
    public static final Pattern SCORE_REG_PATTERN = Pattern.compile("^(([1-9]?[0-9])|100)(.[0-9])?$");

    /**
     * 检查身份证号是否符合规则
     *
     * @param cardNo
     * @return
     */
    public static boolean checkCardNoReg(String cardNo) {
        return PERSON_CARD_NO_PATTERN.matcher(cardNo).find();
    }

    /**
     * 检查ids在指定的表中是否存在
     * @return
     */
    public static boolean checkDataForIdsIsExist(String tableName, String ids) {
        return checkDataForIdsIsExist(tableName, ids.split(CommonConstant.SPLIT_ID_SIGN));
    }

    /**
     * 检查ids在指定的表中是否存在
     * @return
     */
    public static boolean checkDataForIdsIsExist(String tableName, String[] ids) {
        return checkDataForIdsIsExist(tableName, Arrays.asList(ids));
    }

    /**
     * 检查ids在指定的表中是否存在
     * @return
     */
    public static boolean checkDataForIdsIsExist(String tableName, Collection<String> ids) {
        List<Object> result = getGeneralDao()
                .getBySQL("select 1 from " + tableName + " where " + CommonUtils.madeSqlIn(ids, "id"), null);
        if(CollectionUtils.isEmpty(result) || result.size() != ids.size()) {
            return false;
        }
        return true;
    }

    /**
     * 校验生日是否与身份证号相对应
     *
     * @param cardNo
     * @param birthday
     * @return
     */
    public static boolean checkCardNoBirthday(String cardNo, String birthday) {
        String birth;
        if (cardNo.length() == 15) {
            birth = cardNo.substring(6, 12);
            birthday = birthday.replaceAll("-", "").substring(2);
        } else {
            birth = cardNo.substring(6, 14);
            birthday = birthday.replaceAll("-", "");
        }
        return birth.equals(birthday);
    }

    /**
     * 进行constraint校验
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Set<ConstraintViolation<T>> checkConstraintValidate(T bean) {
        return Validation.buildDefaultValidatorFactory().getValidator().validate(bean);
    }

    private static GeneralDao getGeneralDao() {
        if(generalDao == null) {
            generalDao = (GeneralDao) SpringUtil.getBean("core_generalDao");
        }
        return generalDao;
    }

}
