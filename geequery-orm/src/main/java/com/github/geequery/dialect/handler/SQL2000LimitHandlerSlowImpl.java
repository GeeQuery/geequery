package com.github.geequery.dialect.handler;

import java.util.Arrays;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;

import com.github.geequery.jsqlparser.Parsers;
import com.github.geequery.jsqlparser.parser.ParseException;
import com.github.geequery.jsqlparser.statement.select.OrderBy;
import com.github.geequery.jsqlparser.statement.select.PlainSelect;
import com.github.geequery.jsqlparser.statement.select.Select;
import com.github.geequery.jsqlparser.statement.select.SubSelect;
import com.github.geequery.jsqlparser.statement.select.Top;
import com.github.geequery.jsqlparser.statement.select.Union;
import com.github.geequery.tools.PageLimit;
import com.querydsl.sql.SQLBindings;

public class SQL2000LimitHandlerSlowImpl implements LimitHandler {
	public SQLBindings toPageSQL(String sql, PageLimit offsetLimit) {
		long offset=offsetLimit.getOffset();
		if(offset==0){//没有offset可以简化处理
			int indexDistinct=StringUtils.indexOfIgnoreCase(sql, "select distinct");
			int index=StringUtils.indexOfIgnoreCase(sql, "select");
			return new SQLBindings(new StringBuilder( sql.length() + 8 )
			.append(sql).insert(index + (indexDistinct == index ? 15 : 6), " top " + offsetLimit.getLimit() ).toString(), Arrays.asList());
		}
		return processToPageSQL(sql,offsetLimit);
	}

	protected SQLBindings processToPageSQL(String sql, PageLimit offsetLimit) {
		try {
			Select select = Parsers.parseNativeSelect(sql);
			if(select.getSelectBody() instanceof PlainSelect){
				return toPage(offsetLimit,(PlainSelect)select.getSelectBody(),sql);
			}else{
				return toPage(offsetLimit,(Union)select.getSelectBody(),sql);
			}
		} catch (ParseException e) {
			throw new PersistenceException(e);
		}
	}

	private SQLBindings toPage(PageLimit offsetLimit, Union union,String raw) {
		OrderBy order=union.getOrderBy();
		if(order==null){
			order=union.getLastPlainSelect().getOrderBy();
			if(order!=null){
				//解析器问题，会把属于整个union的排序条件当做是属于最后一个查询的
				union.getLastPlainSelect().setOrderBy(null);
				union.setOrderBy(order);
			}else{
				throw new UnsupportedOperationException("Select must have order to page");
			}
		}
		StringBuilder sb=new StringBuilder(raw.length()+40);
		sb.append("select top ");
		sb.append(offsetLimit.getEnd()).append(" * from");
		SubSelect s=new SubSelect();
		union.setOrderBy(null);
		s.setSelectBody(union);
		s.setAlias("__ef_tmp1");
		s.appendTo(sb);
		sb.append('\n');
		order.appendTo(sb);
//		sb.append(") __ef_tmp2\n");
//		order.reverseAppendTo(sb,"__ef_tmp2",null);
		return new BindSql(sb.toString()).setReverseResult(new ResultSetLaterProcess(offsetLimit.getOffset()));
	}

	private SQLBindings toPage(PageLimit offsetLimit, PlainSelect selectBody,String raw) {
		OrderBy order=selectBody.getOrderBy();
		if(order==null){
			throw new UnsupportedOperationException("Select must have order to page");
		}
		selectBody.setTop(new Top(offsetLimit.getEnd()));
		
		StringBuilder sb=new StringBuilder(raw.length()+30);
//		sb.append("select top ").append(offsetLimit[1]).append(" * from (");
		selectBody.appendTo(sb);
//		sb.append(") __ef_t");
//		order.reverseAppendTo(sb,"__ef_t",selectBody.getSelectItems());
		return new SQLBindings(sb.toString(), Arrays.asList()).setReverseResult(new ResultSetLaterProcess(offsetLimit.getOffset()));
	}


	@Override
	public SQLBindings toPageSQL(String sql, PageLimit offsetLimit, boolean isUnion) {
		return toPageSQL(sql, offsetLimit);
	}

}
