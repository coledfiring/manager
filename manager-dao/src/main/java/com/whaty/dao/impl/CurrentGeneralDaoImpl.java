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
import java.util.function.Supplier;

/**
 * 通用dao层
 *
 * @author weipengsen
 */
@SuppressWarnings("ALL")
@Lazy
@Component(CommonConstant.GENERAL_DAO_BEAN_NAME)
public class CurrentGeneralDaoImpl extends HibernateDaoSupport implements GeneralDao {

    @Resource(name = "generalDaoSupport")
    private GeneralDaoSupport generalDaoSupport;

    @Resource(name = "sessionFactory")
    public void setSessionFactory0(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public <T extends AbstractBean> T save(T transientInstance) throws DataOperateException {
        return this.handleExceptionWithResult(() -> {
            this.getCurrentSession().saveOrUpdate(transientInstance);
            return transientInstance;
        });
    }

    @Override
    public <T extends AbstractBean> T getById(Class<T> clazz, String id) throws DataOperateException {
        return this.handleExceptionWithResult(() -> (T) getHibernateTemplate().get(clazz, id));
    }

    /**
     * 根据样例查找
     *
     * @param instance
     * @return
     */
    @Override
    public <T extends AbstractBean> List getByExample(final T instance) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.getHibernateTemplate().executeFind(session ->
                this.generalDaoSupport.getByExample(session, instance)));
    }

    @Override
    public int deleteByIds(Class clazz, List ids) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                this.generalDaoSupport.deleteByIds(this.getCurrentSession(), clazz, ids));
    }

    /**
     * 根据条件查找
     */
    @Override
    public <T> List<T> getList(final DetachedCriteria detachedCriteria) throws DataOperateException {
        return (List<T>) this.handleExceptionWithResult(() -> this.getHibernateTemplate().executeFind(session ->
                this.generalDaoSupport.<T>getList(session, detachedCriteria)));
    }

    /**
     * 删除单个
     */
    @Override
    public <T extends AbstractBean> void delete(T persistentInstance) throws DataOperateException {
        this.handleExceptionNoResult(session -> session.delete(persistentInstance), this.getCurrentSession());
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
        return this.handleExceptionWithResult(() ->
                this.generalDaoSupport.updateColumnByIds(this.getCurrentSession(), ids, clazz, column, value));
    }

    /**
     * 在service中拼好sql传入，返回的List为数组而非对象
     */
    @Override
    public <T> List<T> getBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getBySQL(session, sql)));
    }

    /**
     * 在service中拼好sql传入，返回的List为数组而非对象
     */
    @Override
    public <T> List<T> getBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() -> getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getBySQL(session, sql, args)));
    }

    @Override
    public <T> List<T> getBySQLWithParameters(String sql, Map<String, Object> parametersMap) {
        return getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getBySQLWithParameters(session, sql, parametersMap));
    }

    @Override
    public List<Map<String, Object>> getMapBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> (List<Map<String, Object>>) this.getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getMapBySQL(session, sql)));
    }

    @Override
    public List<Map<String, Object>> getMapBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() -> (List<Map<String, Object>>) this.getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getMapBySQL(session, sql, args)));
    }

    @Override
    public <T> T getOneBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                getHibernateTemplate().execute(session -> this.generalDaoSupport.getOneBySQL(session, sql, args)));
    }

    @Override
    public Map<String, Object> getOneMapBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                getHibernateTemplate().execute(session -> this.generalDaoSupport.getOneMapBySQL(session, sql)));
    }

    @Override
    public Map<String, Object> getOneMapBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                getHibernateTemplate().execute(session -> this.generalDaoSupport.getOneMapBySQL(session, sql, args)));
    }

    /**
     * 用HQL语句查询的方法
     *
     * @param hql
     * @return
     */
    @Override
    public <T> List<T> getByHQL(final String hql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getByHQL(session, hql)));
    }

    @Override
    public <T> List<T> getByHQL(final String hql, final String... args) throws DataOperateException {
        return this.handleExceptionWithResult(() -> getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getByHQL(session, hql, args)));
    }

    @Override
    public int executeByHQL(String hql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.generalDaoSupport.executeByHQL(this.getCurrentSession(), hql));
    }

    @Override
    public int executeBySQL(String sql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.generalDaoSupport.executeBySQL(this.getCurrentSession(), sql));
    }

    @Override
    public int executeBySQL(String sql, Map<String, Object> params) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.generalDaoSupport.executeBySQL(this.getCurrentSession(), sql, params));
    }

    @Override
    public int executeBySQL(String sql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.generalDaoSupport.executeBySQL(this.getCurrentSession(), sql, args));
    }

    @Override
    public EnumConst getEnumConstByNamespaceCode(String namespace, String code) throws DataOperateException {
        return this.getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getEnumConstByNamespaceCode(session, namespace, code));

    }

    @Override
    public EnumConst getEnumConstByNamespaceAndName(String namespace, String name) throws DataOperateException {
        return this.getHibernateTemplate().execute(session ->
                this.generalDaoSupport.getEnumConstByNamespaceAndName(session, namespace, name));
    }

    @Override
    public int deleteByIds(Class clazz, List siteIds, List ids) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                this.generalDaoSupport.deleteByIds(this.getCurrentSession(), clazz, siteIds, ids));
    }

    @Override
    public <T extends AbstractBean> T getById(Class<T> entityClass, List siteIds, String id)
            throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                (T) getHibernateTemplate().execute(session ->
                        this.generalDaoSupport.getById(session, entityClass, siteIds, id)));
    }

    @Override
    public int batchExecuteSql(List<String> sqlList) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
            this.generalDaoSupport.batchExecuteSql(this.getCurrentSession(), sqlList));
    }

    @Override
    public int batchExecuteSql(List<String> sqlList, List<Map<String, Object>> paramList) throws DataOperateException {
        return this.handleExceptionWithResult(() ->
                this.generalDaoSupport.batchExecuteSql(this.getCurrentSession(), sqlList, paramList));
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

    @Override
    public <T> T getOneByHQL(String hql) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.getMyHibernateTemplate().execute(session ->
                this.generalDaoSupport.getOneByHQL(session, hql)));
    }

    @Override
    public <T> T getOneByHQL(String hql, Object... args) throws DataOperateException {
        return this.handleExceptionWithResult(() -> this.getMyHibernateTemplate().execute(session ->
                this.generalDaoSupport.getOneByHQL(session, hql, args)));
    }

    @Override
    public int saveAll(Collection<? extends AbstractBean> collection) throws DataOperateException {
        int count = 0;
        for (AbstractBean instance : collection) {
            this.save(instance);
            count++;
        }
        return count;
    }

    @Override
    public boolean checkNotEmpty(String sql, Object... args) throws DataOperateException {
        return CollectionUtils.isNotEmpty(this.getBySQL(sql, args));
    }

    @Override
    public void evictInstance(Object instance) {
        if (instance instanceof Collection) {
            this.getCurrentSession().evict(instance);
        } else {
            this.getCurrentSession().evict(instance);
        }
    }

    @Override
    public void clearAll() {
        this.getCurrentSession().clear();
    }

    @Override
    public void evict(Object target) {
        this.getCurrentSession().evict(target);
    }

    @Override
    public void flush() {
        this.getCurrentSession().flush();
    }

    @Override
    public void evictAll(Collection targets) {
        targets.forEach(e -> this.evict(e));
    }

    @Override
    public Session getHibernateSession() {
        return this.getMyHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    @Override
    public void deleteAll(Set<? extends AbstractBean> triggers) {
        triggers.forEach(this::delete);
    }

    /**
     * 获得当前线程session
     *
     * @return
     * @author weipengsen
     */
    public Session getCurrentSession() {
        return this.getMyHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    /**
     * 异常句柄处理
     * @param functional
     * @param <T>
     * @return
     */
    private <T> T handleExceptionWithResult(Supplier<T> functional) {
        try {
            return functional.get();
        } catch (RuntimeException e) {
            throw new DataOperateException(e);
        }
    }

    /**
     * 异常句柄处理
     * @param functional
     * @param <T>
     * @return
     */
    private void handleExceptionNoResult(Consumer<Session> functional, Session session) {
        try {
            functional.accept(session);
        } catch (RuntimeException e) {
            throw new DataOperateException(e);
        }
    }

}
