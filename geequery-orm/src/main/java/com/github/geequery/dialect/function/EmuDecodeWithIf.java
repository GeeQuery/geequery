package com.github.geequery.dialect.function;

import java.util.LinkedList;
import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.NullValue;
import com.github.geequery.jsqlparser.expression.operators.relational.EqualsTo;
import com.github.geequery.jsqlparser.visitor.Expression;

/**
 * 用 if(expr1, expr2, expr3)的样式来模拟Oracle的decode函数。
 * 该函数只在MySQL中支持
 * @author jiyi
 *
 */
public final class EmuDecodeWithIf extends BaseArgumentSqlFunction{

	public String getName() {
		return "decode";
	}

	public Expression renderExpression(List<Expression> arguments) {
		LinkedList<Expression> copy=new LinkedList<Expression>(arguments);
		Expression root=copy.removeFirst();
		Expression ifFunc=wrapWithIf(root,copy);
		return ifFunc;
	}

	private Expression wrapWithIf(Expression root, LinkedList<Expression> copy) {
		if(copy.size()==1){
			return copy.removeFirst();
		}else if(copy.isEmpty()){
			return NullValue.getInstance();
		}
		return new Function("if",toEqual(root,copy.removeFirst()),copy.removeFirst(),wrapWithIf(root,copy));
	}
	
	private Expression toEqual(Expression left, Expression right) {
		return new EqualsTo(left,right);
	}
}
