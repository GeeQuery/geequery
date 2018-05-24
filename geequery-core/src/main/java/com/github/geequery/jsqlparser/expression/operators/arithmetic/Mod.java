package com.github.geequery.jsqlparser.expression.operators.arithmetic;

import com.github.geequery.jsqlparser.expression.BinaryExpression;
import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.jsqlparser.visitor.ExpressionType;
import com.github.geequery.jsqlparser.visitor.ExpressionVisitor;

/**
 * 求余数
 * @author jiyi
 *
 */
public class Mod  extends BinaryExpression {

	public Mod(){
	}
	public Mod(Expression left,Expression right){
		this.setLeftExpression(left);
		this.setRightExpression(right);
	}
	
	@Override
	protected void acceptExp(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

    public String getStringExpression() {
        return "%";
    }
    
	public ExpressionType getType0() {
		return ExpressionType.arithmetic;
	}
}
