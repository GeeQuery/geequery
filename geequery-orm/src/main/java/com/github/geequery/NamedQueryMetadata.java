package com.github.geequery;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import jef.database.jsqlparser.JPQLConvert;
import jef.database.jsqlparser.JPQLSelectConvert;
import jef.database.jsqlparser.SelectToCountWrapper;
import jef.database.jsqlparser.SqlFunctionlocalization;
import jef.database.query.ParameterProvider;

import com.github.geequery.common.Entry;
import com.github.geequery.core.DbUtils;
import com.github.geequery.core.DialectCase;
import com.github.geequery.core.OperateTarget;
import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.dialect.Feature;
import com.github.geequery.entity.MetaHolder;
import com.github.geequery.jsqlparser.RemovedDelayProcess;
import com.github.geequery.jsqlparser.expression.JpqlDataType;
import com.github.geequery.jsqlparser.expression.JpqlParameter;
import com.github.geequery.jsqlparser.expression.SqlExpression;
import com.github.geequery.jsqlparser.expression.Table;
import com.github.geequery.jsqlparser.expression.operators.relational.Between;
import com.github.geequery.jsqlparser.expression.operators.relational.EqualsTo;
import com.github.geequery.jsqlparser.expression.operators.relational.ExpressionList;
import com.github.geequery.jsqlparser.expression.operators.relational.GreaterThan;
import com.github.geequery.jsqlparser.expression.operators.relational.GreaterThanEquals;
import com.github.geequery.jsqlparser.expression.operators.relational.InExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.LikeExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.MinorThan;
import com.github.geequery.jsqlparser.expression.operators.relational.MinorThanEquals;
import com.github.geequery.jsqlparser.expression.operators.relational.NotEqualsTo;
import com.github.geequery.jsqlparser.parser.ParseException;
import com.github.geequery.jsqlparser.statement.delete.Delete;
import com.github.geequery.jsqlparser.statement.insert.Insert;
import com.github.geequery.jsqlparser.statement.select.Limit;
import com.github.geequery.jsqlparser.statement.select.PlainSelect;
import com.github.geequery.jsqlparser.statement.select.Select;
import com.github.geequery.jsqlparser.statement.select.Union;
import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.jsqlparser.visitor.SelectBody;
import com.github.geequery.jsqlparser.visitor.Statement;
import com.github.geequery.jsqlparser.visitor.VisitorAdapter;
import com.github.geequery.springdata.repository.support.Update;
import com.github.geequery.tools.ArrayUtils;
import com.github.geequery.tools.StringUtils;

public class NamedQueryMetadata {

	private Map<DatabaseDialect, DialectCase> datas = new IdentityHashMap<DatabaseDialect, DialectCase>();;
	
	private static final class DialectCase {
		Statement statement;
		jef.database.jsqlparser.statement.select.Select count;
		Map<Object, ParameterMetadata> params;
		RemovedDelayProcess delays;
		public Limit countLimit;
	}

	public String toString() {
		if (this.datas.isEmpty()) {
			return rawsql;
		} else {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<DatabaseDialect, DialectCase> e : datas.entrySet()) {
				DialectCase dc = e.getValue();
				sb.append(e.getKey().getName()).append(":");
				sb.append(dc.statement).append("\n");
			}
			return sb.toString();
		}
	}
	
	static final class ParameterMetadata {
		JpqlParameter param;
		Object parent;
		boolean escape;

		ParameterMetadata(DatabaseDialect dialect, JpqlParameter p, Object parent) {
			this.param = p;
			this.parent = parent;
			if (parent instanceof LikeExpression) {
				if (dialect.has(Feature.NOT_SUPPORT_LIKE_ESCAPE)) {
					escape = false;
					((LikeExpression) parent).setEscape(null);
				} else {
					if (p.getDataType() != null && p.getDataType().ordinal() > 9) {
						((LikeExpression) parent).setEscape("/");
						escape = true;
					}
				}
			}
		}

		final JpqlDataType getDataType() {
			return param.getDataType();
		}
	}

	/*
	 * 解析SQL语句，改写
	 */
	private static DialectCase analy(String sql, int type, OperateTarget db) throws SQLException {
		final DatabaseDialect dialect = db.getProfile();
		try {
			Statement st = DbUtils.parseStatement(sql);
			final Map<Object, ParameterMetadata> params = new HashMap<Object, ParameterMetadata>();
			// Schema重定向处理：将SQL语句中的schema替换为映射后的schema
			st.accept(new VisitorAdapter() {
				@Override
				public void visit(JpqlParameter param) {
					params.put(param.getKey(), new ParameterMetadata(dialect, param, visitPath.getFirst()));
				}

				@Override
				public void visit(Table table) {
					String schema = table.getSchemaName();
					if (schema != null) {
						String newSchema = MetaHolder.getMappingSchema(schema);
						if (newSchema != schema) {
							table.setSchemaName(newSchema);
						}
					}
					if (dialect.containKeyword(table.getName())) {
						table.setName(DbUtils.escapeColumn(dialect, table.getName()));
					}
				}

				@Override
				public void visit(jef.database.jsqlparser.expression.Column c) {
					String schema = c.getSchema();
					if (schema != null) {
						String newSchema = MetaHolder.getMappingSchema(schema);
						if (newSchema != schema) {
							c.setSchema(newSchema);
						}
					}
					if (dialect.containKeyword(c.getColumnName())) {
						c.setColumnName(DbUtils.escapeColumn(dialect, c.getColumnName()));
					}
				}

			});
			// 进行本地语言转化
			SqlFunctionlocalization localization = new SqlFunctionlocalization(dialect, db);
			st.accept(localization);

			if (type == TYPE_JPQL) {
				if (st instanceof Select) {
					st.accept(new JPQLSelectConvert(dialect));
				} else if (st instanceof Insert) {
					st.accept(new JPQLConvert(dialect));
				} else if (st instanceof Update) {
					st.accept(new JPQLConvert(dialect));
				} else if (st instanceof Delete) {
					st.accept(new JPQLConvert(dialect));
				}

			}

			DialectCase result = new DialectCase();
			result.statement = st;
			result.params = params;
			if (localization.delayLimit != null || localization.delayStartWith != null) {
				result.delays = new RemovedDelayProcess(localization.delayLimit, localization.delayStartWith);
			}
			return result;
		} catch (ParseException e) {
			String message = e.getMessage();
			int n = message.indexOf("Was expecting");
			if (n > -1) {
				message = message.substring(0, n);
			}
			throw new SQLException(StringUtils.concat("Parse error:", sql, "\n", message));
		}
	}
	
	/**
	 * 获得SQL语句中所有的参数和定义
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public Map<Object, ParameterMetadata> getParams(OperateTarget db) {
		DialectCase dc;
		try {
			dc = getDialectCase(db);
		} catch (SQLException e) {
			throw DbUtils.toRuntimeException(e);
		}
		return dc.params;
	}

	/**
	 * 得到SQL和绑定参数
	 * 
	 * @param db
	 * @param prov
	 * @return 要执行的语句和绑定变量列表
	 * @throws SQLException
	 */
	public SqlAndParameter getSqlAndParams(OperateTarget db, ParameterProvider prov) throws SQLException {
		DialectCase dc = getDialectCase(db);
		SqlAndParameter result = applyParam(dc.statement, prov);
		result.setInMemoryClause(dc.delays);
		return result;
	}

	private DialectCase getDialectCase(OperateTarget db) throws SQLException {
		DatabaseDialect profile = db.getProfile();
		if (datas == null) {
			// 当使用testNamedQueryConfigedInDb案例时，由于使用Unsafe方式构造对象，故构造器方法未运行造成datas为null;
			datas = new IdentityHashMap<DatabaseDialect, DialectCase>();
		}
		DialectCase dc = datas.get(profile);
		if (dc == null) {
			synchronized (datas) {
				if ((dc = datas.get(profile)) == null) {
					dc = analy(this.rawsql, this.type, db);
					datas.put(profile, dc);
				}
			}
		}
		return dc;
	}

	/**
	 * 得到修改后的count语句和绑定参数 注意只有select语句能修改成count语句
	 * 
	 * @param db
	 * @param prov
	 * @return
	 * @throws SQLException
	 */
	public SqlAndParameter getCountSqlAndParams(OperateTarget db, ParameterProvider prov) throws SQLException {
		DialectCase dc = getDialectCase(db);
		if (dc.count == null) {
			if (dc.statement instanceof jef.database.jsqlparser.statement.select.Select) {
				SelectBody oldBody = ((jef.database.jsqlparser.statement.select.Select) dc.statement).getSelectBody();
				SelectToCountWrapper body = null;
				if (oldBody instanceof PlainSelect) {
					body = new SelectToCountWrapper((PlainSelect) oldBody, db.getProfile());
				} else if (oldBody instanceof Union) {
					body = new SelectToCountWrapper((Union) oldBody);
				}
				if (body == null) {
					throw new SQLException("Can not generate count SQL statement for " + dc.statement.getClass().getName());
				}
				jef.database.jsqlparser.statement.select.Select ctst = new jef.database.jsqlparser.statement.select.Select();
				ctst.setSelectBody(body);
				dc.count = ctst;
				if (dc.delays != null && dc.delays.limit != null) {
					dc.countLimit = dc.delays.limit;
				} else {
					dc.countLimit = body.getRemovedLimit();
				}
			} else {
				throw new IllegalArgumentException();
			}
		}
		SqlAndParameter result = applyParam(dc.count, prov);
		result.setInMemoryClause(dc.delays);
		result.setLimit(dc.countLimit);
		return result;
	}

	private final static class ParamApplier extends VisitorAdapter {
		private ParameterProvider prov;
		private List<Object> params;

		public ParamApplier(ParameterProvider prov, List<Object> params) {
			this.prov = prov;
			this.params = params;
		}

		// 进行绑定变量匹配
		@Override
		public void visit(JpqlParameter param) {
			Object value = null;
			boolean contains;
			if (param.isIndexParam()) {
				value = prov.getIndexedParam(param.getIndex());
				contains = prov.containsParam(param.getIndex());
			} else {
				value = prov.getNamedParam(param.getName());
				contains = prov.containsParam(param.getName());
			}

			if (value instanceof SqlExpression) {
				param.setResolved(((SqlExpression) value).getText());
			} else if (value != null) {
				if (value.getClass().isArray()) {
					int size = Array.getLength(value);
					if (value.getClass().getComponentType().isPrimitive()) {
						value = ArrayUtils.toObject(value);
					}
					for (Object v : (Object[]) value) {
						params.add(v);
					}
					param.setResolved(size);
				} else if (value instanceof Collection) {
					int size = ((Collection<?>) value).size();
					for (Object v : (Collection<?>) value) {
						params.add(v);
					}
					param.setResolved(size);
				} else {
					params.add(value);
					param.setResolved(0);
				}
			} else if (contains) {
				params.add(value);
				param.setResolved(0);
			} else {
				param.setNotUsed();
			}
		}

		@Override
		public void visit(NotEqualsTo notEqualsTo) {
			super.visit(notEqualsTo);
			notEqualsTo.checkEmpty();
		}

		@Override
		public void visit(InExpression inExpression) {
			super.visit(inExpression);
			inExpression.setEmpty(Boolean.FALSE);
			if (inExpression.getItemsList() instanceof ExpressionList) {
				ExpressionList list0 = (ExpressionList) inExpression.getItemsList();
				List<Expression> list = list0.getExpressions();
				if (list.size() == 1 && (list.get(0) instanceof JpqlParameter)) {
					JpqlParameter p = (JpqlParameter) list.get(0);
					if (p.resolvedCount() == -1) {
						inExpression.setEmpty(Boolean.TRUE);
					}
				}
			}
		}

		@Override
		public void visit(Between between) {
			super.visit(between);
			if (between.getBetweenExpressionStart() instanceof JpqlParameter) {
				JpqlParameter p = (JpqlParameter) between.getBetweenExpressionStart();
				if (p.resolvedCount() == -1) {
					between.setEmpty(Boolean.TRUE);
					return;
				}
			}
			if (between.getBetweenExpressionEnd() instanceof JpqlParameter) {
				JpqlParameter p = (JpqlParameter) between.getBetweenExpressionEnd();
				if (p.resolvedCount() == -1) {
					between.setEmpty(Boolean.TRUE);
					return;
				}
			}
			between.setEmpty(Boolean.FALSE);
		}

		@Override
		public void visit(EqualsTo equalsTo) {
			super.visit(equalsTo);
			equalsTo.checkEmpty();
		}

		@Override
		public void visit(MinorThan minorThan) {
			super.visit(minorThan);
			minorThan.checkEmpty();
		}

		@Override
		public void visit(MinorThanEquals minorThanEquals) {
			super.visit(minorThanEquals);
			minorThanEquals.checkEmpty();
		}

		@Override
		public void visit(GreaterThan greaterThan) {
			super.visit(greaterThan);
			greaterThan.checkEmpty();
		}

		@Override
		public void visit(GreaterThanEquals greaterThanEquals) {
			super.visit(greaterThanEquals);
			greaterThanEquals.checkEmpty();
		}

		@Override
		public void visit(LikeExpression likeExpression) {
			super.visit(likeExpression);
			likeExpression.checkEmpty();
		}
	}

	/**
	 * 在指定的SQL表达式中应用参数
	 * 
	 * @param ex
	 * @param prov
	 * @return
	 */
	public static Entry<String, List<Object>> applyParam(Expression ex, MapProvider prov) {
		final List<Object> params = new ArrayList<Object>();
		ex.accept(new ParamApplier(prov, params));
		return new Entry<String, List<Object>>(ex.toString(), params);
	}

	/*
	 * 返回应用参数后的查询
	 */
	public static SqlAndParameter applyParam(Statement st, final ParameterProvider prov) {
		final List<Object> params = new ArrayList<Object>();
		st.accept(new ParamApplier(prov, params));
		return new SqlAndParameter(st, params, prov);
	}
}
