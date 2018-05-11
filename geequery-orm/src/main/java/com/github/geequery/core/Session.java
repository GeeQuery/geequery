package com.github.geequery.core;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import com.github.geequery.core.cache.Cache;
import com.github.geequery.dialect.DatabaseDialect;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.sql.SQLQueryFactory;

/**
 * 实现QueryDSL的JPQLQueryFactory接口，因此可以直接当作JPAQueryFactory来使用。
 * @author jiyi
 *
 */
public interface Session extends JPQLQueryFactory{
	/**
	 * 获取指定数据库的方言，支持从不同数据源中获取<br/>
	 * 
	 * @param datasourceName
	 *            数据源名称，如果单数据源的场合，可以传入null
	 * @return 指定数据源的方言
	 */
	DatabaseDialect getProfile(String datasourceName);

	/**
	 * 关闭数据库事务。<br>
	 * <ul>
	 * <li>在DbClient上调用此方法，无任何影响。</li>
	 * <li>在Transaction上调用此方法，将会关闭事务，未被提交的修改将会回滚。</li>
	 * </ul>
	 */
	void close();

	/*
	 * 内部使用 得到缓存
	 */
	Cache getCache();

	/*
	 * 当前数据库操作所在的事务，用于记录日志以跟踪SQL查询所在事务
	 */
	String getTransactionId(String dbKey);//

	/**
	 * 清理一级缓存
	 * 
	 * @param entity
	 *            要清理的数据或查询
	 */
	default void evict(Object entity) {
		getCache().evict(entity);
	}

	/**
	 * 清空全部的一级缓存
	 */
	default void evictAll() {
		getCache().evictAll();
	}

	/**
	 * 判断Session是否有效
	 * 
	 * @return true if the session is open.
	 */
	abstract boolean isOpen();

	/**
	 * 创建命名查询
	 * 
	 * <h3>什么是命名查询</h3>
	 * 事先将E-SQL编写在配置文件或者数据库中，运行时加载并解析，使用时按名称进行调用。这类SQL查询被称为NamedQuery。对应JPA规范当中的
	 * “命名查询”。
	 * 
	 * <h3>使用示例</h3>
	 * 
	 * <pre>
	 * <code>NativeQuery&lt;ResultWrapper&gt; query = db.createNamedQuery("unionQuery-1", ResultWrapper.class);
	 *List<ResultWrapper> result = query.getResultList();
	 *
	 *配置在named-queries.xml中的SQL语句
	 *&lt;query name="unionQuery-1"&gt;
	 *&lt;![CDATA[
	 *select * from(
	 *(select upper(t1.person_name) AS name, T1.gender, '1' AS GRADE, T2.NAME AS SCHOOLNAME
	 *	from T_PERSON T1
	 *	inner join SCHOOL T2 ON T1.CURRENT_SCHOOL_ID=T2.ID)	 
	 *  union 
	 *(select t.NAME,t.GENDER,t.GRADE,'Unknown' AS SCHOOLNAME from STUDENT t)) a
	 *]]&gt;
	 *&lt;/query&gt;</code>
	 * 
	 * <pre>
	 * 即使用本方法返回的NativeQuery对象上，可以执行和该SQL语句相关的各种操作。
	 * 
	 * @param name
	 *            数据库中或者文件中配置的命名查询的名称
	 * @param resultWrapper
	 *            想要的查询结果包装类型
	 * @return 查询对象(NativeQuery)
	 * @see NativeQuery
	 */
	// <T> NativeQuery<T> createNamedQuery(String name, Class<T> resultWrapper);

	/**
	 * {@linkplain #createNamedQuery(String, Class) 什么是命名查询}
	 * 
	 * @param name
	 *            数据库中或者文件中配置的命名查询的名称
	 * @param resultMeta
	 *            想要的查询结果包装类型
	 * @return 查询对象(NativeQuery)
	 * @see NativeQuery
	 */
	// <T> NativeQuery<T> createNamedQuery(String name, ITableMetadata
	// resultMeta);

	/**
	 * 更新数据（带级联）<br>
	 * 如果和其他表具有关联的关系，那么插入时会自动维护其他表中的数据，这些操作包括了Delete操作（删除子表的部分数据）
	 * 
	 * @param obj
	 *            被更新的对象
	 * @return 更新的记录数 (仅主表，级联修改的记录行数未算在内。)
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 * @see {@link #update(IQueryableEntity)}
	 */
	int updateCascade(Object entity);

	/**
	 * 删除数据（带级联） <br>
	 * 如果和其他表具有关联的关系，那么插入时会自动维护其他表中的数据，这些操作包括了Delete操作
	 * 
	 * @param obj
	 *            删除请求的Entity对象
	 * @return 影响的记录数 (仅主表，级联修改的记录行数未算在内。)
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	int deleteCascade(Object entity);

	/**
	 * 合并记录——记录如果已经存在，则比较并更新；如果不存在则新增。（无级联操作）
	 * 
	 * @param entity
	 *            要合并的记录数据
	 * @return 如果插入返回对象本身，如果是更新则返回旧记录的值(如果插入，返回null;如果没修改，返回原对象;如果修改，返回旧对象。)
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> T merge(T entity);

	/**
	 * 合并记录——记录如果已经存在，则比较并更新；如果不存在则新增。（无级联操作）
	 * 
	 * @param entity
	 *            要合并的记录数据
	 * @param keys
	 *            业务主键字段名
	 * @return 如果插入返回对象本身，如果是更新则返回旧记录的值(如果插入，返回null;如果没修改，返回原对象;如果修改，返回旧对象。)
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> T merge(T entity, String[] keys);

	/**
	 * 合并记录——记录如果已经存在，则比较并更新；如果不存在则新增。（带级联操作）
	 * 
	 * @param entity
	 *            要合并的记录数据
	 * @return 如果插入返回对象本身，如果是更新则返回旧记录的值
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> T mergeCascade(T entity);

	/**
	 * 插入数据（带级联）<br>
	 * 如果和其他表具有1VS1、1VSN的关系，那么插入时会自动维护其他表中的数据。这些操作包括了Insert或者update.
	 * 
	 * @param obj
	 *            插入的对象
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	void insertCascade(Object obj);

	/**
	 * 插入数据（带级联）<br>
	 * 如果和其他表具有1VS1或1VSN的关系，那么插入时会自动维护其他表中的数据，这些操作包括了Insert或者update.
	 * 
	 * @param obj
	 *            插入的对象
	 * @param dynamic
	 *            dynamic模式：某些字段在数据库中设置了defauelt value。
	 *            如果在实体中为null，那么会将null值插入数据库，造成数据库的缺省值无效。 为了使用dynamic模式后，
	 *            只有手工设置为null的属性，插入数据库时才是null。如果没有设置过值，在插入数据库时将使用数据库的默认值。
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	void insertCascade(Object obj, boolean dynamic);

	/**
	 * 插入对象 <br>
	 * 不处理级联关系。如果要支持级联插入，请使用{@link #insertCascade(IQueryableEntity)}
	 * 
	 * @param obj
	 *            插入的对象。
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	void insert(Object obj);

	/**
	 * 插入对象。<br>
	 * 如果使用dynamic模式将会忽略掉没有set过的属性值
	 * 
	 * @param obj
	 *            插入的对象。
	 * @param dynamic
	 *            dynamic模式：某些字段在数据库中设置了defauelt value。
	 *            如果在实体中为null，那么会将null值插入数据库，造成数据库的缺省值无效。 为了使用dynamic模式后，
	 *            只有手工设置为null的属性，插入数据库时才是null。如果没有设置过值，在插入数据库时将使用数据库的默认值。
	 */
	void insert(Object obj, boolean dynamic);

	/**
	 * 按主键删除<strong>单条</strong>对象
	 * 
	 * @param clz
	 *            实体类型
	 * @param keys
	 *            主键的值。<br>
	 *            (注意，可变参数不是用于传入多行记录的值，而是用于传入单条记录的复合主键， 要批量删除多条请用
	 *            {@linkplain #batchDelete}方法)
	 * @return 删除记录条数
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	int delete(Class<?> entityClass, Serializable... keys);

	/**
	 * 按指定字段的值删除对象。<br>
	 * 如果要按该字段批量删除对象，请使用 {@link #batchDeleteByField(Path<T>, List) }方法。
	 * 
	 * 
	 * @param field
	 *            作为删除条件的字段
	 * @param value
	 *            删除条件值
	 * @return 删除的行数
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> int deleteByField(EntityPath<T> field, Object value);

	/**
	 * 删除对象
	 * 
	 * @param obj
	 *            删除请求
	 * @return 影响的记录数
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	int delete(Object entity);

	/**
	 * 更新对象，无级联操作
	 * 
	 * @param obj
	 *            被更新的对象
	 * @return 影响的记录行数
	 * @throws SQLException
	 * @see {@link #updateCascade(IQueryableEntity)}
	 */
	int update(Object obj);

	/**
	 * 查出单个对象。如果结果不唯一抛出NonUniqueResultException。
	 * 
	 * @param obj
	 *            查询条件
	 * @return 使用传入的对象进行查询，结果返回记录的第一条。如未查到返回null。。
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 * @throws NonUniqueResultException
	 *             结果不唯一
	 */
	<T> T load(T obj);

	/**
	 * 查出单个对象
	 * 
	 * @param obj
	 *            查询条件
	 * @param unique
	 *            true要求查询结果必须唯一。false允许结果不唯一，但仅取第一条。
	 * @return 使用传入的对象进行查询，结果返回记录的第一条。如未查到返回null
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 * @throws NonUniqueResultException
	 *             当unique为true且查询结果不唯一时。
	 */
	<T> T load(T obj, boolean unique);

	/**
	 * 根据样例查找
	 * 
	 * @param entity
	 *            查询条件
	 * @param property
	 *            作为查询条件的字段名。当不指定properties时，首先检查entity当中是否设置了主键，如果有主键按主键删除，
	 *            否则按所有非空字段作为匹配条件。
	 * @return 查询结果
	 */
	<T> List<T> selectByExample(T entity, String... propertyName);

	/**
	 * 根据字段查询
	 * 
	 * @param meta
	 *            表元数据
	 * @param propertyName
	 *            字段名
	 * @param value
	 *            字段值
	 * @return 查询结果
	 * @throws SQLException
	 */
	<T> List<T> selectByField(BeanPath<T> meta, String propertyName, Object value);

	/**
	 * 按指定的字段的值加载记录<br>
	 * 如果要根据该字段的值批量加载记录，可使用 {@link #batchLoadByField(Path<T>, List) }方法。
	 * 
	 * @param field
	 *            作为查询条件的字段
	 * @param values
	 *            要查询的值
	 * @return 符合条件的记录
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> List<T> selectByField(Path<T> field, Object value);

	/**
	 * 按指定的字段的值加载记录<br>
	 * 如果查询结果不唯一，抛出NonUniqueResultException异常
	 * 
	 * @param field
	 *            作为查询条件的字段
	 * @param value
	 *            要查询的值
	 * @return 符合条件的记录
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 * @throws NonUniqueResultException
	 *             结果不唯一
	 */
	<T> T loadByField(Path<T> field, Object value);

	/**
	 * 按主键获取一条记录。注意这里的可变参数是为了支持复合主键，并不是加载多条记录。<br>
	 * 如需加载多条记录，请用 {@link #batchLoad(Class, List) } 方法
	 * 
	 * @param meta
	 *            元数据
	 * @param keys
	 *            主键的值。
	 * @return 查询结果
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> T load(BeanPath<T> meta, Serializable... keys);

	/**
	 * 按主键加载多条记录。适用与拥有大量主键值，需要在数据库中查询与之对应的记录时。<br>
	 * 查询会使用IN条件来减少操作数据库的次数。如果要查询的条件超过了500个，会自动分多次进行查询。
	 * <p>
	 * <strong>注意：在多库操作下，这一方法不支持对每条记录单独分组并计算路由。</strong>
	 * <strong>注意：此方法不支持复合主键</strong>
	 * 
	 * @param clz
	 *            实体类
	 * @param pkValues
	 *            主键的值(多值)
	 * @return 查询结果
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> List<T> batchLoad(BeanPath<T> clz, List<? extends Serializable> pkValues);

	/**
	 * 按主键删除多条记录。适用与拥有大量主键值，需要在数据库中查询与之对应的记录时。<br>
	 * 会使用IN条件来减少操作数据库的次数。如果要删除的条件超过了500个，会自动分多次进行删除。
	 * 
	 * <p>
	 * <strong>注意1：在多库操作下，这一方法不支持对每条记录单独分组并计算路由。</strong><br>
	 * 需要路由的场景下请使用 {@link #batchDelete(List, boolean)}方法
	 * <p>
	 * <strong>注意2：不支持复合主键</strong><br>
	 * 需要复合主键的场景下请使用 {@link #batchDelete(List, boolean)}方法
	 * 
	 * @param clz
	 *            要删除的记录类型
	 * @param keys
	 *            主键列表。复合主键不支持。如需批量删除复合主键的类请用{@link #batchDelete(List, boolean)}
	 * @throws SQLException
	 *             如果数据库操作错误，抛出。
	 */
	<T> int batchDelete(BeanPath<T> clz, List<? extends Serializable> keys);

	/**
	 * 执行批量插入操作。
	 * 
	 * @param entities
	 *            要插入的对象
	 * @param group
	 *            是否对传入的对象按所属表重新分组。<br>
	 *            在启用分库分表后，用户如果不确定传入的多个对象在路由计算后属于同一张表，则需打开此开关。<br>
	 *            开关开启后会对每个对象进行路由计算并重新分组操作（这一操作将损耗一定的性能）。
	 * @throws SQLException
	 */
	<T> void batchInsert(List<T> entities, Boolean group);
	
	/**
	 * 获取QueryDSL的SqlQueryFactory对象
	 * @param datasourceName
	 * @return
	 */
	SQLQueryFactory sqlFactory(String datasourceName);
}
