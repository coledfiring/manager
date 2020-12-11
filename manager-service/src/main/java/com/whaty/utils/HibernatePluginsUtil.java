package com.whaty.utils;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.hibernate.dao.impl.GeneralHibernateDao;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.reference.annotation.ValidateReference;
import com.whaty.util.CommonUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * hibernate工具类
 *
 * @author weipengsen
 */
public class HibernatePluginsUtil extends GeneralHibernateDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernatePluginsUtil.class);

    private static HibernatePluginsUtil hibernatePluginsUtil;

    private static Configuration hibernateConf;

    private static final Map<String, PersistentClass> ENTITY_CLASS_MAP = new HashMap<>(16);

    static {
        hibernateConf = ((LocalSessionFactoryBean) ContextLoader.getCurrentWebApplicationContext()
                .getBean("&sessionFactory")).getConfiguration();
        Iterator<PersistentClass> classIterator = hibernateConf.getClassMappings();
        while (classIterator.hasNext()) {
            PersistentClass currentClass = classIterator.next();
            ENTITY_CLASS_MAP.put(currentClass.getJpaEntityName(), currentClass);
        }
    }

    private HibernatePluginsUtil() {
        this.setSessionFactory((SessionFactory) ContextLoader.getCurrentWebApplicationContext()
                .getBean("sessionFactory"));
    }

    /**
     * 初始化hibernate工具
     *
     * @return
     */
    public static HibernatePluginsUtil initiation() {
        return hibernatePluginsUtil == null
                ? hibernatePluginsUtil = new HibernatePluginsUtil() : hibernatePluginsUtil;
    }

    /**
     * 获得Hibernate持久化类
     *
     * @param clazz
     * @return PersistentClass
     */
    private static PersistentClass getPersistentClass(Class clazz) {
        synchronized (HibernatePluginsUtil.class) {
            PersistentClass pc = hibernateConf.getClassMapping(clazz.getName());
            if (pc == null) {
                hibernateConf = hibernateConf.addClass(clazz);
                pc = hibernateConf.getClassMapping(clazz.getName());
            }
            return pc;
        }
    }

    /**
     * 通过实体类名获取hibernate持久化类
     * @param entityName
     * @return
     */
    public static PersistentClass getPersistentClass(String entityName) {
        synchronized (HibernatePluginsUtil.class) {
            return ENTITY_CLASS_MAP.get(entityName);
        }
    }

    /**
     * 获得表名
     *
     * @param clazz 映射到数据库的po类
     * @return String
     */
    public static String getTableName(Class clazz) {
        return getPersistentClass(clazz).getTable().getName();
    }

    /**
     * 使用当前session校验数据是否被引用
     *
     * @param clazz
     * @param ids
     * @return
     */
    public static boolean validateReferencingCurrentSession(Class clazz, String ids) {
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) HibernatePluginsUtil.initiation().getSessionFactory();
        Map<String, ClassMetadata> metaData = sessionFactory.getAllClassMetadata();
        Session session = sessionFactory.getCurrentSession();
        try {
            return validateReferencingCurrentSession(clazz, ids, metaData, session);
        } catch (ClassNotFoundException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * open一个新的session用于校验数据引用
     *
     * @param clazz
     * @param ids
     * @return
     */
    public static boolean validateReferencingOpenSession(Class clazz, String ids) {
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) HibernatePluginsUtil.initiation().getSessionFactory();
        Map<String, ClassMetadata> metaData = sessionFactory.getAllClassMetadata();
        Session session = sessionFactory.openSession();
        try {
            return validateReferencingCurrentSession(clazz, ids, metaData, session);
        } catch (ClassNotFoundException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 校验数据是否被引用
     *
     * @param clazz
     * @param ids
     * @param metaData
     * @param session
     * @return
     */
    private static boolean validateReferencingCurrentSession(Class clazz, String ids, Map<String,
            ClassMetadata> metaData, Session session) throws ClassNotFoundException {
        // 如果ids最后一个字符为“,”则去掉该逗号
        if (ids.trim().endsWith(CommonConstant.SPLIT_ID_SIGN)) {
            ids = ids.trim().substring(0, ids.length() - 1);
        }

        // 打开常量属性匹配
        boolean isEnumConst = false;
        String namespace = null;
        if (clazz.getName().equals(EnumConst.class.getName())) {
            isEnumConst = true;
            namespace = session
                    .createSQLQuery("select max(namespace) from enum_const where " + CommonUtils.madeSqlIn(ids, "id"))
                    .uniqueResult().toString();
        }

        List<String[]> tableColumnList = new ArrayList<>();
        for (Map.Entry<String, ClassMetadata> entry : metaData.entrySet()) {
            SingleTableEntityPersister step = (SingleTableEntityPersister) entry.getValue();
            if (!siteCanValidateReference(Class.forName(step.getEntityName()))) {
                continue;
            }
            for (String str : step.getPropertyNames()) {
                if (clazz.getName().equals(step.getPropertyType(str).getName())) {
                    if (isEnumConst && !str.equals("enumConstBy" + namespace)) {
                        continue;
                    }
                    tableColumnList.add(new String[]{step.getTableName(), step.getPropertyColumnNames(str)[0]});
                }
            }
        }
        if (tableColumnList.size() == 0) {
            return false;
        }
        StringBuilder sql = new StringBuilder("select  ");
        for (String[] strArr : tableColumnList) {
            sql.append(" (select count(1) from " + strArr[0] + " where "
                    + CommonUtils.madeSqlIn(ids, strArr[1]) + ")+");
        }
        sql.append("0");
        Object totalNum = session.createSQLQuery(sql.toString()).uniqueResult();
        return totalNum != null && Integer.valueOf(String.valueOf(totalNum)) > 0;
    }

    /**
     * 检查站点是否可以校验此类
     * @param clazz
     * @return
     */
    private static boolean siteCanValidateReference(Class<?> clazz) {
        ValidateReference validateReference = clazz.getAnnotation(ValidateReference.class);
        return validateReference == null || validateReference.validatePolicy()
                .checkCanValidate(validateReference.siteCodes(), SiteUtil.getSiteCode());
    }

    /**
     * 获得所有列名
     *
     * @param clazz 映射到数据库的po类
     * @return List<String> 列名
     */
    public static Map<String, String> getColumnNameMap(Class clazz) {
        PersistentClass pc = getPersistentClass(clazz);
        ///添加主键
        Property tmp = pc.getIdentifierProperty();
        Map<String, String> columns = new HashMap<>(16);
        Column col = (Column) ((tmp.getValue().getColumnIterator()).next());
        columns.put(tmp.getName(), col.getName());

        Iterator<Property> itr = pc.getUnjoinedPropertyIterator();
        while (itr.hasNext()) {
            try {
                tmp = itr.next();
                Iterator<Column> cos = tmp.getValue().getColumnIterator();
                if (cos != null && cos.hasNext()) {
                    col = (cos).next();
                    columns.put(tmp.getName(), col.getName());
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("获取" + clazz + "列失败", e);
                }
            }
        }
        return columns;
    }

    /**
     * 获得clazz对应表中的字段名
     *
     * @param clazz 实体类
     * @param name  属性名
     * @return 对应字段
     */
    public static String getColumnName(Class clazz, String name) {
        return ((Column) getPersistentClass(clazz).getProperty(name).getValue().getColumnIterator().next()).getName();
    }

}