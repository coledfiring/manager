package com.whaty.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

import java.sql.Types;

/**
 * 重写MySQL5Dialect类，注册Types.LONGVARCHAR
 * @author zqf
 *
 */
public class MyDialect extends MySQL5Dialect {

	public MyDialect(){
		super();
		registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());
		registerHibernateType(Types.LONGVARCHAR,Hibernate.STRING.getName());
		registerHibernateType(Types.BINARY,Hibernate.STRING.getName());
		registerHibernateType(-1, Hibernate.STRING.getName());
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
		//mysql convert函数支持
		registerFunction("convert", new SQLFunctionTemplate(Hibernate.STRING, "convert(?1 using ?2)"));
	}

}
