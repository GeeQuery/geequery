package com.github.geequery.dialect.function;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.visitor.Expression;

public interface FunctionDialect {
	public Expression rewrite(String name, Expression... args);

	public void rewrite(Function function);

	public SQLFunction getFunction();

	public DbFunction getStardard();

	public int getArgCount();
	
	public int getMatch();
}
