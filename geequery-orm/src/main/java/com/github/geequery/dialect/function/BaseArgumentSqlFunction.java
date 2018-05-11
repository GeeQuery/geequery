package com.github.geequery.dialect.function;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.github.geequery.jsqlparser.visitor.Expression;

public abstract class BaseArgumentSqlFunction implements SQLFunction {

	
	public boolean needEscape() {
		return false;
	}

	public boolean hasArguments() {
		return true;
	}

	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	public String[] requiresUserFunction() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
	protected void assertParam(List<Expression> arguments, int i) {
		if(i==0 && arguments==null){
			return;
		}
		if(arguments.size()!=i){
			throw new IllegalArgumentException("function "+ getName()+" must have "+ i+ " args, but current is "+arguments.size());
		}
		
	}
}
