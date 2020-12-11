package com.whaty.dao.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.dao.support.GeneralDaoSupport;
import com.whaty.framework.exception.DataOperateException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * openSession的generalDao
 *
 * @author weipengsen
 */
@Lazy
@Component(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
public class OpenGeneralDaoImpl extends HibernateDaoSupport implements GeneralDao {

    @Resource(name = "generalDaoSupport")
    private GeneralDaoSupport generalDaoSupport;

    @Resource(name = "sessionFactory")
    public void setSessionFactory0(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public <T extends AbstractBean> T save(T transientInstance) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                session.saveOrUpdate(transientInstance);
                session.getTransaction().commit();
                return transientInstance;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public <T extends AbstractBean> T getById(Class<T> clazz, String id) throws DataOperateException {
        return this.handleExceptionWithResult(session -> (T) session.get(clazz, id));
    }

    /**
     * 根据样例查找
     *
     * @param instance
     * @return
     */
    @Override
    public <T extends AbstractBean> List<T> getByExample(T instance) throws DataOperateException {
        return this.handleExceptionWithResult(session ->
                (List<T>) this.generalDaoSupport.getByExample(session, instance));
    }

    @Override
    public int deleteByIds(Class clazz, List ids) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.deleteByIds(session, clazz, ids);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    /**
     * 根据条件查找
     */
    @Override
    public <T> List<T> getList(DetachedCriteria detachedCriteria) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getList(session, detachedCriteria));
    }

    /**
     * 删除单个
     */
    @Override
    public <T extends AbstractBean> void delete(T persistentInstance) throws DataOperateException {
        this.handleExceptionNoResult(session -> {
            session.beginTransaction();
            try {
                session.delete(persistentInstance);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public HibernateTemplate getMyHibernateTemplate() {
        return this.getHibernateTemplate();
    }

    /**
     * 批量更新一个字段
     */
    @Override
    public int updateColumnByIds(List ids, Class<? extends AbstractBean> clazz,
                                 String column, String value) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            try {
                session.beginTransaction();
                int count = this.generalDaoSupport.updateColumnByIds(session, ids, clazz, column, value);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public <T> List<T> getBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getBySQL(session, sql));
    }

    @Override
    public <T> List<T> getBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getBySQL(session, sql, args));
    }

    @Override
    public <T> List<T> getBySQLWithParameters(String sql, Map<String, Object> parametersMap) throws DataOperateException {
        return this.handleExceptionWithResult(session ->
                this.generalDaoSupport.getBySQLWithParameters(session, sql, parametersMap));
    }

    @Override
    public List<Map<String, Object>> getMapBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getMapBySQL(session, sql));
    }

    @Override
    public List<Map<String, Object>> getMapBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getMapBySQL(session, sql, args));
    }

    @Override
    public <T> T getOneBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getOneBySQL(session, sql, args));
    }

    @Override
    public Map<String, Object> getOneMapBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getOneMapBySQL(session, sql));
    }

    @Override
    public Map<String, Object> getOneMapBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getOneMapBySQL(session, sql, args));
    }

    @Override
    public <T> List<T> getByHQL(String hql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getByHQL(session, hql));
    }

    @Override
    public <T> List<T> getByHQL(String hql, String... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getByHQL(session, hql, args));
    }

    @Override
    public int executeByHQL(String hql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.executeByHQL(session, hql);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public int executeBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.executeBySQL(session, sql);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public int executeBySQL(String sql, Map<String, Object> params) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.executeBySQL(session, sql, params);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public int executeBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.executeBySQL(session, sql, args);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public EnumConst getEnumConstByNamespaceCode(String namespace, String code) throws DataOperateException {
        return this.handleExceptionWithResult(session ->
                this.generalDaoSupport.getEnumConstByNamespaceCode(session, namespace, code));
    }

    @Override
    public EnumConst getEnumConstByNamespaceAndName(String namespace, String name) throws DataOperateException {
        return this.handleExceptionWithResult(session ->
                this.generalDaoSupport.getEnumConstByNamespaceAndName(session, namespace, name));
    }

    @Override
    public int deleteByIds(Class clazz, List siteIds, List ids) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.deleteByIds(session, clazz, siteIds, ids);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public <T extends AbstractBean> T getById(Class<T> entityClass,
                                              List siteIds, String id) throws DataOperateException {
        return this.handleExceptionWithResult(session ->
                this.generalDaoSupport.getById(session, entityClass, siteIds, id));
    }

    @Override
    public int batchExecuteSql(List<String> sqlList) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.batchExecuteSql(session, sqlList);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public int batchExecuteSql(List<String> sqlList, List<Map<String, Object>> paramList) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = this.generalDaoSupport.batchExecuteSql(session, sqlList, paramList);
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public <T> T getOneByHQL(String hql) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getOneByHQL(session, hql));
    }

    @Override
    public <T> T getOneByHQL(String hql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(session -> this.generalDaoSupport.getOneByHQL(session, hql, args));
    }

    @Override
    public int saveAll(Collection<? extends AbstractBean> collection) throws DataOperateException {
        return this.handleExceptionWithResult(session -> {
            session.beginTransaction();
            try {
                int count = 0;
                for (AbstractBean instance : collection) {
                    session.saveOrUpdate(instance);
                    count++;
                }
                session.getTransaction().commit();
                return count;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        });
    }

    @Override
    public boolean checkNotEmpty(String sql, Object... args) throws DataOperateException {
        return CollectionUtils.isNotEmpty(this.getBySQL(sql, args));
    }

    @Override
    public void evictInstance(Object instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evict(Object target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evictAll(Collection targets) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session getHibernateSession() {
        return this.getOpenSession();
    }

    @Override
    public void deleteAll(Set<? extends AbstractBean> triggers) {
        triggers.forEach(this::delete);
    }

    @Override
    public int batchExecuteInsertSql(String tableName, String[] columnArray, List<Map<String, Object>> values) {
        List<String> sqlList = new LinkedList<>();
        int startIndex;
        int endIndex = 0;
        do {
            startIndex = endIndex;
            endIndex = (values.size() - startIndex) > 1000 ? (1000 + startIndex) : values.size();
            sqlList.add(this.generateInsertSql(tableName, columnArray, values.subList(startIndex, endIndex)));
        } while (endIndex < values.size());
        return this.batchExecuteSql(sqlList);
    }

    /**
     * 新建session
     *
     * @return
     * @author weipengsen
     */
    private Session getOpenSession() {
        return this.getMyHibernateTemplate().getSessionFactory().openSession();
    }

    /**
     * 异常句柄处理
     *
     * @param functional
     * @param <T>
     * @return
     */
    private <T> T handleExceptionWithResult(Function<Session, T> functional) {
        Session session = this.getOpenSession();
        try {
            return functional.apply(session);
        } catch (RuntimeException e) {
            throw new DataOperateException(e);
        } finally {
            session.close();
        }
    }

    /**
     * 异常句柄处理
     *
     * @param functional
     * @return
     */
    private void handleExceptionNoResult(Consumer<Session> functional) {
        Session session = this.getOpenSession();
        try {
            functional.accept(session);
        } catch (RuntimeException e) {
            throw new DataOperateException(e);
        } finally {
            session.close();
        }
    }

}
