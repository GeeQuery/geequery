package com.github.geequery.core.cache;

import java.util.List;

import com.github.geequery.jsqlparser.statement.delete.Delete;
import com.github.geequery.jsqlparser.statement.insert.Insert;
import com.github.geequery.jsqlparser.statement.truncate.Truncate;
import com.github.geequery.jsqlparser.statement.update.Update;

@SuppressWarnings("rawtypes")
public final class CacheDummy implements Cache {

	static CacheDummy instance = new CacheDummy();

	public static CacheDummy getInstance() {
		return instance;
	}

	private CacheDummy() {
	}

	public boolean contains(Class cls, Object primaryKey) {
		return false;
	}

	public void evict(Class cls, Object primaryKey) {
	}

	public void evict(Class cls) {
	}

	public void evictAll() {
	}

	public void evict(CacheKey cacheKey) {
	}

	public <T> void onLoad(CacheKey sql, List<T> result, Class<T> clz) {
	}

	public void evict(Object cacheKey) {
	}

	public List load(CacheKey sql) {
		return null;
	}

	public void onInsert(Object obj, String table) {
	}

	public void onDelete(String table, String where, List<Object> bind) {
	}

	public void onUpdate(String table, String where, List<Object> bind) {
	}

	public boolean isDummy() {
		return true;
	}

	public void process(Truncate st, List<Object> list) {
		throw new UnsupportedOperationException();
	}

	public void process(Delete st, List<Object> list) {
		throw new UnsupportedOperationException();
	}

	public void process(Insert st, List<Object> list) {
		throw new UnsupportedOperationException();
	}

	public void process(Update st, List<Object> list) {
		throw new UnsupportedOperationException();
	}

	public long getHitCount() {
		return 0L;
	}

	public long getMissCount() {
		return 0L;
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return (T) this;
	}
}
