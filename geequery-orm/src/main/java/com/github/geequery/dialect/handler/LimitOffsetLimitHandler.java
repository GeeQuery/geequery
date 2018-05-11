package com.github.geequery.dialect.handler;

import java.util.Arrays;

import com.github.geequery.jsqlparser.ext.UnionJudgement;
import com.github.geequery.jsqlparser.ext.UnionJudgementDruidPGImpl;
import com.github.geequery.tools.PageLimit;
import com.github.geequery.tools.StringUtils;
import com.querydsl.sql.SQLBindings;

public class LimitOffsetLimitHandler implements LimitHandler {
	private UnionJudgement unionJudge;

	public LimitOffsetLimitHandler() {
		if (UnionJudgement.isDruid()) {
			unionJudge = new UnionJudgementDruidPGImpl();
		} else {
			unionJudge = UnionJudgement.DEFAULT;
		}
	}

	public SQLBindings toPageSQL(String sql, PageLimit range) {
		return toPageSQL(sql, range, unionJudge.isUnion(sql));

	}

	public SQLBindings toPageSQL(String sql, PageLimit range, boolean isUnion) {
		String limit;
		if (range.getOffset() == 0) {
			limit = " limit " + range.getLimit();
		} else {
			limit = " limit " + range.getLimit() + " offset " + range.getOffset();
		}
		return new SQLBindings(isUnion ? StringUtils.concat("select * from (", sql, ") tb__", limit) : sql.concat(limit), Arrays.asList());
	}
}
