package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.Parenthesis;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Subtraction;
import com.github.geequery.jsqlparser.visitor.Expression;

/**
 * 截取oracle纯time
 * @author jiyi
 *
 */
public class EmuOracleTime extends BaseArgumentSqlFunction{

	public String getName() {
		return "time";
	}

	public Expression renderExpression(List<Expression> arguments) {
		Expression ex=arguments.get(0);
		ex=EmuOracleCastTimestamp.getInstance().convert(ex);
		Expression result=new Subtraction(ex,new Function("trunc",arguments.get(0)));
		result=new Parenthesis(result);
		return result;
	}
}
