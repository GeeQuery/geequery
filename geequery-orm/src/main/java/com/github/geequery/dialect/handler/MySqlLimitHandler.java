package com.github.geequery.dialect.handler;

import java.util.Arrays;

import com.github.geequery.jsqlparser.ext.UnionJudgement;
import com.github.geequery.jsqlparser.ext.UnionJudgementDruidMySQLImpl;
import com.github.geequery.tools.PageLimit;
import com.github.geequery.tools.StringUtils;
import com.querydsl.sql.SQLBindings;

public class MySqlLimitHandler implements LimitHandler {
	private final static String MYSQL_PAGE = " limit %start%,%next%";
	private UnionJudgement unionJudge;

	public MySqlLimitHandler() {
		if(UnionJudgement.isDruid()){
			unionJudge=new UnionJudgementDruidMySQLImpl();
		}else{
			unionJudge=UnionJudgement.DEFAULT;
		}
	}

	public SQLBindings toPageSQL(String sql, PageLimit range) {
		return toPageSQL(sql, range,unionJudge.isUnion(sql));
	}

	@Override
	public SQLBindings toPageSQL(String sql, PageLimit range, boolean isUnion) {
		String[] s = new String[] { Long.toString(range.getOffset()), Integer.toString(range.getLimit()) };
		String limit = StringUtils.replaceEach(MYSQL_PAGE, new String[] { "%start%", "%next%" }, s);
		return new SQLBindings(isUnion ? StringUtils.concat("select * from (", sql, ") tb__", limit) : sql.concat(limit), Arrays.asList());
	}
}
