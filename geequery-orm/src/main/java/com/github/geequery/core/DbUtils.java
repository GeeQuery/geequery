package com.github.geequery.core;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

public class DbUtils {
	private DbUtils(){
	}

	/**
	 * 将异常包装为RuntimeException
	 * 
	 * @param e
	 * @return
	 */
	public static PersistenceException toRuntimeException(SQLException e) {
		String s = e.getSQLState();
		if (e instanceof SQLIntegrityConstraintViolationException) {
			return new EntityExistsException(e);
		} else if (e instanceof SQLTimeoutException) {
			return new QueryTimeoutException(s, e);
		}
		return new PersistenceException(s, e);
	}

	/**
	 * 将异常包装为Runtime异常
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException toRuntimeException(Throwable e) {
		while (true) {
			if (e instanceof RuntimeException) {
				return (RuntimeException) e;
			}
			if (e instanceof InvocationTargetException) {
				e = e.getCause();
				continue;
			}
			if (e instanceof Error) {
				throw (Error) e;
			}
			if (e instanceof SQLException) {
				return toRuntimeException((SQLException) e);
			}
			return new IllegalStateException(e);
		}
	}
}
