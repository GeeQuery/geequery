package com.github.geequery.dialect.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.geequery.jsqlparser.expression.CaseExpression;
import com.github.geequery.jsqlparser.expression.WhenClause;
import com.github.geequery.jsqlparser.expression.operators.relational.EqualsTo;
import com.github.geequery.jsqlparser.visitor.Expression;


/**
 * 用 case expr1
 *  when expr2 then expr3
 *  when ... then.
 *  else ...
 *  end 来模拟oracle的decode函数
 * @author jiyi
 *
 */
public class EmuDecodeWithDerbyCase extends BaseArgumentSqlFunction{
	public String getName() {
		return "decode";
	}

	public Expression renderExpression(List<Expression> arguments) {
		LinkedList<Expression> copy=new LinkedList<Expression>(arguments);
		CaseExpression result=new CaseExpression();
		Expression switchExpr=copy.removeFirst();
		List<WhenClause> whens=new ArrayList<WhenClause>();
		while(copy.size()>1){
			WhenClause when=new WhenClause();
			when.setWhenExpression(new EqualsTo(switchExpr,copy.removeFirst()));
			when.setThenExpression(copy.removeFirst());
			whens.add(when);
		}
		result.setWhenClauses(whens);
		if(!copy.isEmpty()){
			result.setElseExpression(copy.removeFirst());
		}
		return result;
	}
}
