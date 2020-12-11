package com.whaty.dao;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.exception.DataOperateException;
import com.whaty.util.CommonUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用持久层
 * @author weipengsen
 */
public interface GeneralDao {

	/**
	 * 保存
	 * @param transientInstance
	 * @return
	 * @throws DataOperateException
	 */
	<T extends AbstractBean> T save(T transientInstance) throws DataOperateException;

    /**
     * 根据id列表删除
     * @param clazz 所需删除的表
     * @param ids
     * @return
	 * @throws DataOperateException
     */
	<T extends AbstractBean> int deleteByIds(Class<T> clazz, List ids) throws DataOperateException;

    /**
     * 删除单个
     * @param persistentInstance
	 * @throws DataOperateException
     */
	<T extends AbstractBean> void delete(T persistentInstance) throws DataOperateException;

	/**
	 * 根据id查找数据
	 * @param clazz
	 * @param id
	 * @return
	 * @throws DataOperateException
	 */
	<T extends AbstractBean> T getById(Class<T> clazz, String id) throws DataOperateException;

    /**
     * 根据样例
     * @param instance
     * @return
	 * @throws DataOperateException
     */
	<T extends AbstractBean> List<T> getByExample(T instance) throws DataOperateException;

    /**
     * 根据条件查找
     * @param detachedCriteria
     * @return
	 * @throws DataOperateException
     */
	<T> List<T> getList(DetachedCriteria detachedCriteria) throws DataOperateException;

 	/**
 	 * 批量更新一个字段
 	 * @param ids
	 * @param clazz
 	 * @param column
 	 * @param value
 	 * @return
	 * @throws DataOperateException
 	 */
 	int updateColumnByIds(List ids, Class<? extends AbstractBean> clazz,
						  String column, String value) throws DataOperateException;

 	/**
 	 * 使用sql查询的接口
 	 * @param sql
 	 * @return
	 * @throws DataOperateException
 	 */
	<T> List<T> getBySQL(String sql) throws DataOperateException;

	/**
	 * 使用sql查询的接口
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataOperateException
	 */
	<T> List<T> getBySQL(String sql, Object... args) throws DataOperateException;

	/**
	 * 根据sql查询数据并赋予sql指定的参数
	 * @param sql
	 * @param parametersMap
	 * @return
	 */
	<T> List<T> getBySQLWithParameters(String sql, Map<String, Object> parametersMap);

    /**
	 * 拿到map格式的数据
	 * @param sql
	 * @return
	 * @throws DataOperateException
	 */
	List<Map<String, Object>> getMapBySQL(String sql) throws DataOperateException;

	/**
	 * 拿到map格式的数据
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataOperateException
	 */
	List<Map<String, Object>> getMapBySQL(String sql, Object... args) throws DataOperateException;

	/**
	 * 通过sql获取一条数据
	 * @param sql
	 * @param args
	 * @return
	 */
	<T> T getOneBySQL(String sql, Object... args);

	/**
	 * 使用sql获取一条map数据
	 * @param sql
	 * @return
	 */
	Map<String, Object> getOneMapBySQL(String sql);

	/**
	 * 使用sql获取一条map数据
	 * @param sql
	 * @return
	 */
	Map<String, Object> getOneMapBySQL(String sql, Object... args);

	/**
	 * 根据hql查询数据
	 * @param hql
	 * @return
	 * @throws DataOperateException
	 */
 	<T> List<T> getByHQL(String hql) throws DataOperateException;

	/**
	 * 根据hql和传参查询数据
	 * @param hql
	 * @param args
	 * @return
	 * @throws DataOperateException
	 */
 	<T> List<T> getByHQL(String hql, String... args) throws DataOperateException;

	/**
	 * 执行hql
	 * @param hql
	 * @return
	 * @throws DataOperateException
	 */
 	int executeByHQL(String hql) throws DataOperateException;

	/**
	 * 执行sql
	 * @param sql
	 * @return
	 * @throws DataOperateException
	 */
 	int executeBySQL(String sql) throws DataOperateException;

	/**
	 * 执行sql
	 * @param sql
	 * @param params
	 * @return
	 * @throws DataOperateException
	 */
	int executeBySQL(String sql, Map<String, Object> params) throws DataOperateException;

	/**
	 * 执行sql
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataOperateException
	 */
	int executeBySQL(String sql, Object... args) throws DataOperateException;

	/**
	 * 根据namespace和code查询常量
	 * @param namespace
	 * @param code
	 * @return
	 * @throws DataOperateException
	 */
 	EnumConst getEnumConstByNamespaceCode(String namespace, String code) throws DataOperateException;

	/**
	 * 根据namespace和name查询常量
	 * @param namespace
	 * @param name
	 * @return
	 * @throws DataOperateException
	 */
 	EnumConst getEnumConstByNamespaceAndName(String namespace, String name) throws DataOperateException;

	/**
	 * 获得hibernateTemplate
	 * @return
	 * @throws DataOperateException
	 */
 	HibernateTemplate getMyHibernateTemplate();

	/**
	 * 通过id删除数据
	 * @param clazz
	 * @param siteIds
	 * @param ids
	 * @return
	 * @throws DataOperateException
	 */
	<T extends AbstractBean> int deleteByIds(Class<T> clazz, List siteIds, List ids) throws DataOperateException;

	/**
	 * 根据id查询数据
	 * @param entityClass
	 * @param siteIds
	 * @param id
	 * @return
	 * @throws DataOperateException
	 */
	<T extends AbstractBean> T getById(Class<T> entityClass, List siteIds, String id) throws DataOperateException;

	/**
	 * 批量执行数据
	 * @param sqlList
	 * @return
	 * @throws DataOperateException
	 */
	int batchExecuteSql(List<String> sqlList) throws DataOperateException;

	/**
	 * 批量执行数据
	 * @param sqlList
	 * @param params
	 * @return
	 * @throws DataOperateException
	 */
	int batchExecuteSql(List<String> sqlList, List<Map<String, Object>> params) throws DataOperateException;

	/**
	 * 根据表名、填充字段数组、填充数据生成直接插入值的insert语句并执行
	 * 其中字段数组和填充数据的key必须一致
	 * 数据每一千分成两个sql执行
	 * @param tableName
	 * @param columnArray
	 * @param values
	 * @return
	 */
	int batchExecuteInsertSql(String tableName, String[] columnArray, List<Map<String, Object>> values);

	/**
	 * 通过HQL获得一个实体类对象
	 * @param hql
	 * @return
	 * @author weipengsen
	 * @throws DataOperateException
	 */
	<T> T getOneByHQL(String hql) throws DataOperateException;

	/**
	 * 通过HQL获得一个实体类对象
	 * @param hql
	 * @param args
	 * @return
	 * @author weipengsen
	 * @throws DataOperateException
	 */
	<T> T getOneByHQL(String hql, Object... args) throws DataOperateException;

	/**
	 * 存储集合中的实体类
	 * @param collection
	 * @return
	 * @throws DataOperateException
	 */
	int saveAll(Collection<? extends AbstractBean> collection) throws DataOperateException;

	/**
	 * 检查sql查询出的数据不为空
	 * @return
	 * @throws DataOperateException
	 */
	boolean checkNotEmpty(String sql, Object... args) throws DataOperateException;

	/**
	 * 解除对象游离态
	 * @param instance
	 * @return
	 */
	void evictInstance(Object instance);

	/**
	 * 将当前session所有的游离态对象都变为游离态
	 */
    void clearAll();

	/**
	 * 将指定对象切除游离态
	 * @param target
	 */
	void evict(Object target);

	/**
	 * 刷新session缓存
	 */
	void flush();

	/**
	 * 解除集合中所有的游离态
	 * @param targets
	 */
	void evictAll(Collection targets);

	/**
	 * 生成insert sql
	 * @param tableName
	 * @param columnArray
	 * @param maps
	 * @return
	 */
	default String generateInsertSql(String tableName, String[] columnArray, List<Map<String, Object>> maps) {
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(tableName);
		sql.append("(");
		Arrays.stream(columnArray).forEach(e -> sql.append(e).append(","));
		sql.delete(sql.lastIndexOf(","), sql.length());
		sql.append(") values");
		maps.forEach(e -> {
			sql.append("(");
			Arrays.stream(columnArray).forEach(c -> {
				if (e.get(c) == null) {
					sql.append("null");
				} else if (e.get(c) instanceof Number) {
					sql.append(e.get(c));
				} else if (e.get(c) instanceof Date) {
					sql.append("STR_TO_DATE('" + CommonUtils.changeDateToString((Date) e.get(c)) + "', '%Y-%m-%d')");
				} else {
					sql.append("'").append(e.get(c)).append("'");
				}
				sql.append(",");
			});
			sql.delete(sql.lastIndexOf(","), sql.length());
			sql.append("),");
		});
		sql.delete(sql.lastIndexOf(","), sql.length());
		return sql.toString();
	}

	/**
	 * 获取会话
	 * @return
	 */
    Session getHibernateSession();

	/**
	 * 删除所有实体类对象
	 * @param triggers
	 */
	void deleteAll(Set<? extends AbstractBean> triggers);
}
