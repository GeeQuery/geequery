package com.github.geequery.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.github.geequery.annotation.Comment;
import com.github.geequery.annotation.EasyEntity;

/**
 * 描述一个命名查询的配置.
 * 
 * <h3>什么是命名查询</h3>
 * 命名查询即Named-Query,在Hibernate和JPA中都有相关的功能定义。简单来说，命名查询就是将查询语句(SQL,HQL,JPQL等)
 * 事先编写好， 然后为其指定一个名称。<br>
 * 在使用ORM框架时，取出事先解析好的查询，向其中填入绑定变量的参数，形成完整的查询。
 * 
 * <h3>EF-ORM的命名查询和上述两种框架定义有什么不同</h3> EF-ORM也支持命名查询，机制和上述框架相似，具体有以下的不同。
 * <ul>
 * <li>命名查询默认定义在配置文件 named-queries.xml中。不支持使用Annotation等方法定义</li>
 * <li>命名查询也可以定义在数据库表中，数据库表的名称可由用户配置</li>
 * <li>命名查询可以支持 {@linkplain com.github.geequery.core.NativeQuery E-SQL}
 * 和JPQL两种语法（后者特性未全部实现,不推荐）</li>
 * <li>由于支持E-SQL，命名查询可以实现动态SQL语句的功能，配置XML的配置功能，比较近似与IBatis的操作方式</li>
 * </ul>
 * 
 * <h3>使用示例</h3> 在named-queries.xml中配置
 * 
 * <pre>
 * <tt>&lt;query name = "testIn" type="sql" fetch-size="100" &gt;
 *  	&lt;![CDATA[
 *  		   select * from person_table where id in (:names&lt;int&gt;)
 *  	]]&gt;
 * &lt;/query&gt;</tt>
 * </pre>
 * 
 * 上例中,:names就是一个绑定变量占位符。实际使用方式如下：
 * 
 * <pre>
 * <tt>  ...
 *    Session session=getSession();
 *    NativeQuery&lt;Person&gt; query=session.createNamedQuery("testIn",Person.class);
 *    query.setParam("names",new String[]{"张三","李四","王五"});
 *    List&lt;Person&gt; persons=query.getResultList();   //相当于执行了 select * from person_table where id in ('张三','李四','王五') 
 *   ...
 * </tt>
 * </pre>
 * 
 * @author jiyi
 * @see com.github.geequery.core.NativeQuery
 * 
 */
@EasyEntity(checkEnhanced = false)
@Entity
@javax.persistence.Table(name = "NAMED_QUERIES")
@Comment("GeeQuery Named Queries")
public class NamedQueryConfig {
	public static final int TYPE_SQL = 0;
	public static final int TYPE_JPQL = 1;

	@Id
	@Column(name = "NAME")
	private String name;

	@Column(name = "SQL_TEXT", length = 4000)
	private String rawsql;

	/**
	 * 设置该命名查询的类型，是SQL，还是JPQL(TYPE_JPQL/TYPE_SQL)
	 */
	@Column(name = "TYPE", precision = 1)
	private int type;
	/**
	 * 标记
	 */
	@Column(name = "TAG")
	private String tag;

	/**
	 * 备注信息
	 */
	@Column(name = "REMARK")
	private String remark;

	/**
	 * fetchSize of Result.
	 */
	@Column(name = "FETCH_SIZE", precision = 6)
	private int fetchSize;

	private boolean fromDb = false;

	public boolean isFromDb() {
		return fromDb;
	}

	public void setFromDb(boolean fromDb) {
		this.fromDb = fromDb;
	}

	public NamedQueryConfig() {
	};

	public NamedQueryConfig(String name, String sql, boolean isJpql, int fetchSize) {
		this.rawsql = sql;
		this.name = name;
		this.fetchSize = fetchSize;
		this.type = isJpql ? TYPE_JPQL : TYPE_SQL;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRawsql() {
		return rawsql;
	}

	public void setRawsql(String rawsql) {
		this.rawsql = rawsql;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
}
