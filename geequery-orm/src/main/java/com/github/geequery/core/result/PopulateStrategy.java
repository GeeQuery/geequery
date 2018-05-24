package com.github.geequery.core.result;

/**
 * <h3>枚举对象，描述查询结果拼装到对象的策略</h3>
 * <ul>
 * <li>{@link #PLAIN_MODE}<br>
 * 强制使用PLAIN_MODE.(即使是dataobject子类，也使用Plain模式拼装)</li>
 * <li>{@link #SKIP_COLUMN_ANNOTATION}<br>
 * 拼装时，忽略Column名，直接使用类的Field名作为列名/li>
 * <li>{@link #NO_RESORT}<br>
 * 禁用内存重新排序功能</li>
 * </ul>
 */
public enum PopulateStrategy {
	/**
	 * 忽略@Column注释。<br/>
	 * 
	 * 一个名为 createTime 的字段，其注解为@Column(name="CREATE_TIME")。<br>
	 * 正常情况下，标记为的字段会对应查询结果中的"CREATE_TIME"列。<br>
	 * 使用SKIP_COLUMN_ANNOTATION参数后，对应到查询结果中的createtime列。
	 * 
	 */
	SKIP_COLUMN_ANNOTATION,
	/**
	 * <b>关于PLAIN_MODE的用法</b>
	 * 正常情况下，检测到查询结果是DataObject的子类时，就会采用嵌套法(NESTED)拼装结果，比如
	 * 这种情况下将结果集作为立体结构，将不同表选出的字段作为不同的实体的属性。如:<br>
	 * <li>Person.name <-> 'T1__name'</li> <li>Person.school.id <-> 'T2__id'</li>
	 * <p>
	 * 嵌套法拼装时，会忽略没有绑定到数据库表的字段的拼装（即非数据元模型中定义的字段）。
	 * 如果我们要将结果集中的列直接按名称对应到实体上，那么就需要使用PLAIN_MODE.
	 */
	PLAIN_MODE,
	/**
	 * 当返回结果是数组时，将查出的每个列作为一个元素，用数组的形式返回
	 */
	COLUMN_TO_ARRAY
}