package com.github.geequery.entity;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.geequery.accelerator.bean.BeanAccessor;
import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.dialect.type.ColumnMapping;
import com.github.geequery.tools.reflect.FieldAccessor;
import com.querydsl.core.types.EntityPath;

/**
 * 表的元模型
 * 
 * <h3>什么是元模型</h3> 元模型<meta-model>是一张数据库表的结构在ORM中的描述。<br>
 * 元模型是一个Java对象，这个对象中存放了数据库表的各种字段、类型等结构定义。 我们可以通过元模型来指定一张表和这张表对应的Java映射类型。
 * 
 * <h3>元模型的获得</h3> 一般来说，我们会使用一个类对应数据库中的一张表。比如类 jef.orm.test.Person对应数据库中的
 * PERSON表。 我们可以用以下方法来获得这个表的元模型。
 * 
 * <pre>
 * <tt>
 *  ITableMetadata metaModel=MetaHolder.getMeta(jef.orm.test.Person.class)
 *  </tt>
 * </pre>
 * 
 * @author Jiyi
 */
public interface EntityMetadata<T> {

	boolean enhanced();

	default BitSet getOrCreateTouch(T obj) {
		FieldAccessor accessor = getTouchRecord();
		BitSet result = (BitSet) accessor.getObject(obj);
		if (result == null) {
			result = new BitSet();
			accessor.set(obj, result);
		}
		return result;
	}

	default boolean isTouched(T obj, int index) {
		FieldAccessor accessor = getTouchRecord();
		BitSet result = (BitSet) accessor.getObject(obj);
		return result == null ? false : result.get(index);
	}

	FieldAccessor getTouchRecord();

	FieldAccessor getTouch();

	FieldAccessor getLazyAccessor();

	/**
	 * 得到此表对应的模型类。对于基本类型来说，模型类型和容器类型都是一致的。
	 * 
	 * @return 对应的模型类。
	 */
	Class<T> getThisType();

	/**
	 * 返回class名称
	 * 
	 * @return class名称
	 */
	String getName();

	/**
	 * 返回class名称Simple
	 * 
	 * @return class Simple名称
	 */
	String getSimpleName();

	/**
	 * 得到该对象绑定的数据源名（重定向后）<br>
	 * ORM允许用户使用Annotation @BindDataSource("db2") 添加在实体类上，指定该实体操作绑定特定的数据源。<br>
	 * 建模时的数据源，在实际运行环境中经过重定向后变为实际部署的数据源名称。<br>
	 * ORM会将对这张表的操作全部在这个数据源上执行。
	 * 
	 * @return 重定向后的数据源名称。
	 * @see jef.database.annotation.BindDataSource
	 */
	String getBindDsName();

	/**
	 * 得到schema名称。 ORM允许用户使用Annotation @Table(schema="S1")
	 * 添加在实体类上，指定该实体操作位于特定的schema上。<br>
	 * 建模时的schema，在实际运行环境中经过重定向后变为实际部署的数据源名称。<br>
	 * 
	 * @return 重定向后的schema
	 */
	String getSchema();

	/**
	 * 返回表名
	 * 
	 * @param withSchema
	 *            true要求带schema，schema是已经经过了重定向的名称
	 * @return 返回表名，如果实体绑定了schema，并且withSchema为true,那么返回形如
	 *         <code>schema.table</code>的名称
	 */
	String getTableName(boolean withSchema);

	/**
	 * 返回所有的元模型字段和类型。这些字段的顺序会进行调整，Clob和Blob将会被放在最后。 这些字段的顺序一旦确定那么就是固定的。
	 * 
	 * 注意当元数据未初始化完成前，不要调用这个方法。
	 * 
	 * @return
	 */
	Collection<ColumnMapping> getColumns();

	/**
	 * 获取字段的元数据定义
	 * 
	 * @return MappingType,包含了该字段的数据库列名、java字段名、类型等各种信息。
	 * @see ColumnMapping
	 */
	ColumnMapping getColumnDef(EntityPath<T> field);

	//
	// /**
	// * 返回所有自增字段的定义，如果没有则返回空数组
	// *
	// * @return 所有自增字段的定义
	// */
	// AutoIncrementMapping[] getAutoincrementDef();
	//
	// /**
	// * 返回第一个自增字段的定义，如果没有则返回null
	// *
	// * @return 返回第一个自增字段的定义
	// */
	// AutoIncrementMapping getFirstAutoincrementDef();
	//
	// /**
	// * 需要自动维护数据的列定义（每次更新时自动刷新的列，例如版本或修改时间）
	// *
	// * @return 需要自动维护记录更新的列定义
	// */
	// VersionSupportColumn[] getAutoUpdateColumnDef();
	//
	// /**
	// * 获得用于进行版本控制（防止并发修改冲突）的列定义
	// * @return the VersionSupportColumn with version usage
	// */
	// VersionSupportColumn getVersionColumn();

	/**
	 * 获取被设置为主键的字段
	 * 
	 * @return
	 */
	List<ColumnMapping> getPKFields();

	/**
	 * 获取索引的元数据定义
	 * 
	 * @return 索引的定义
	 */
	List<IndexDef> getIndexDefinition();

	/**
	 * 得到所有的unique约束
	 * 
	 * @return
	 */
	List<UniqueConstraintDef> getUniqueDefinitions();

	// ///////////////////////引用关联查询相关////////////////////
	/**
	 * 按照引用的关系获取所有关联字段
	 * 
	 * @return 关联关系字段
	 */
	// Map<Reference, List<AbstractRefField>> getRefFieldsByRef();

	/**
	 * 按照名称获得所有关联字段
	 * 
	 * @return 关联关系字段
	 */
	// Map<String, jef.database.meta.AbstractRefField> getRefFieldsByName();

	// //////////////////////附加功能////////////////////

	/**
	 * 根据名称得到一个Field对象（大小写不敏感） 如果找不到将返回null(不抛出异常)
	 * 
	 * @param name
	 * @return Field对象
	 */
	ColumnMapping findField(String left);

	/**
	 * 不考虑表别名的情况返回列名
	 * 
	 * @param field
	 *            field
	 * @param profile
	 *            当前数据库方言
	 * @return 数据库列名
	 */
	String getColumnName(EntityPath<T> field, DatabaseDialect profile, boolean escape);

	// /////////////////////////////// 反射与访问 ///////////////////////////

	/**
	 * 内部使用，得到Bean访问器
	 * 
	 * @return
	 */
	BeanAccessor getContainerAccessor();

	// ///////////////////////////// 其他行为 //////////////////////////////
	/**
	 * 返回所有的Lob字段
	 * 
	 * @return 所有Lob Field
	 */
	EntityPath<T> getLobFieldNames();

	// /////////////基于KV表扩展的设计//////////////

	boolean isCacheable();

	boolean isUseOuterJoin();

	Map<String, String> getColumnComments();
}
