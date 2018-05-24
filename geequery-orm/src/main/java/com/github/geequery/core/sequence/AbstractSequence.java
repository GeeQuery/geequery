package com.github.geequery.core.sequence;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.PersistenceException;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.core.DbUtils;
import com.github.geequery.core.SessionFactory;
import com.github.geequery.core.config.DbCfg;
import com.github.geequery.dbmeta.DbMetaData;
import com.github.geequery.tools.JefConfiguration;
import com.github.geequery.tools.StringUtils;

/**
 * Sequence实现的抽象类
 * 
 * @author jiyi
 * 
 */
public abstract class AbstractSequence implements Sequence {
	private int cacheSize;
	protected final Queue<Long> cache = new ConcurrentLinkedQueue<Long>(); 

	// 需要初始化
	protected String dbKey;
	protected SessionFactory session;
	private final AtomicInteger tryInitCount=new AtomicInteger();
	private volatile boolean initSuccess;

	protected AbstractSequence(OperateTarget target,SequenceManager parent) {
		cacheSize = JefConfiguration.getInt(DbCfg.SEQUENCE_BATCH_SIZE, 50);
		if (cacheSize < 1)
			cacheSize = 1;
		if (target != null) {
			this.dbKey = target.getDbkey();
			this.session = target.getSession().getNoTransactionSession();
		}
	}
	
	protected synchronized void tryInit() {
		if(!initSuccess){
			tryInitCount.incrementAndGet();
			try{
				initSuccess=doInit(session,dbKey);
			}catch(SQLException e){
				LogUtil.warn("Sequence Implementation creating error.", e);
			}
		}
	}

	/**
	 * 执行数据库初始化
	 * @return
	 */
	protected abstract boolean doInit(SessionFactory session,String dbKey) throws SQLException;

	/**
	 * 将一个已经领出的键值从新塞回到Sequence中
	 * 
	 * @param key
	 */
	public void pushBack(long key) {
		cache.add(key);
	}

	public long next() {
		Long value = cache.poll();
		if (value != null){
			return value;
		}
		try {
			if(!initSuccess){
				if(tryInitCount.get()<3){
					tryInit();
					if(!initSuccess){
						throw new PersistenceException("Sequence/Table ["+this.getName()+"] createing failure.");	
					}
				}else{
					throw new PersistenceException("Sequence/Table ["+this.getName()+"] is not exist, and there will be no attemp to create it.");	
				}
			}
			return getFirstAndPushOthers(cacheSize, session, dbKey);
		} catch (SQLException e) {
			throw DbUtils.toRuntimeException(e);
		}
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * 
	 * @param size
	 *            要生成的ID数量
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected abstract long getFirstAndPushOthers(int size, SessionFactory client, String dbKey) throws SQLException;

	/**
	 * 将从from 到 value的值全部加入缓存(含头含尾)
	 * 
	 * @param from
	 * @param value
	 */
	protected void pushRange(long from, long value) {
		for (long i = from; i <= value; i++) {
			cache.offer(i);
		}
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		if (cacheSize < 1)
			cacheSize = 1;
		this.cacheSize = cacheSize;
	}

	protected long caclStartValue(DbMetaData meta, String schema, String table, String column, int initValue, long max) throws SQLException {
		long start = JefConfiguration.getInt(DbCfg.AUTO_SEQUENCE_OFFSET, -1);// 自动校准
		if (start == -1) {
			long calc = (StringUtils.isNotBlank(table) && StringUtils.isNotBlank(column)) ? meta.getSequenceStartValue(schema, table, column) : initValue;
			if (calc < max) {
				start = calc;
			}
		} else {
			start += initValue;
		}
		return start;
	}

	public boolean checkSequenceValue(String table, String column) throws SQLException {
		if (StringUtils.isBlank(table) || StringUtils.isBlank(column)) {
			throw new SQLException();
		}
		long maxInTable = session.getMetaData(dbKey).getSequenceStartValue(null, table, column) - 1;
		long next = this.next();
		pushBack(next);
		return maxInTable < next;
	}
}
