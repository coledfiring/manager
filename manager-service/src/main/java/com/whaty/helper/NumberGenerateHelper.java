package com.whaty.helper;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.CommonUtils;
import com.whaty.utils.HibernatePluginsUtil;
import com.whaty.utils.StaticBeanUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 号码生成辅助工具
 *
 * @author weipengsen
 */
public class NumberGenerateHelper {

    private final static String SPLIT_SIGN = ".";

    /**
     * 加载配置
     *
     * @returnR
     */
    public JSONObject loadSetup(String numberType) {
       /* String key = CacheKeys.NUMBER_GENERATE_CACHE_KEY
                .getKeyWithParams(SiteUtil.getSiteCode(), numberType);
        //从缓存中获取xml
        String regular = this.getSetupFromCache(key);
        if(StringUtils.isBlank(regular)) {*/
        //从数据库中获取xml字符串并转换成xml对象
        String regular = this.getSetupFromDB(numberType);
        //}
        return JSONObject.fromObject(regular);
    }

    /**
     * 存储配置
     *
     * @param numberType
     * @param setup
     */
    public void accessSetup(String setup, String numberType) {
        /*String key = CacheKeys.NUMBER_GENERATE_CACHE_KEY
                .getKeyWithParams(SiteUtil.getSiteCode(), numberType);*/
        this.accessSetupToDB(setup, numberType);
        //this.accessSetupToCache(setup, key);
    }

    /**
     * 存储配置到数据库
     */
    protected void accessSetupToDB(String setup, String numberType) {
        if (StaticBeanUtils.getGeneralDao()
                .checkNotEmpty("select value from system_variables where name = ? and site_code = ?",
                        numberType, SiteUtil.getSiteCode())) {
            StaticBeanUtils.getGeneralDao()
                    .executeBySQL("Update system_variables set value = ? where name = ? and site_code = ?",
                    setup, numberType, SiteUtil.getSiteCode());
        } else {
            StaticBeanUtils.getGeneralDao()
                    .executeBySQL("INSERT system_variables (id, NAME, VALUE, note, site_code) " +
                            "VALUES(REPLACE (uuid(), '-', ''), ?, ?, '号码生成规则', ?)", numberType, setup,
                            SiteUtil.getSiteCode());
        }
    }

    /**
     * 存储配置到缓存
     */
    protected void accessSetupToCache(String setup, String key) {
        StaticBeanUtils.getRedisCacheService().remove(key);
        StaticBeanUtils.getRedisCacheService().putToCache(key, setup);
    }

    /**
     * 从缓存获取配置
     *
     * @return
     */
    protected String getSetupFromCache(String key) {
        return StaticBeanUtils.getRedisCacheService().getFromCache(key);
    }

    /**
     * 从数据库获取配置
     *
     * @param numberType
     * @return
     */
    protected String getSetupFromDB(String numberType) {
        return StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select value from system_variables where name = ? and site_code = ?",
                        numberType, SiteUtil.getSiteCode());
    }

    /**
     * 解析通用生成方法的indexData，并返回column_name和join sql字符串
     *
     * @param indexData
     * @param basicTableAlias 拼接所基于的表的别名
     * @param basicBean       拼接所基于的实体类
     * @return
     */
    public Map<String, String> parseGenerateNumberIndexData(String indexData, String basicTableAlias,
                                                            Class basicBean) throws ClassNotFoundException {
        Map<String, String> resultMap = new HashMap<>(2);
        if (!indexData.contains(SPLIT_SIGN)) {
            resultMap.put("alias", HibernatePluginsUtil.getColumnName(basicBean, indexData));
            return resultMap;
        }
        String[] columns = indexData.split("\\" + SPLIT_SIGN);
        //每次循环对应得上次column对应的bean
        Class bean = basicBean;
        //最终对应的结果集
        String resultColumn = null;
        //每次循环的当前column对应的上次column的值
        String linkTableAlias = basicTableAlias;

        StringBuilder whereSql = new StringBuilder();
        //bean类型的包路径
        String beanPackage = bean.getName().replace(bean.getSimpleName(), "");
        for (int i = 0; i < columns.length; i++) {
            //column对应的数据库字段
            String dataColumn = HibernatePluginsUtil.getColumnName(bean, columns[i]);
            if (i != columns.length - 1) {
                //当前column对应的实体类
                if (columns[i].contains("enumConst")) {
                    bean = EnumConst.class;
                } else {
                    bean = Class.forName(beanPackage + CommonUtils.upperFirstLetter(columns[i]));
                }
                //当前column对应的
                String tableName = HibernatePluginsUtil.getTableName(bean);
                whereSql.append("INNER JOIN ").append(tableName).append(" ").append(columns[i])
                        .append("  ON ").append(columns[i]).append(".ID = ").append(linkTableAlias)
                        .append(".").append(dataColumn).append("\n");
                linkTableAlias = columns[i];
            } else {
                //最后一个调用字段，不是外键管理而是结果集
                resultColumn = linkTableAlias + "." + dataColumn;
            }
        }
        resultMap.put("alias", resultColumn);
        resultMap.put("whereSql", whereSql.toString());
        return resultMap;
    }

}
