package com.github.geequery.dialect.handler;

import java.util.Arrays;

import com.github.geequery.tools.PageLimit;
import com.querydsl.sql.SQLBindings;

public class OracleLimitHander implements LimitHandler {

	private final static String ORACLE_PAGE1 = "select * from (select tb__.*, rownum rid__ from (";
	private final static String ORACLE_PAGE2 = " ) tb__ where rownum <= %end%) where rid__ > %start%";

	public SQLBindings toPageSQL(String sql, PageLimit range) {
		if(range.getOffset()==0){
			return new SQLBindings("select tb__.* from (\n"+sql+") tb__ where rownum <= "+range.getLimit(),Arrays.asList());
		}else{
			sql = ORACLE_PAGE1 + sql;
			String limit = ORACLE_PAGE2.replace("%start%", String.valueOf(range.getOffset()));
			limit = limit.replace("%end%", String.valueOf(range.getEnd()));
			sql = sql.concat(limit);
			return new SQLBindings(sql,Arrays.asList());	
		}
		
	}


	@Override
	public SQLBindings toPageSQL(String sql, PageLimit offsetLimit, boolean isUnion) {
		return toPageSQL(sql, offsetLimit);
	}

}
