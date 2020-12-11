package com.whaty.util;

import com.whaty.constant.SQLConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql处理工具
 *
 * @author weipengsen
 */
public class SQLHandleUtils {

    /**
     * 正则表达式，匹配sql中parentId占位符
     */
    private static final String REPLACE_SIGN = "\\[.+?\\|.+?\\]";
    /**
     * 正则表达式模式，匹配sql中parentId占位符
     */
    private static final Pattern SIGN_REPLACE_PATTERN = Pattern.compile(REPLACE_SIGN);

    /**
     * 正则表达式，匹配sql中[ConditionSign]占位符
     */
    private static final String REPLACE_CONDITION_SIGN = "\\[ConditionSign\\]";

    /**
     * 正则表达式模式，匹配sql中[ConditionSign]占位符
     */
    private static final Pattern CONDITION_SIGN_REPLACE_PATTERN = Pattern.compile(REPLACE_CONDITION_SIGN);

    /**
     * 处理sql中的字符
     *
     * @param sql
     * @return
     */
    public static String handleSignInSQL(String sql) {
        return sql.replace(SQLConstant.VARIABLE_SIGN, SQLConstant.CONVERT_VARIABLE_SIGN);
    }

    /**
     * 校验sql中的key都在param中
     *
     * @param sql
     * @param params
     * @return
     */
    public static boolean checkAliasExists(String sql, Map params) {
        Matcher m = SIGN_REPLACE_PATTERN.matcher(sql);
        while (m.find()) {
            String[] signTargetArr = m.group().trim().replace("[", "").replace("]", "").split("\\|");
            if (MapUtils.isEmpty(params) || !params.containsKey(signTargetArr[0])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用参数替换sql中的占位符，规则为
     * 1.[key|alias] 其中alias为sql中使用的别名，key为传入的参数在map中的key，如果key不存在则替换为1=1
     * 2.替换[ConditionSign]为1=1 若想自定义[ConditionSign]的替换内容参见{@code replaceConditionSign}方法
     *
     * @param sql
     * @param params
     * @param replaceAll 是否将所有占位符都替换，为true则没有对应值的占位符替换成1=1
     * @return
     */
    public static String replaceSignUseParams(String sql, Map params, boolean replaceAll) {
        if (!replaceAll && MapUtils.isEmpty(params)) {
            return sql;
        }
        Matcher m = SIGN_REPLACE_PATTERN.matcher(sql);
        while (m.find()) {
            String sign = m.group();
            String[] signTargetArr = sign.trim().replace("[", "").replace("]", "").split("\\|");
            String alias = signTargetArr[1];
            String key = signTargetArr[0];
            if (MapUtils.isEmpty(params) || !params.containsKey(key)) {
                if (replaceAll) {
                    sql = sql.replace(sign, " 1=1");
                }
                continue;
            }
            if (params.get(key) == null) {
                if (replaceAll) {
                    sql = sql.replace(sign, " 1=1");
                }
            } else if (params.get(key) instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection) params.get(key))) {
                    sql = sql.replace(sign, CommonUtils.madeSqlIn((Collection) params.get(key), alias));
                } else {
                    sql = sql.replace(sign, " 1=1");
                }
            } else if (params.get(key).getClass().isArray()) {
                if (ArrayUtils.isNotEmpty((Object[]) params.get(key))) {
                    sql = sql.replace(sign, CommonUtils.madeSqlIn((String[]) params.get(key), alias));
                } else {
                    sql = sql.replace(sign, " 1=1");
                }
            } else {
                if (key.endsWith("_dateLimit")) {
                    String[] dateArr = ((String) params.get(key)).split(",");
                    sql = sql.replace(sign, alias + " between '" + dateArr[0] + "' and '" + dateArr[1] + "'");
                } else if (key.endsWith("_like")) {
                    sql = sql.replace(sign, alias + " like '%" + params.get(key) + "%'");
                } else {
                    sql = sql.replace(sign, alias + "='" + params.get(key) + "'");
                }
            }
        }
        sql = replaceConditionSign(sql);
        return sql;
    }

    /**
     * 使用参数替换sql中的占位符，规则为
     * 1.[key|alias] 其中alias为sql中使用的别名，key为传入的参数在map中的key，如果key不存在则替换为1=1
     * 2.替换[ConditionSign]为1=1 若想自定义[ConditionSign]的替换内容参见{@code replaceConditionSign}方法
     *
     * @param sql
     * @param params
     * @return
     */
    public static String replaceSignUseParams(String sql, Map params) {
        return replaceSignUseParams(sql, params, false);
    }

    /**
     * 使用{@code params}替换sql中的[ConditionSign]占位符
     * 替换内容的顺序为sql中占位符出现的顺序
     * 一个参数与一个占位符对应
     * 返回处理后的sql
     *
     * @param sql
     * @param params
     * @return
     */
    public static String replaceConditionSign(String sql, String... params) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        Matcher m = CONDITION_SIGN_REPLACE_PATTERN.matcher(sql);
        int i = 0;
        String replacement;
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            if (null != params && params.length > i && StringUtils.isNotBlank(params[i])) {
                replacement = params[i];
            } else {
                replacement = " 1=1 ";
            }
            m.appendReplacement(sb, replacement);
            i++;
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 替换所有占位符
     *
     * @param sql
     * @return
     */
    public static String replaceAllBlankSign(String sql) {
        Matcher m = SIGN_REPLACE_PATTERN.matcher(sql);
        while (m.find()) {
            String sign = m.group();
            sql = sql.replace(sign, "1 = 1");
        }
        sql = replaceConditionSign(sql);
        return sql;
    }
}
