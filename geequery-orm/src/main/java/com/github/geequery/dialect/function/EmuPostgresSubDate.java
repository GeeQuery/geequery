package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Interval;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Subtraction;
import com.github.geequery.jsqlparser.visitor.Expression;

public class EmuPostgresSubDate extends BaseArgumentSqlFunction{
	public String getName() {
		return "subdate";
	}

	public Expression renderExpression(List<Expression> arguments) {
		Expression adjust=arguments.get(1);
		if(adjust instanceof Interval){
			Interval interval=(Interval)adjust;
			interval.toPostgresMode();
		}
		return new Subtraction(arguments.get(0), adjust);
	}
}
