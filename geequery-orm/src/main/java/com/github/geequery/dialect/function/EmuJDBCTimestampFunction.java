package com.github.geequery.dialect.function;

import java.util.List;

import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.StringValue;
import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.tools.reflect.Enums;

public class EmuJDBCTimestampFunction extends BaseArgumentSqlFunction{
	private Func func;
	private DatabaseDialect parent;

	public EmuJDBCTimestampFunction(Func name,DatabaseDialect parent){
		this.func=name;
		this.parent=parent;
	}
	
	public String getName() {
		return func.name();
	}
	
	@Override
	public boolean needEscape() {
		return true;
	}

	public Expression renderExpression(List<Expression> arguments) {
		Function func=new Function(this.func.name(),arguments);
		func.setEscaped(true);
		String ex=arguments.get(0).toString().toUpperCase();
		if(ex.startsWith("SQL_TSI_")){
			ex=ex.substring(8);
		}
		SQL_TSI tsi=Enums.valueOf(SQL_TSI.class, ex, null);
		if(tsi==null){
			throw new IllegalArgumentException("Can not parse the argument: "+arguments.get(0)+" to a valid SQL_TSI value.");
		}
		arguments.set(0, tsi.get());
		if(this.func==Func.timestampadd){
			checkArg(arguments,2);
		}else if(this.func==Func.timestampdiff){
			checkArg(arguments,1);
			checkArg(arguments,2);
		}
		return func;
		
	}

	private void checkArg(List<Expression> arguments, int i) {
		Expression ex=arguments.get(i);
		if(ex instanceof StringValue){
			FunctionMapping mapping=parent.getFunctions().get("timestamp");
			if(mapping!=null){
				ex=mapping.rewrite("timestamp", ex);
				arguments.set(i, ex);
			}
		}
	}
}