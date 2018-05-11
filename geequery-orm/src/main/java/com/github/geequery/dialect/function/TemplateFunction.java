package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.TemplateExpression;
import com.github.geequery.jsqlparser.visitor.Expression;

/**
 * 支持用模板来表示函数的改写
 * @author jiyi
 *
 */
public class TemplateFunction extends BaseArgumentSqlFunction{
	private String template;
	private String name;
	public String getName() {
		return name;
	}
	
	public TemplateFunction(String name,String template){
		this.name=name;
		this.template=template;
	}

	public Expression renderExpression(List<Expression> arguments) {
		return new TemplateExpression(template, arguments.toArray(new Expression[arguments.size()]));
	}
}
