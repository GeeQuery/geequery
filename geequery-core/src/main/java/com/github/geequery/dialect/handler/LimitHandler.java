package com.github.geequery.dialect.handler;

import com.github.geequery.tools.PageLimit;
import com.querydsl.sql.SQLBindings;

/**
 * LimitHandler用于进行结果集限制。offset,limit的控制
 * 
 * @author jiyi
 *
 */
public interface LimitHandler {
	/**
	 * 对于SQL语句进行结果集限制
	 * 
	 * @param sql
	 * @param offsetLimit
	 * @return
	 */
	SQLBindings toPageSQL(String sql, PageLimit offsetLimit);

	/**
	 * 对于SQL语句进行结果集限制
	 * 
	 * @param sql
	 * @param offsetLimit
	 * @param isUnion
	 * @return
	 */
	SQLBindings toPageSQL(String sql, PageLimit offsetLimit, boolean isUnion);
}
