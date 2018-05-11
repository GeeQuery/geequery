package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.SqlExpression;
import com.github.geequery.jsqlparser.visitor.Expression;

/**
 * 在postgres上模拟year, month, day, hour, minute, second六个函数
 * @author jiyi
 *
 */
public class EmuPostgresExtract extends BaseArgumentSqlFunction{
	private String name;
	private Expression fieldName;
	
	public EmuPostgresExtract(String name){
		this(name,name);
	}
			
	public EmuPostgresExtract(String name,String fieldString){
		this.name=name;
		this.fieldName=new SqlExpression(fieldString);
	}

	public String getName() {
		return name;
	}

	public Expression renderExpression(List<Expression> arguments) {
		Function function=new Function("extract",fieldName,arguments.get(0));
		function.getParameters().setBetween(" from ");
		return function;
	}
}
