package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Interval;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Addition;
import com.github.geequery.jsqlparser.visitor.Expression;

public class EmuPostgresAddDate extends BaseArgumentSqlFunction{
	public boolean hasArguments() {
		return true;
	}

	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	public String getName() {
		return "adddate";
	}

	public Expression renderExpression(List<Expression> arguments) {
		Expression adjust=arguments.get(1);
		if(adjust instanceof Interval){
			Interval interval=(Interval)adjust;
			interval.toPostgresMode();
		}
		return new Addition(arguments.get(0), adjust);
	}
}
