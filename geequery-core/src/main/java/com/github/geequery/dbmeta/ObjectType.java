package com.github.geequery.dbmeta;

/**
 * 枚举数据库对象类型
 * <ul>
 * <li>{@link #TABLE}<br>
 * 数据库表</li>
 * <li>{@link #SEQUENCE}<br>
 * 数据库序列</li>
 * <li>{@link #VIEW}<br>
 * 数据库视图</li>
 * <li>{@link #FUNCTION}<br>
 * 自定义函数</li>
 * <li>{@link #PROCEDURE}<br>
 * 存储过程</li>
 * </ul>
 * <p>
 */
public enum ObjectType {
	/**
	 * 表，包含各种临时表等
	 */
	TABLE,
	/**
	 * 序列
	 */
	SEQUENCE,
	/**
	 * 视图
	 */
	VIEW,
	/**
	 * 函数
	 */
	FUNCTION,
	/**
	 * 存储过程
	 */
	PROCEDURE
}