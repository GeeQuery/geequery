package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.LongValue;
import com.github.geequery.jsqlparser.expression.SqlExpression;
import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.tools.StringUtils;

public class EmuSQLServerTrunc extends BaseArgumentSqlFunction{
	private SqlExpression INTEGER=new SqlExpression("integer");
	
	private SqlExpression FLOAT=new SqlExpression("float");
	
	@Override
	public String getName() {
		return "trunc";
	}

	@Override
	public Expression renderExpression(List<Expression> arguments) {
		Expression input=arguments.get(0);
		Expression digital=null;
		if(arguments.size()>1){
			digital=arguments.get(1);
		}else{
			digital=LongValue.L0;
		}
		int numeric=-1;
		String numricStr=digital.toString();//截断后保留的位数
		if(StringUtils.isNumeric(numricStr)){
			numeric=Integer.parseInt(numricStr);
		}
		Function roundFunc=new Function("round",input,digital,LongValue.L1);
		if(numeric==0){
			return new Function("convert",INTEGER, roundFunc);			
		}else{
			return new Function("convert",FLOAT, roundFunc);
		}
	}

}

