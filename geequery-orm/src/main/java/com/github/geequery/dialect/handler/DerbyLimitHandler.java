package com.github.geequery.dialect.handler;

import java.util.Arrays;

import com.github.geequery.tools.PageLimit;
import com.querydsl.sql.SQLBindings;

public class DerbyLimitHandler implements LimitHandler {

	public com.querydsl.sql.SQLBindings toPageSQL(String sql, PageLimit range) {
		String limit; 
		if(range.getOffset()==0){
			limit=" fetch next "+range.getLimit()+" rows only";
		}else{
			limit=" offset "+range.getOffset()+" row fetch next "+range.getLimit()+" rows only";
		}
		sql = sql.concat(limit);
		return new SQLBindings(sql, Arrays.asList());
	}

	public SQLBindings toPageSQL(String sql, PageLimit range, boolean isUnion) {
		return toPageSQL(sql, range);
	}
}
