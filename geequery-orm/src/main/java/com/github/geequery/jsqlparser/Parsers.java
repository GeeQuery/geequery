package com.github.geequery.jsqlparser;

import java.io.StringReader;
import java.util.List;

import javax.persistence.PersistenceException;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.core.DbUtils;
import com.github.geequery.jsqlparser.parser.JpqlParser;
import com.github.geequery.jsqlparser.parser.ParseException;
import com.github.geequery.jsqlparser.parser.StSqlParser;
import com.github.geequery.jsqlparser.parser.TokenMgrError;
import com.github.geequery.jsqlparser.statement.create.ColumnDefinition;
import com.github.geequery.jsqlparser.statement.create.CreateTable;
import com.github.geequery.jsqlparser.statement.select.OrderBy;
import com.github.geequery.jsqlparser.statement.select.Select;
import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.jsqlparser.visitor.SelectItem;
import com.github.geequery.jsqlparser.visitor.Statement;
import com.github.geequery.tools.StringUtils;

public class Parsers {
	/**
	 * 解析Select语句(原生SQL)
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static Select parseNativeSelect(String sql) throws ParseException {
		StSqlParser parser = new StSqlParser(new StringReader(sql));
		return parser.Select();
	}

	/**
	 * 解析表达式
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static Expression parseExpression(String sql) {
		try {
			JpqlParser parser = new JpqlParser(new StringReader(sql));
			return parser.SimpleExpression();
		} catch (ParseException e) {
			throw DbUtils.toRuntimeException(e);
		}
	}

	/**
	 * 解析select后的语句
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static List<SelectItem> parseSelectItems(String sql) throws ParseException {
		JpqlParser parser = new JpqlParser(new StringReader(sql));
		return parser.SelectItemsList();
	}

	public static ColumnDefinition parseColumnDef(String def) throws ParseException {
		String sql = StringUtils.concat("create table A (B ", def, ")");
		StSqlParser parser = new StSqlParser(new StringReader(sql));
		CreateTable ct = parser.CreateTable();
		return ct.getColumnDefinitions().get(0);
	}

	/**
	 * 解析select语句
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static Select parseSelect(String sql) throws ParseException {
		JpqlParser parser = new JpqlParser(new StringReader(sql));
		return parser.Select();
	}

	/**
	 * 解析OrderBy元素
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static OrderBy parseOrderBy(String sql) {
		StSqlParser parser = new StSqlParser(new StringReader("ORDER BY " + sql));
		try {
			return parser.OrderByElements();
		} catch (ParseException e) {
			throw new PersistenceException(sql, e);
		}
	}

	/**
	 * 解析二元表达式
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static Expression parseBinaryExpression(String sql) throws ParseException {
		JpqlParser parser = new JpqlParser(new StringReader(sql));
		return parser.Expression();
	}

	/**
	 * 解析语句
	 * 
	 * @param sql
	 * @return
	 * @throws ParseException
	 */
	public static Statement parseStatement(String sql) throws ParseException {
		JpqlParser parser = new JpqlParser(new StringReader(sql));
		try {
			return parser.Statement();
		} catch (ParseException e) {
			LogUtil.error("ErrorSQL:" + sql);
			throw e;
		} catch (TokenMgrError e) {
			LogUtil.error("ErrorSQL:" + sql);
			throw e;
		}
	}

}
