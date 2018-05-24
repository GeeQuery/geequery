package com.github.geequery.core;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.dialect.DbProperty;
import com.github.geequery.tools.string.CharUtils;

public final class DbUtils {

	private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

	private DbUtils() {
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

	/**
	 * 如果列名或表名碰到了数据库的关键字，那么就要增加引号一类字符进行转义
	 * 
	 * @param profile
	 * @param name
	 * @return
	 */
	public static final String escapeColumn(DatabaseDialect profile, String name) {
		if (name == null)
			return name;
		String w = profile.getProperty(DbProperty.WRAP_FOR_KEYWORD);
		if (w != null && profile.containKeyword(name)) {
			StringBuilder sb = new StringBuilder(name.length() + 2);
			sb.append(w.charAt(0)).append(name).append(w.charAt(1));
			return sb.toString();
		}
		return name;
	}

	/**
	 * 将带下划线的名称，转为不带下划线的名称<br>
	 * 例如： you_are_boy -> YouAreBoy
	 * 
	 * @param name
	 *            待转的名称
	 * @param capitalize
	 *            首字母是否要大写
	 * @return 转换后的名称
	 */
	public static String underlineToUpper(String name, boolean capitalize) {
		char[] r = new char[name.length()];
		int n = 0;
		boolean nextUpper = capitalize;
		for (char c : name.toCharArray()) {
			if (c == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					r[n] = Character.toUpperCase(c);
					nextUpper = false;
				} else {
					r[n] = Character.toLowerCase(c);
				}

				n++;
			}
		}
		return new String(r, 0, n);
	}

	/**
	 * 将带大小写的名称，转换为全小写但带下划线的名称 例如: iLoveYou -> i_love_you
	 * 
	 * @param name
	 *            待转换为名称
	 * @return 转换后的名称
	 */
	public static String upperToUnderline(String name) {
		if (name == null)
			return null;
		boolean skipUpper = true;
		StringBuilder sb = new StringBuilder();
		char[] chars = name.toCharArray();
		sb.append(chars[0]);
		for (int i = 1; i < chars.length; i++) {
			if (CharUtils.isUpperAlpha(chars[i])) {
				if (!skipUpper) {
					sb.append('_').append(chars[i]);
					skipUpper = true;
				} else {
					if (i + 2 < chars.length && CharUtils.isLowerAlpha(chars[i + 1])) {
						sb.append('_').append(chars[i]);
					} else {
						sb.append(chars[i]);
					}
				}
			} else {
				sb.append(chars[i]);
				skipUpper = false;
			}
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Close the given JDBC Connection and ignore any thrown exception. This is
	 * useful for typical finally blocks in manual JDBC code.
	 * 
	 * @param con
	 *            the JDBC Connection to close (may be {@code null})
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				logger.error("Could not close JDBC Connection", ex);
			} catch (Throwable ex) {
				logger.error("Unexpected exception on closing JDBC Connection", ex);
			}
		}
	}

	/**
	 * 安静的关闭结果集
	 * 
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// Do nothing
			}
		}
	}

	/**
	 * 关闭指定的Statement
	 * 
	 * @param st
	 */
	public static void close(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			// Do nothing
		}
	}

}
