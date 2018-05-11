package com.github.geequery.core;

import java.io.Serializable;
import java.util.List;

import com.github.geequery.core.cache.Cache;
import com.github.geequery.dialect.DatabaseDialect;
import com.querydsl.core.Tuple;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.sql.SQLQueryFactory;

public class SessionImpl implements Session {
	@Override
	public DatabaseDialect getProfile(String datasourceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cache getCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTransactionId(String dbKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int updateCascade(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteCascade(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T merge(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T merge(T entity, String[] keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T mergeCascade(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertCascade(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertCascade(Object obj, boolean dynamic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(Object obj, boolean dynamic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int delete(Class<?> entityClass, Serializable... keys) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int deleteByField(EntityPath<T> field, Object value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T load(T obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T load(T obj, boolean unique) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> selectByExample(T entity, String... propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> selectByField(BeanPath<T> meta, String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> selectByField(Path<T> field, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T loadByField(Path<T> field, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T load(BeanPath<T> meta, Serializable... keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> batchLoad(BeanPath<T> clz, List<? extends Serializable> pkValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> int batchDelete(BeanPath<T> clz, List<? extends Serializable> keys) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> void batchInsert(List<T> entities, Boolean group) {
		// TODO Auto-generated method stub
		
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

	@Override
	public JPQLQuery<?> query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLQueryFactory sqlFactory(String datasourceName) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
