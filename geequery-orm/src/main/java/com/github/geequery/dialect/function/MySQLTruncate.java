package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.LongValue;
import com.github.geequery.jsqlparser.visitor.Expression;

public class MySQLTruncate extends BaseArgumentSqlFunction{

	public String getName() {
		return "trunc";
	}

	public Expression renderExpression(List<Expression> arguments) {
		if(arguments.size()==1){
			return new Function("truncate",arguments.get(0),LongValue.L0);
		}else{
			return new Function("truncate",arguments);
		}
	}
	
	

}
