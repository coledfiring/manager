package com.whaty.framework.aop.operatelog.strategy;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.aspectj.lang.JoinPoint;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * sql收集策略
 *
 * @author weipengsen
 */
public class SqlCollectStrategy extends AbstractCollectStrategy<SqlRecord> {

    private GeneralDao generalDao;

    private final static Pattern SQL_ID_PATTERN = Pattern.compile("\\s+(as|AS|aS|As)\\s+id\\s*,");

    public SqlCollectStrategy(Map<String, Object> params, SqlRecord annotation, JoinPoint join) {
        super(params, annotation, join);
        this.generalDao = StaticBeanUtils.getGeneralDao();
    }

    @Override
    public List<Map<String, Object>> collectData() {
        if (!SQL_ID_PATTERN.matcher(this.annotation.sql()).find()) {
            throw new IllegalArgumentException("sql in sqlRecord must has alias 'id'");
        }
        String sql = this.annotation.sql().replace("${siteCode}", MasterSlaveRoutingDataSource.getDbType());
        if (StringUtils.isNotBlank(UserUtils.getCurrentUserId())) {
            sql = sql.replace("${currentUserId}", UserUtils.getCurrentUserId());
            sql = ScopeHandleUtils.handleScopeSignOfSql(sql, UserUtils.getCurrentUserId());
        }
        if (!SQLHandleUtils.checkAliasExists(sql, this.params)) {
            return null;
        }
        this.params.computeIfPresent(CommonConstant.PARAM_IDS, (k, o) -> IdsTypeEnum.convertIdsToString(o));
        return this.generalDao.getMapBySQL(SQLHandleUtils.replaceSignUseParams(sql, this.params));
    }

    enum IdsTypeEnum {

        /**
         * 集合类型
         */
        COLLECTION_TYPE(Collection.class::isAssignableFrom, e -> ((Collection) e).stream().findFirst()),
        /**
         * 数组类型
         */
        ARRAY_TYPE(Class::isArray, e -> Arrays.stream((String[]) e).findFirst()),
        /**
         * 字符串类型
         */
        STRING_TYPE(String.class::isAssignableFrom, e -> Optional.ofNullable((String) e)),
        ;

        private Predicate<Class> typePredicate;

        private Function<Object, Optional<String>> idsFunction;

        IdsTypeEnum(Predicate<Class> typePredicate, Function<Object, Optional<String>> idsFunction) {
            this.typePredicate = typePredicate;
            this.idsFunction = idsFunction;
        }

        /**
         * 将传入的ids对象转化成string[]
         * @param ids
         * @return
         */
        static String[] convertIdsToString(Object ids) {
            Optional<Optional<String>> temp = Arrays.stream(values())
                    .filter(e -> e.getTypePredicate().test(ids.getClass())).findFirst()
                    .map(IdsTypeEnum::getIdsFunction).map(e -> e.apply(ids));
            if (!temp.isPresent()) {
                throw new IllegalArgumentException();
            }
            return temp.get().map(e -> e.split(CommonConstant.SPLIT_ID_SIGN)).get();
        }

        public Predicate<Class> getTypePredicate() {
            return typePredicate;
        }

        public Function<Object, Optional<String>> getIdsFunction() {
            return idsFunction;
        }
    }

    @Override
    public String namespace() {
        return this.annotation.namespace();
    }
}
