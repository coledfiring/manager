package com.whaty.analyse.framework.domain;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.ConditionType;
import com.whaty.analyse.framework.domain.bean.AbstractCondition;
import com.whaty.common.string.StringUtils;
import com.whaty.framework.exception.UncheckException;
import com.whaty.handler.tree.TreeUtils;
import com.whaty.utils.StaticBeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统计条件VO
 *
 * @author weipengsen
 */
@Data
public class AnalyseConditionVO implements Serializable {

    private static final long serialVersionUID = -6379256522023668143L;

    private String label;

    private String type;

    private Collection<?> options;

    private String dataIndex;

    private Integer serial;

    private Object defaultValue;

    public AnalyseConditionVO(String label, ConditionType type, String dataIndex,
                              Integer serial) {
        this.label = label;
        this.type = type.getType();
        this.dataIndex = dataIndex;
        this.serial = serial;
    }

    /**
     * 将条件do转换为条件vo
     * @param origin
     * @return
     */
    public static List<AnalyseConditionVO> convert(List<? extends AbstractCondition> origin,
                                                   Map<String, Object> params) {
        return Optional.ofNullable(origin).map(l -> l.stream().filter(Objects::nonNull)
                .map(e -> new AnalyseConditionVO(e.getLabel(),
                        ConditionType.getTypeByCode(e.getEnumConstByFlagBlockConditionType().getCode()),
                        e.getDataIndex(), e.getSerial()).init(e, params))
                .collect(Collectors.toList())).orElse(null);
    }

    /**
     * 初始化
     * @param condition
     * @param params
     * @return
     */
    private AnalyseConditionVO init(AbstractCondition condition, Map<String, Object> params) {
        this.setDefaultValue(Optional.ofNullable(condition.getDefaultSql())
                .map(e -> AnalyseUtils.handleSql(e, params))
                .map(e -> AnalyseUtils.composeSwitchSite(() ->
                        StaticBeanUtils.getOpenGeneralDao().getOneBySQL(e)).get())
                .orElse(null));
        if (!condition.needOptions()) {
            return this;
        }
        List<Map<String, Object>> options;
        if (StringUtils.isNotBlank(condition.getReflectMethod())) {
            options = this.getOptionsFromReflect(condition.getReflectMethod(), params);
        } else if (StringUtils.isNotBlank(condition.getSql())) {
            options = this.getOptionsFromSql(condition.getSql(), params);
        } else {
            throw new IllegalArgumentException("sql and reflectMethod for condition not found");
        }
        if ("2".equals(condition.getEnumConstByFlagBlockConditionType().getCode())
                || "4".equals(condition.getEnumConstByFlagBlockConditionType().getCode())) {
            this.setOptions(SelectOption.convert(options));
        } else if ("5".equals(condition.getEnumConstByFlagBlockConditionType().getCode())) {
            this.setOptions(TreeUtils.buildTree(options));
        }
        return this;
    }

    /**
     * 从反射中获取选项
     * @param reflectMethod
     * @param params
     * @return
     */
    private List<Map<String, Object>> getOptionsFromReflect(String reflectMethod, Map<String, Object> params) {
        String[] invokeMethodArr = reflectMethod.split("@");
        try {
            Method targetMethod = Class.forName(invokeMethodArr[0]).getMethod(invokeMethodArr[1], Map.class);
            if (!Modifier.isStatic(targetMethod.getModifiers())) {
                throw new IllegalArgumentException();
            }
            if (!List.class.isAssignableFrom(targetMethod.getReturnType())) {
                throw new IllegalArgumentException();
            }
            return (List<Map<String, Object>>) targetMethod.invoke(null, params);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 从数据库中查询选项
     * @param sql
     * @param params
     * @return
     */
    private List<Map<String, Object>> getOptionsFromSql(String sql, Map<String, Object> params) {
        return Optional.ofNullable(sql)
                .map(e -> AnalyseUtils.handleSql(e, params))
                .map(e -> AnalyseUtils.composeSwitchSite(() ->
                        StaticBeanUtils.getOpenGeneralDao().getMapBySQL(e)).get())
                .orElse(null);
    }

    /**
     * 抽象下拉
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static abstract class AbstractOption implements Serializable {

        private static final long serialVersionUID = -7471475188719581948L;

        protected String id;

        protected String label;
    }

    /**
     * 下拉选项
     *
     * @author weipengsen
     */
    @Data
    public static class SelectOption extends AbstractOption {

        private static final long serialVersionUID = -9160912353829445211L;

        public SelectOption(String id, String label) {
            super(id, label);
        }

        /**
         * 转换数据
         * @param origin
         * @return
         */
        public static List<SelectOption> convert(List<Map<String, Object>> origin) {
            return origin.stream()
                    .map(o -> new SelectOption((String) o.get("id"), (String) o.get("label")))
                    .collect(Collectors.toList());
        }

    }

}
