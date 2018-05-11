package com.github.geequery.helper;

import java.sql.SQLException;

import javax.persistence.PersistenceException;

import com.github.geequery.core.ref.ILazyLoadContext;
import com.github.geequery.entity.EntityMetadata;
import com.github.geequery.entity.MetaHolder;

/**
 * 提供若干方法，供在植入的增强代码中调用，从而简化增强逻辑
 * 
 * @author jiyi
 *
 */
public class EnhanceHelper {

	// Enhance, 植入一个BitSet对象

	private EnhanceHelper() {
	}

	public static <T> void stopRecord(T obj) {
		EntityMetadata<T> em = MetaHolder.getMetadata(obj);
		em.getTouch().set(obj, Boolean.FALSE);
	};

	public static <T> void startRecord(T obj) {
		EntityMetadata<T> em = MetaHolder.getMetadata(obj);
		em.getTouch().set(obj, Boolean.TRUE);
	}

	//用来标记对象中的某号字段被设置过值了
	public static <T> void touch(T obj, int i) {
		EntityMetadata<T> em = MetaHolder.getMetadata(obj);
		em.getOrCreateTouch(obj).set(i);
	}

	//用来标记某个延迟加载列被人工设置过值了。
	public final <T> void beforeSet(T obj, String fieldname) {
		EntityMetadata<T> em = MetaHolder.getMetadata(obj);
		ILazyLoadContext lazyload = (ILazyLoadContext) em.getLazyAccessor().getObject(obj);
		if (lazyload == null)
			return;
		lazyload.markProcessed(fieldname);
	}

	//触发并处理延迟加载的字段
	public final <T> void beforeGet(T obj, String fieldname) {
		EntityMetadata<T> em = MetaHolder.getMetadata(obj);
		ILazyLoadContext lazyload = (ILazyLoadContext) em.getLazyAccessor().getObject(obj);
		if (lazyload == null)
			return;
		int id = lazyload.needLoad(fieldname);
		if (id > -1) {
			try {
				if (lazyload.process(this, id)) {
					lazyload = null; // 清理掉，以后不再需要延迟加载
				}
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}
	}
}
