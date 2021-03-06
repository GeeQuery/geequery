package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.SqlExpression;
import com.github.geequery.jsqlparser.visitor.Expression;

/**
 * 在Oracle上模拟 year day minute second等函数
 * @author jiyi
 *
 */
public class EmuOracleExtract extends BaseArgumentSqlFunction{
	private String name;
	private Expression fieldName;
	private boolean needTimestamp;
	public EmuOracleExtract(String name,boolean needTimestamp){
		this.name=name;
		this.fieldName=new SqlExpression(name);
		this.needTimestamp=needTimestamp;
	}
			
	public String getName() {
		return name;
	}

	public Expression renderExpression(List<Expression> arguments) {
		Expression ex=arguments.get(0);
		if(needTimestamp){
			ex=EmuOracleCastTimestamp.getInstance().convert(ex);
		}
		Function function=new Function("extract",fieldName,ex);
		function.getParameters().setBetween(" from ");
		return function;
	}
}
