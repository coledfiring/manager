package com.whaty.dao.support;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.exception.DataOperateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用dao操作提供类
 *
 * @author weipengsen
 */
@Lazy
@Component("generalDaoSupport")
public class GeneralDaoSupport {

    private static final String MORE_THAN_ONE_RESULT_MESSAGE = "Expected one result(or null) but found more";

    /**
     * 根据样例获取实体类
     *
     * @param session
     * @param instance
     * @return
     */
    public <T extends AbstractBean> List getByExample(Session session, T instance) {
        return session.createCriteria(instance.getClass().getName())
                .add(Example.create(instance)).list();
    }

    /**
     * 通过ids删除数据
     *
     * @param session
     * @param clazz
     * @param ids
     * @return
     */
    public int deleteByIds(Session session, Class clazz, List ids) {
        Query query = session.createQuery("delete from " + clazz.getName() + " where id in(:ids)");
        query.setParameterList(CommonConstant.PARAM_IDS, ids);
        return query.executeUpdate();
    }

    /**
     * 通过detachedCriteria获取数据
     *
     * @param session
     * @param detachedCriteria
     * @return
     */
    public <T> List<T> getList(Session session, DetachedCriteria detachedCriteria) {
        return detachedCriteria.getExecutableCriteria(session).list();
    }

    /**
     * 通过ids更新字段
     *
     * @param session
     * @param ids
     * @param clazz
     * @param column
     * @param value
     * @return
     */
    public int updateColumnByIds(Session session, List ids, Class<? extends AbstractBean> clazz,
                                 String column, String value) {
        String[] columns = column.split(CommonConstant.SPLIT_ID_SIGN);
        String[] values = value.split(CommonConstant.SPLIT_ID_SIGN);
        if (columns.length != values.length) {
            throw new RuntimeException("更新操作 列与值数量不匹配");
        }

        StringBuilder sqlPrepare = new StringBuilder(" set");
        Arrays.stream(columns).forEach(e -> sqlPrepare.append(" n.").append(e).append(" = ? ,"));
        sqlPrepare.delete(sqlPrepare.lastIndexOf(","), sqlPrepare.length());

        String sql = "update " + clazz.getName() + " n " + sqlPrepare + " where n.id in(:ids)";
        Query query = session.createQuery(sql);

        for (int j = 0; j < values.length; j++) {
            if (columns[j].toLowerCase().endsWith("date")) {
                query.setDate(j, new Date(Long.parseLong(values[j])));
            } else {
                query.setString(j, values[j]);
            }
        }
        query.setParameterList(CommonConstant.PARAM_IDS, ids);
        return query.executeUpdate();
    }

    /**
     * 通过sql获取数据
     *
     * @param session
     * @param sql
     * @param args
     * @return
     */
    public List getBySQL(Session session, String sql, Object... args) {
        Query query = session.createSQLQuery(sql);
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return query.list();
    }

    /**
     * 通过参数和sql获取数据
     *
     * @param session
     * @param sql
     * @param parametersMap
     * @return
     */
    public List getBySQLWithParameters(Session session, String sql, Map<String, Object> parametersMap) {
        Query query = session.createSQLQuery(sql);
        Set<String> keys = parametersMap.keySet();
        for (String key : keys) {
            Object valueObj = parametersMap.get(key);
            if (valueObj instanceof Object[]) {
                Object[] value = (Object[]) parametersMap.get(key);
                query.setParameterList(key, value);
            } else if (valueObj instanceof List) {
                List value = (List) parametersMap.get(key);
                query.setParameterList(key, value);
            } else {
                query.setParameter(key, parametersMap.get(key));
            }
        }
        return query.list();
    }

    /**
     * 通过sql获取map类型数据
     *
     * @param session
     * @param sql
     * @return
     */
    public List<Map<String, Object>> getMapBySQL(Session session, String sql, Object... args) {
        Query query = session.createSQLQuery(sql);
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return (List<Map<String, Object>>) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 通过hql获取数据
     *
     * @param session
     * @param hql
     * @return
     */
    public <T> List<T> getByHQL(Session session, String hql) {
        return session.createQuery(hql).list();
    }

    /**
     * 通过hql和参数获取数据
     *
     * @param session
     * @param hql
     * @param args
     * @return
     */
    public <T> List<T> getByHQL(Session session, String hql, String... args) {
        Query query = session.createQuery(hql);
        for (int i = 0; i < args.length; i++) {
            query.setString(i, args[i]);
        }
        return query.list();
    }

    /**
     * 执行hql更新
     *
     * @param session
     * @param hql
     * @return
     */
    public int executeByHQL(Session session, String hql) {
        return session.createQuery(hql).executeUpdate();
    }

    /**
     * 执行sql更新
     *
     * @param session
     * @param sql
     * @return
     */
    public int executeBySQL(Session session, String sql) {
        return session.createSQLQuery(sql).executeUpdate();
    }

    /**
     * 通过sql和参数执行更新
     *
     * @param session
     * @param sql
     * @param params
     * @return
     */
    public int executeBySQL(Session session, String sql, Map<String, Object> params) {
        Query query = session.createSQLQuery(sql);
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((key, val) -> {
                if (val instanceof List) {
                    query.setParameterList(key, (List) val);
                } else {
                    query.setParameter(key, val);
                }
            });
        }
        return query.executeUpdate();
    }

    /**
     * 通过sql和参数执行更新
     *
     * @param session
     * @param sql
     * @param args
     * @return
     */
    public int executeBySQL(Session session, String sql, Object... args) {
        Query query = session.createSQLQuery(sql);
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return query.executeUpdate();
    }

    /**
     * 根据namespace和code获取enumConst
     *
     * @param session
     * @param namespace
     * @param code
     * @return
     */
    public EnumConst getEnumConstByNamespaceCode(Session session, String namespace, String code) {
        String hql = "from EnumConst e where e.namespace='" + namespace + "' and e.code='" + code + "'";
        List<EnumConst> enumConstList = session.createQuery(hql).list();
        if (CollectionUtils.isNotEmpty(enumConstList)) {
            return enumConstList.get(0);
        }
        return null;
    }

    /**
     * 通过namespace和name获取enumConst
     *
     * @param session
     * @param namespace
     * @param name
     * @return
     */
    public EnumConst getEnumConstByNamespaceAndName(Session session, String namespace, String name) {
        String hql = "from EnumConst e where e.namespace='" + namespace + "' and e.name='" + name + "'";
        List<EnumConst> enumConstList = session.createQuery(hql).list();
        if (CollectionUtils.isNotEmpty(enumConstList)) {
            return enumConstList.get(0);
        }
        return null;
    }

    /**
     * 通过ids删除含有peSite的表的数据
     *
     * @param session
     * @param clazz
     * @param siteIds
     * @param ids
     * @return
     */
    public int deleteByIds(Session session, Class clazz, List siteIds, List ids) {
        Query query = session.createQuery("delete from "
                + clazz.getName() + " where id in(:ids) and peSite.id in(:siteIds)");
        query.setParameterList("ids", ids);
        query.setParameterList("siteIds", siteIds);
        return query.executeUpdate();
    }

    /**
     * 根据id获取实体类
     *
     * @param session
     * @param entityClass
     * @param siteIds
     * @param id
     * @param <T>
     * @return
     */
    public <T extends AbstractBean> T getById(Session session, Class<T> entityClass,
                                              List siteIds, String id) {
        Query query = session.createQuery("from " + entityClass.getName() +
                " where id in(:id) and peSite.id in(:siteIds)");
        query.setParameter("id", id);
        query.setParameterList("siteIds", siteIds);
        List list = query.list();
        if (CollectionUtils.isNotEmpty(list)) {
            return (T) list.get(0);
        }
        return null;
    }

    /**
     * 批量执行sql更新
     *
     * @param session
     * @param sqlList
     * @return
     */
    public int batchExecuteSql(Session session, List<String> sqlList) {
        int result = 0;
        for (String sql : sqlList) {
            Query query = session.createSQLQuery(sql);
            result += query.executeUpdate();
        }
        return result;
    }

    /**
     * 批量执行sql更新
     *
     * @param session
     * @param sqlList
     * @param paramList
     * @return
     */
    public int batchExecuteSql(Session session, List<String> sqlList, List<Map<String, Object>> paramList) {
        int i = 0;
        for (int k = 0; k < sqlList.size(); ++k) {
            Query query = session.createSQLQuery(sqlList.get(k));
            if (CollectionUtils.isNotEmpty(paramList)) {
                Map<String, Object> params = paramList.get(k);
                params.forEach((key, val) -> {
                    if (val instanceof List) {
                        query.setParameterList(key, (List) val);
                    } else {
                        query.setParameter(key, val);
                    }
                });
            }
            i += query.executeUpdate();
        }

        return i;
    }

    /**
     * 使用hql获取一条数据
     * @param session
     * @param hql
     * @param args
     * @return
     */
    public <T> T getOneByHQL(Session session, String hql, Object... args) {
        Query query = session.createQuery(hql);
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    /**
     * 通过sql获取一个map集合
     * @param session
     * @param sql
     * @return
     */
    public Map<String, Object> getOneMapBySQL(Session session, String sql, Object... args) {
        List<Map<String, Object>> result = this.getMapBySQL(session, sql, args);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        if (result.size() > 1) {
            throw new DataOperateException(MORE_THAN_ONE_RESULT_MESSAGE);
        }
        return result.get(0);
    }

    /**
     * 通过sql获取一个数据
     * @param session
     * @param sql
     * @param args
     * @return
     */
    public <T> T getOneBySQL(Session session, String sql, Object[] args) {
        List<Object> result = this.getBySQL(session, sql, args);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        if (result.size() > 1) {
            throw new DataOperateException(MORE_THAN_ONE_RESULT_MESSAGE);
        }
        return (T) result.get(0);
    }

}
