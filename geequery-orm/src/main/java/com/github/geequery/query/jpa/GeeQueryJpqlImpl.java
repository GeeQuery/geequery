package com.github.geequery.query.jpa;

import java.sql.Connection;

import com.querydsl.core.Tuple;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;

public class GeeQueryJpqlImpl implements JPQLQueryFactory {

	private final javax.inject.Provider<Connection> connectionProvider;

	GeeQueryJpqlImpl(javax.inject.Provider<Connection> provider) {
		this.connectionProvider = provider;
	}

	@Override
	public JPQLQuery<?> query() {
		return null;
	}

	@Override
	public DeleteClause<?> delete(EntityPath<?> path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> JPQLQuery<T> select(Expression<T> expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<Tuple> select(Expression<?>... exprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> JPQLQuery<T> selectDistinct(Expression<T> expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<Tuple> selectDistinct(Expression<?>... exprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<Integer> selectOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<Integer> selectZero() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> JPQLQuery<T> selectFrom(EntityPath<T> from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<?> from(EntityPath<?> from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPQLQuery<?> from(EntityPath<?>... from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateClause<?> update(EntityPath<?> path) {
		// TODO Auto-generated method stub
		return null;
	}

}
