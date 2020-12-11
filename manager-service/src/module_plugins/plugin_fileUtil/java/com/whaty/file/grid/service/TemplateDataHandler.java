package com.whaty.file.grid.service;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.search.scope.ScopeManager;
import com.whaty.dao.GeneralDao;
import com.whaty.file.domain.bean.PePrintTemplate;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.template.constant.TemplateConstant;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * 模板数据获取处理器
 *
 * @author weipengsen
 */
public class TemplateDataHandler {

    private final GeneralDao generalDao;

    private final PePrintTemplate template;

    private ScopeManager<String> sqlScopeManager;

    private Map<String, String> extraData;

    private String[] sqlArr;

    private final static Logger logger = LoggerFactory.getLogger(TemplateDataHandler.class);

    private final static Predicate<String> IS_LIST_SIGN = k -> k.startsWith(TemplateConstant.TEMPLATE_LIST_PREFIX)
            || k.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_PREFIX)
            || k.startsWith(TemplateConstant.TEMPLATE_LIST_MORE_COLUMN_LEFT_FULL_PREFIX)
            || k.startsWith(TemplateConstant.TEMPLATE_LIST_MARK_PREFIX);

    public TemplateDataHandler(PePrintTemplate template) {
        this.generalDao = StaticBeanUtils.getGeneralDao();
        this.sqlScopeManager = (ScopeManager<String>) SpringUtil.getBean(CommonConstant.SQL_SCOPE_MANAGER_BEAN_NAME);
        this.template = template;
        this.template.buildSignKeySet();
    }

    /**
     * 获取显示使用的额外参数
     * @return
     */
    private Map<String, String> getExtraData() {
        Map<String, String> extraData = this.getSystemVariables();
        extraData.putAll(this.getConfigData());
        return extraData;
    }

    /**
     * 从sql中查询数据，一个主sql多个从sql，使用';'分割，其中主中的结果集可以作为从的条件，第一个sql为主sql
     *
     * @param args
     * @return
     */
    public List<Map<String, Object>> getTemplateData(Map<String, String> args) {
        args.putAll(this.collectExtraParam());
        //处理sql
        this.sqlArr = this.handleSpecialSign(this.handleSql(this.template.getSearchSql()))
                .split(PrintConstant.MAIN_SUB_SQL_SPLIT_SIGN);
        for (int i = 0; i < this.sqlArr.length; i++) {
            this.sqlArr[i] = this.setArgsToSql(args, this.sqlArr[i]);
        }
        if (this.checkSqlHasSign(this.sqlArr[0])) {
            if (logger.isWarnEnabled()) {
                logger.warn("master sql has the sign that can not replace in template '%s'", this.template.getId());
            }
            throw new ServiceException("模板配置有误，请联系管理员");
        }
        List<Map<String, Object>> pageData = this.generalDao.getMapBySQL(this.sqlArr[0]);
        if (CollectionUtils.isEmpty(pageData)) {
            throw new ServiceException("选中的记录没有可打印的数据");
        }
        this.extraData = this.getExtraData();
        return this.convertData(pageData);
    }

    /**
     * 收集额外配置
     *
     * @return
     */
    private Map<String, String> collectExtraParam() {
        Map<String, String> extraParam = new HashMap<>(16);
        if (StaticBeanUtils.getUserService().getCurrentUser() != null) {
            extraParam.put("currentUserId", UserUtils.getCurrentUserId());
            if (UserUtils.getCurrentUnit() != null) {
                extraParam.put("currentUnitId", UserUtils.getCurrentUnit().getId());
            }
        }
        return extraParam;
    }

    /**
     * 将查询出的数据转换成打印使用的数据类型
     *
     * @param dataList
     * @return
     */
    private List<Map<String, Object>> convertData(List<Map<String, Object>> dataList) {
        return dataList.stream().map(this::buildPage).peek(e -> e.putAll(this.extraData)).peek(this::getSubData)
                .collect(Collectors.toList());
    }

    /**
     * 构建页面对象
     *
     * @param origin
     * @return
     */
    private Map<String, Object> buildPage(Map<String, Object> origin) {
        return origin.entrySet().stream().filter(en -> this.template.signExists(en.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        en -> this.getValidValue(en.getKey(), en.getValue())));
    }

    /**
     * 获取子数据
     * @param page
     */
    private void getSubData(Map<String, Object> page) {
        for (int i = 1; i < this.sqlArr.length; i++) {
            this.getSubData(i, page);
        }
    }

    /**
     * 获取子查询的数据
     *
     * @param index
     * @param page
     */
    private void getSubData(int index, Map<String, Object> page) {
        String sqlTemp = this.setArgsToSql(page, this.sqlArr[index]);
        if (this.checkSqlHasSign(sqlTemp)) {
            if (logger.isWarnEnabled()) {
                logger.warn("sub sql [%s] has the sign that can not replace in template '%s'",
                        this.sqlArr[index], this.template.getId());
            }
            throw new ServiceException("模板配置有误，请联系管理员");
        }
        List<Map<String, Object>> subDataList = this.generalDao.getMapBySQL(sqlTemp);
        if (CollectionUtils.isEmpty(subDataList)) {
            return;
        }
        if (subDataList.size() == 1) {
            subDataList.get(0).entrySet().stream()
                    .filter(e -> !IS_LIST_SIGN.test(e.getKey()))
                    .forEach(e -> page.put(e.getKey(), this.getValidValue(e.getKey(), e.getValue())));
            return;
        }
        List<Map<String, String>> list = subDataList.stream().map(this::convertToSubData).collect(Collectors.toList());
        page.put("list" + index, list);
    }

    /**
     * 转换子数据
     * @param origin
     * @return
     */
    private Map<String, String> convertToSubData(Map<String, Object> origin) {
        return origin.entrySet().stream().filter(e -> this.template.signExists(e.getKey()))
                .filter(e -> IS_LIST_SIGN.test(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> this.getValidValue(e.getKey(), e.getValue())));
    }

    /**
     * 获取有效值
     *
     * @param key
     * @param value
     * @return
     */
    private String getValidValue(String key, Object value) {
        if (key.endsWith(TemplateConstant.TEMPLATE_PICTURE_SUFFIX)) {
            return this.getNoNullPictureValue(value);
        } else {
            return this.template.encodeIllegalSign(this.getNoNullValue(value));
        }
    }

    /**
     * 检查sql中是否存在占位符
     *
     * @param sql
     * @return
     */
    private boolean checkSqlHasSign(String sql) {
        return PrintConstant.REG_PATTERN_ARG_SIGN.matcher(sql).find();
    }

    /**
     * 处理特殊字符
     *
     * @param sql
     * @return
     */
    private String handleSpecialSign(String sql) {
        return SQLHandleUtils.handleSignInSQL(sql);
    }

    /**
     * 获取非空的图片路径
     *
     * @param value
     * @return
     */
    private String getNoNullPictureValue(Object value) {
        boolean fileIsNotExists = StringUtils.isBlank((String) value)
                || !new File(CommonUtils.getRealPath((String) value)).exists();
        return CommonUtils.getRealPath(fileIsNotExists ? PrintConstant.DEFAULT_PICTURE_PATH : (String) value);
    }

    /**
     * 获取非空的字符值
     *
     * @param value
     * @return
     */
    private String getNoNullValue(Object value) {
        return Optional.ofNullable(value).map(String::valueOf).orElse("");
    }

    /**
     * 获取系统属性
     *
     * @return
     */
    private Map<String, String> getSystemVariables() {
        return this.generalDao.<Object[]>getBySQL("select name, value from system_variables where site_code = ?",
                SiteUtil.getSiteCode()).stream().collect(Collectors.toMap(e -> (String) e[0], e -> (String) e[1]));
    }

    /**
     * 获取配置信息，日期等
     *
     * @return
     */
    private Map<String, String> getConfigData() {
        return TycjCollectionUtils.map("config_date", CommonUtils.changeDateToString(new Date()));
    }

    /**
     * 将参数设置到sql中
     *
     * @param args
     * @param sql
     * @return
     */
    private String setArgsToSql(Map<String, ?> args, String sql) {
        Matcher m = PrintConstant.REG_PATTERN_ARG_SIGN.matcher(sql);
        while (m.find()) {
            String sign = m.group();
            String[] signArr = sign.replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                    .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "")
                    .split(PrintConstant.SEARCH_SQL_ARG_SIGN_SPLIT);
            String argKey = signArr[1];
            if (args.containsKey(argKey)) {
                String condition = CommonUtils.madeSqlIn(this.getNoNullValue(args.get(argKey)), signArr[0]);
                sql = sql.replace(sign, condition);
            }
        }
        return this.setScopeToSql(sql);
    }

    /**
     * 设置横向权限
     *
     * @param sql
     * @return
     */
    private String setScopeToSql(String sql) {
        return this.sqlScopeManager.setScopes(sql);
    }

    /**
     * 获得查询sql，子类可以复写此方法进行sql的改写
     *
     * @return
     */
    protected String handleSql(String sql) {
        return sql;
    }

}
