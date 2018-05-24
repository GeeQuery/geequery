package jef.database;


import jef.database.Condition.Operator;
import jef.database.jsqlparser.visitor.Expression;
import jef.database.meta.Feature;
import jef.database.meta.ITableMetadata;
import jef.database.query.SqlContext;
import jef.database.wrapper.clause.SqlBuilder;
import jef.database.wrapper.variable.QueryLookupVariable;
import jef.database.wrapper.variable.ConstantVariable;
import jef.database.wrapper.variable.Variable;

import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.tools.StringUtils;

/**
 * 描述提供值修正的回调 实现
 */
public interface VariableConverter {
	Object process(Object result);
}
