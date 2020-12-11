package com.whaty.framework.grid.optimizedgrid.searchsql;

import com.whaty.core.commons.util.OrderItem;
import com.whaty.core.framework.grid.bean.ColumnConfig;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.search.GridSearchSql;
import com.whaty.core.framework.grid.search.scope.ScopeManager;
import com.whaty.core.framework.grid.util.SQLExpressionParse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 优化grid查询处理类
 *
 * @author weipengsen
 */
public class OptimizedGridSearchSql extends GridSearchSql {

    /**
     * where通配符开始字符
     */
    private final static String WHERE_SIGN_START = "{where";
    /**
     * where通配符结束字符
     */
    private final static String WHERE_SIGN_END = "[sql_where]}";

    /**
     * having通配符开始字符
     */
    private final static String HAVING_SIGN_START = "{having";
    /**
     * having通配符结束字符
     */
    private final static String HAVING_SIGN_END = "[sql_having]}";

    /**
     * order通配符开始字符
     */
    private final static String ORDER_SIGN_START = "{order by";
    /**
     * order通配符结束字符
     */
    private final static String ORDER_SIGN_END = "[sql_orderby]}";
    /**
     * order通配符正则
     */
    private final static String ORDER_SIGN_REG_EXP = "\\{order by(.*)\\[sql_orderby\\]\\}";
    /**
     * group by通配符正则
     */
    private final static String GROUP_BY_SIGN_REG_EXP = "\\s+group\\s+by\\s+";
    /**
     * 计数字符
     */
    private final static String RESULT_COUNT_STR = "count(*)";
    /**
     * 结果集通配符
     */
    private final static String RESULT_SIGN = "[sql_columns]";
    /**
     * group by正则对象
     */
    public static final Pattern GROUP_BY_PATTERN = Pattern.compile(GROUP_BY_SIGN_REG_EXP);

    public OptimizedGridSearchSql(ScopeManager scopeManager, GridConfig gridConfig,
                           Map<String, Object> searchParams) {
        super(scopeManager, gridConfig, searchParams);
    }

    public OptimizedGridSearchSql(ScopeManager scopeManager, GridConfig gridConfig,
                                  Map<String, Object> searchParams, List<OrderItem> orderParams) {
        super(scopeManager, gridConfig, searchParams, orderParams);
    }

    @Override
    public void processSql() {
        String sql = (String) this.getScopeManager()
                .setScopes(this.getGridConfig().gridConfigSource().getSql());
        this.setSql(new StringBuffer(sql));
        boolean isGroupSql = GROUP_BY_PATTERN.matcher(sql.toLowerCase()).find();
        //where条件拼接
        if (isGroupSql && this.getSql().toString().contains(HAVING_SIGN_END)) {
            this.setSql(new StringBuffer(this.setHaving(this.getSql().toString(), this.getSearchParams())));
        } else {
            this.setSql(new StringBuffer(this.setWhere(this.getSql().toString(), this.getSearchParams())));
        }
        //countSql
        if (StringUtils.isBlank(this.getGridConfig().gridConfigSource().getCountSql())) {
            if (isGroupSql) {
                StringBuffer countSql = new StringBuffer(this.getSql().toString()
                        .replace(RESULT_SIGN, this.getColumnResult()).replaceAll(ORDER_SIGN_REG_EXP, ""));
                countSql.insert(0, "select count(1) from (").append(") r");
                this.setCountSql(countSql);
            } else {
                this.setCountSql(new StringBuffer(this.getSql().toString()
                        .replace(RESULT_SIGN, RESULT_COUNT_STR).replaceAll(ORDER_SIGN_REG_EXP, "")));
            }
        } else {
            String countSql = (String) this.getScopeManager()
                    .setScopes(this.getGridConfig().gridConfigSource().getCountSql());
            this.setCountSql(new StringBuffer(this.setWhere(countSql, this.getSearchParams())));
        }
        //排序
        this.setOrder();
        //把column拼接到占位符
        this.setSql(new StringBuffer(this.getSql().toString().replace(RESULT_SIGN, this.getColumnResult())));
    }

    /**
     * 拼接having语句并组合到sql中
     * @param sql
     * @param searchParams
     * @return
     */
    protected String setHaving(String sql, Map<String, Object> searchParams) {
        sql = this.setWhere(sql, null);
        String havingSql = this.getConditionSql(searchParams, null);
        // 替换sql的where字句
        sql = StringUtils.replace(sql, HAVING_SIGN_START,
                this.includeIsNotBlank(sql, HAVING_SIGN_START, HAVING_SIGN_END) ? "HAVING 1=1 and " : "HAVING 1=1 ");
        sql = StringUtils.replace(sql, HAVING_SIGN_END, havingSql);
        return sql;
    }

    /**
     * 判断包围的内容不为空
     * @param sql
     * @param startSign
     * @param endSign
     * @return
     */
    private boolean includeIsNotBlank(String sql, String startSign, String endSign) {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("argument 'sql' is blank");
        }
        if (StringUtils.isBlank(startSign) || StringUtils.isBlank(endSign)) {
            throw new IllegalArgumentException("argument that any sign is blank");
        }
        int startSignEndIndex = sql.indexOf(startSign);
        if (startSignEndIndex == -1) {
            throw new IllegalArgumentException(String
                    .format("not found the start sign '%s' in sql [%s]", startSign, sql));
        }
        int endSignStartIndex = sql.indexOf(endSign);
        if (endSignStartIndex == -1) {
            throw new IllegalArgumentException(String.format("not found the end sign '%s' in sql [%s]", endSign, sql));
        }
        String includeStr = sql.substring(startSignEndIndex + startSign.length(), endSignStartIndex).trim();
        return StringUtils.isNotBlank(includeStr);
    }

    /**
     * 将column拼接到占位符
     */
    protected String getColumnResult() {
        StringBuilder resultStr = new StringBuilder();
        //拿到所有的column
        List<ColumnConfig> list = this.getGridConfig().getListColumnConfig();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ColumnConfig columnConfig : list) {
                String dateIndex = columnConfig.getDataIndex();
                if (dateIndex.toLowerCase().startsWith("combobox_")) {
                    dateIndex = dateIndex.substring(dateIndex.lastIndexOf(".") + 1);
                }
                dateIndex = columnConfig.getSqlResult() + " as " + dateIndex;
                resultStr.append(" ").append(dateIndex).append(", ");
            }
        }
        return resultStr.substring(0, resultStr.lastIndexOf(",")).toString();
    }

    /**
     * 拼接where语句并组合到sql中
     * @param sql
     * @param searchParams
     */
    protected String setWhere(String sql, Map<String, Object> searchParams) {
        String whereSql = this.getConditionSql(searchParams, null);
        // 替换sql的where字句
        sql = StringUtils.replace(sql, WHERE_SIGN_START,
                this.includeIsNotBlank(sql, WHERE_SIGN_START, WHERE_SIGN_END) ? "where 1=1 and " : "where 1=1 ");
        sql = StringUtils.replace(sql, WHERE_SIGN_END, whereSql);
        return sql;
    }

    /**
     * 拼接条件语句
     * @param searchParams
     * @param superName
     * @return
     */
    private String getConditionSql(Map<String, Object> searchParams, String superName) {
        StringBuilder conditionSql = new StringBuilder();
        // 查询条件
        if(MapUtils.isNotEmpty(searchParams)) {
            for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Map) {
                    conditionSql.append(this.getConditionSql((Map)value, (StringUtils
                            .isNotBlank(superName) ? superName + "." : "") + name));
                } else {
                    String strValue;
                    if (value == null) {
                        break;
                    } else if (value instanceof String[]) {
                        strValue = ((String[])value)[0].trim();
                    } else {
                        strValue = String.valueOf(value).trim();
                    }
                    if(StringUtils.isNotBlank(strValue)) {
                        name = this.getGridConfig().getColumByDateIndex((StringUtils
                                .isNotBlank(superName) ? superName + "." : "") + name).getSqlResult();

                        try {
                            conditionSql.append(SQLExpressionParse.ExpressionParse(strValue, name));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return conditionSql.toString();
    }

    /**
     * 拼接排序语句
     */
    protected void setOrder() {
        StringBuilder orderSql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(this.getOrderParams())) {
            for(int i = 0; i < this.getOrderParams().size(); ++i) {
                OrderItem sort = this.getOrderParams().get(i);
                if (sort != null && StringUtils.isNotBlank(sort.getDataIndex())) {
                    String dataIndex = sort.getDataIndex();
                    if (dataIndex.contains(".")) {
                        dataIndex = dataIndex.substring(dataIndex.lastIndexOf(".") + 1);
                    }

                    if ("desc".equalsIgnoreCase(sort.getSort())) {
                        orderSql.append(" ").append(dataIndex).append(" desc");
                    } else {
                        orderSql.append(" ").append(dataIndex).append(" asc");
                    }

                    if (i < this.getOrderParams().size() - 1) {
                        orderSql.append(",");
                    }
                }
            }
        }
        String sql = this.getSql().toString();
        // 替换sql的where字句
        if (orderSql.length() == 0) {
            sql = StringUtils.replace(sql, ORDER_SIGN_START, "");
        } else {
            sql = StringUtils.replace(sql, ORDER_SIGN_START, " order by ");
        }
        sql = StringUtils.replace(sql, ORDER_SIGN_END, orderSql.toString());
        this.setSql(new StringBuffer(sql));
    }

}
