package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.TemplateExpression;
import com.github.geequery.jsqlparser.visitor.Expression;

public class EmuOracleCastTimestamp extends BaseArgumentSqlFunction{
	static EmuOracleCastTimestamp instance=new EmuOracleCastTimestamp();
	public static EmuOracleCastTimestamp getInstance(){
		return instance;
	}
	
	private EmuOracleCastTimestamp(){
	}
	public String getName() {
		return "timestamp";
	}

	public Expression renderExpression(List<Expression> arguments) {
		return convert(arguments.get(0));
	}
	
	public Expression convert(Expression ex){
		if(ex instanceof Function){
			String name=((Function) ex).getName();
			if("sysdate".equals(name)){
				ex=new Function("systimestamp");
			}else if("systimestamp".equals(name)){
			}
		}else{
			ex=new TemplateExpression("cast(%s as timestamp)",ex);
		}
		return ex;
	}

}
