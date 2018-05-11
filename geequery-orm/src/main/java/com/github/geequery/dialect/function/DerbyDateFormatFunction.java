package com.github.geequery.dialect.function;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Concat;
import com.github.geequery.jsqlparser.expression.operators.relational.ExpressionList;
import com.github.geequery.jsqlparser.visitor.Expression;

//TODO Derby居然是没有format date函数的，本来想自己模拟一个。
public class DerbyDateFormatFunction extends BaseArgumentSqlFunction {
	public boolean hasArguments() {
		return false;
	}
	public boolean hasParenthesesIfNoArguments() {
		return true;
	}
	public String getName() {
		return "dateToString";
	}

	
	public Expression renderExpression(List<Expression> arguments){
		String format=arguments.get(1).toString();
		if(format.charAt(0)=='\''){
			format=StringUtils.substringBetween(format, "'", "'");
		}
		format=format.toLowerCase();
		
		
		Function function=new Function("year");
		function.setParameters(new ExpressionList(arguments.get(0)));
		Concat c=new Concat();
		c.setLeftExpression(function);
		
		function =new Function("month");
		function.setParameters(new ExpressionList(arguments.get(0)));
		c.setRightExpression(function);
		return c;
	}

}
